package su.petrosoft.apk_ack_integration.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPKR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.FINANCING_FORM;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KCSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.OWNERSHIP_FORM;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.creatingRequestDto;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.getCofinLevelRepresentationRequestDto;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.getDefaultCfl;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.buildUpdatingByCofinLevelsRequestDto;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.requestDtoToFindSubsidyProgramsForCreationCofinancingLevels;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;
import su.petrosoft.apk_ack_integration.mapper.CofinancingLevelMapper;
import su.petrosoft.apk_ack_integration.mapper.SubsidyProgramMapper;
import su.petrosoft.apk_ack_integration.model.CofinancingLevel;
import su.petrosoft.apk_ack_integration.model.DictionaryData;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.data.excel.CofinancingLevel2026ExcelRow;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateCofinancingLevelsFromExcelResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

@Slf4j
@Service
@RequiredArgsConstructor
public class CofinancingLevelService {

    private final ExcelExtractor excelExtractor;
    private final PlicanteRestClient plicanteRestClient;
    private final ApkPlicanteService plicanteService;
    private final CofinancingLevelMapper cflMapper;
    private final SubsidyProgramMapper spMapper;
    private final DictionaryService dictionaryService;

    @SneakyThrows
    public CreateCofinancingLevelsFromExcelResponseDto createFromExcel(MultipartFile file) {
//        List<CofinancingLevelExcelRow> rows = excelExtractor.getCofinancingLevelRows(file);
        List<CofinancingLevel2026ExcelRow> rows = excelExtractor.getCofinancingLevel2026Rows(file);

        Map<Long, Set<Long>> updatedByFile = new HashMap<>();
        Map<Long, Set<Long>> updatedByDefault = new HashMap<>();
        Map<Dictionary, Map<DictionaryData, Long>> codesMap = dictionaryService.getDataMap(
            Set.of(KCSR, DOPKR, OWNERSHIP_FORM, FINANCING_FORM));
        Map<SubsidyProgram, Set<CofinancingLevel>> excelEntitiesMap = rows.stream()
                .collect(groupingBy(
                        row->spMapper.toEntity(row, codesMap),
                        mapping(cflMapper::toEntity, toSet())));
        log.info("Found [{}] Subsidy_Programs in Excel file", excelEntitiesMap.size());
        log.info("Getting Existing Subsidy_Programs ...");
        Set<SubsidyProgram> existingSpList = plicanteService.findSubsidyPrograms(
                requestDtoToFindSubsidyProgramsForCreationCofinancingLevels());
        log.info("Existing Subsidy_Programs count: [{}]", existingSpList.size());

        if (!rows.isEmpty() || !existingSpList.isEmpty()) {
            HashSet<SubsidyProgram> affectedSps = new HashSet<>(existingSpList);
            affectedSps.retainAll(excelEntitiesMap.keySet());
            log.debug("Count of Existing Subsidy_Programs among found in file: [{}]", affectedSps.size());

            HashSet<SubsidyProgram> unAffectedSps = new HashSet<>(existingSpList);
            unAffectedSps.removeAll(excelEntitiesMap.keySet());

            updatedByFile = updateSubsidyProgramsByCofinLevelsFromFile(affectedSps, excelEntitiesMap, codesMap);
            updatedByDefault = updateSubsidyProgramsByDefaultCofinLevel(unAffectedSps, codesMap);
        } else {
            log.info("No one Subsidy_Program to update");
        }
        return new CreateCofinancingLevelsFromExcelResponseDto(updatedByFile, updatedByDefault);
    }

    private Map<Long, Set<Long>> updateSubsidyProgramsByDefaultCofinLevel(HashSet<SubsidyProgram> unAffectedSps, Map<Dictionary, Map<DictionaryData, Long>> codesMap) {
        Map<Long, Set<Long>> result = new HashMap<>();
        Set<SubsidyProgram> emptySubsidyPrograms = unAffectedSps.stream()
                .filter(sp -> sp.getCofinancingLevelIds().isEmpty())
                .collect(toSet());
        if (!emptySubsidyPrograms.isEmpty()) {
            log.info("Updating Subsidy_Programs by default Cofinancing_Level ...");
            log.info("Creating default Cofinancing_Level instance ...");
            CreateInstanceRequestDto creatingDto = cflMapper.toCreatingDto(getDefaultCfl(), codesMap);
            Long defaultInstanceId = plicanteRestClient.createInstance(creatingDto).id();
            log.info("Default Cofinancing_Level instance created with id [{}]", defaultInstanceId);

            log.info("Updating Subsidy_Programs by Default Cofinancing_Level ...");
            for (SubsidyProgram updatedSp : emptySubsidyPrograms) {
                updatedSp.setCofinancingLevelIds(List.of(defaultInstanceId));
                log.info("Save updating Subsidy_Program [{}]", updatedSp);
                plicanteRestClient.updateInstance(buildUpdatingByCofinLevelsRequestDto(updatedSp));
                result.put(updatedSp.getId(), Set.of(defaultInstanceId));
            }
        } else {
            log.info("All Subsidy_Programs not mentioned in file already have Cofinancing_Levels. Nothing to update");
        }
        log.info("Updated [{}] Subsidy_Programs by default: [{}]",result.size(), result);
        return result;
    }

    private Map<Long, Set<Long>> updateSubsidyProgramsByCofinLevelsFromFile(
            HashSet<SubsidyProgram> affectedSps,
            Map<SubsidyProgram, Set<CofinancingLevel>> excelEntitiesMap,
            Map<Dictionary, Map<DictionaryData, Long>> codesMap
    ) {
        if (!affectedSps.isEmpty()) {
            log.info("Updating Subsidy_Programs by Cofinancing_Levels from file ...");
        }
        Map<Long, Set<Long>> result = new HashMap<>();
        for (SubsidyProgram updatedSp : affectedSps) {
            Long subsidyProgramId = updatedSp.getId();
            Collection<Long> existingCflIdList = updatedSp.getCofinancingLevelIds();
            log.debug("Updated Subsidy_Program [{}] has Cofinancing_Levels: {}", subsidyProgramId, existingCflIdList);

            Set<CofinancingLevel> existingCflList = new HashSet<>();
            for (Long id : existingCflIdList) {
                log.debug("Getting Cofinancing_Level [{}]", id);
                List<Attribute<?>> attributes =
                        plicanteRestClient.getInstanceRepresentation(getCofinLevelRepresentationRequestDto(id));
                CofinancingLevel existingCfl = cflMapper.toEntity(attributes, codesMap);
                existingCflList.add(existingCfl);
            }
            Set<CofinancingLevel> fromFileCflList = excelEntitiesMap.get(updatedSp);
            fromFileCflList.removeAll(existingCflList);
            if (fromFileCflList.isEmpty()) {
                log.debug("All Cofinancing_Levels from file already exist in Subsidy_Program [{}]", subsidyProgramId);
                continue;
            }
            log.debug("Count of new Cofinancing_Levels to create: [{}]", fromFileCflList.size());
            Set<Long> savedCflIds = new HashSet<>();
            for (CofinancingLevel cflToSave : fromFileCflList) {
                InstanceDto savedInstance = plicanteRestClient.createInstance(creatingRequestDto(cflToSave));
                savedCflIds.add(savedInstance.id());
            }
            existingCflIdList.addAll(savedCflIds);
            log.info("Save updating Subsidy_Program [{}]", updatedSp);
            plicanteRestClient.updateInstance(buildUpdatingByCofinLevelsRequestDto(updatedSp));
            result.put(subsidyProgramId, savedCflIds);
        }
        log.info("Updated [{}] Subsidy_Programs by excel data: [{}]",result.size(), result);
        return result;
    }
}
