package su.petrosoft.apk_ack_integration.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.FINANCING_FORM;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.OWNERSHIP_FORM;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.getCofinLevelRepresentationRequestDto;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.getCreatingRequestDto;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.getDefaultCfl;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.buildUpdatingByCofinLevelsRequestDto;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.requestDtoToFindSubsidyProgramsForCreationCofinancingLevels;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
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
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.data.excel.CofinancingLevel2026ExcelRow;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

@Slf4j
@Service
@RequiredArgsConstructor
public class CofinancingLevelService {

    private final ExcelExtractor excelExtractor;
    private final ApkPlicanteService plicanteService;
    private final CofinancingLevelMapper cflMapper;
    private final SubsidyProgramMapper spMapper;
    private final PlicanteRestClient plicanteRestClient;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void createFromExcel(MultipartFile file) {
//        List<CofinancingLevelExcelRow> rows = excelExtractor.getCofinancingLevelRows(file);
        List<CofinancingLevel2026ExcelRow> rows = excelExtractor.getCofinancingLevel2026Rows(file);

        Map<SubsidyProgram, Set<CofinancingLevel>> excelEntitiesMap = rows.stream()
            .collect(groupingBy(
                spMapper::toEntity,
                mapping(cflMapper::toEntity, toSet())));
        log.info("Extracted Subsidy_Programs from Excel count [{}]", excelEntitiesMap.size());

        List<SubsidyProgram> existingSpList = plicanteService.findSubsidyPrograms(
            requestDtoToFindSubsidyProgramsForCreationCofinancingLevels());
        log.info("Found existing Subsidy_Programs count [{}]", existingSpList.size());

        if (rows.isEmpty() && existingSpList.isEmpty()) {
            log.info("No one Subsidy_Program to update");
            return;
        }

        HashSet<SubsidyProgram> affectedSps = new HashSet<>(existingSpList);
        affectedSps.retainAll(excelEntitiesMap.keySet());
        log.debug("Existing Subsidy_Programs contained in excel count [{}]", affectedSps.size());

        HashSet<SubsidyProgram> unAffectedSps = new HashSet<>(existingSpList);
        unAffectedSps.removeAll(excelEntitiesMap.keySet());
        log.debug("Existing Subsidy_Programs not mentioned in excel count [{}]",
            unAffectedSps.size());

        Map<Dictionary, Map<String, Long>> codesMap = plicanteService.getCodesMap(OWNERSHIP_FORM,
            FINANCING_FORM);

        for (SubsidyProgram subsidyProgram : affectedSps) {
            List<Long> existingCflIdList = subsidyProgram.getCofinancingLevelIds();
            log.debug("Updated Subsidy_Program [{}] has Cofinancing_Levels: {}",
                subsidyProgram.getId(), existingCflIdList);

            Set<CofinancingLevel> existingCflList = new HashSet<>();
            for (Long id : existingCflIdList) {
                List<Attribute<?>> attributes =
                    plicanteRestClient.getInstanceRepresentation(
                        getCofinLevelRepresentationRequestDto(id));
                log.debug("Existing Cofinancing_Levels attributes: [{}]", attributes);
                CofinancingLevel existingCfl = cflMapper.toEntity(attributes, codesMap);
                log.debug("Existing Cofinancing_Levels [{}]", existingCfl);
                existingCflList.add(existingCfl);
            }
            Set<CofinancingLevel> cflListToSave = excelEntitiesMap.get(subsidyProgram);
            cflListToSave.removeAll(existingCflList);
            if (cflListToSave.isEmpty()) {
                continue;
            }

            List<Long> savedCflIds = new ArrayList<>();
            for (CofinancingLevel cflToSave : cflListToSave) {
                CreateInstanceRequestDto creatingRequestDto = getCreatingRequestDto(cflToSave,
                    codesMap);
                String s = objectMapper.writeValueAsString(creatingRequestDto);
                log.debug("Cofinancing_Level creating json: [{}]", s);
                InstanceDto savedInstance = plicanteRestClient.createInstance(creatingRequestDto);
                savedCflIds.add(savedInstance.id());
            }
            existingCflIdList.addAll(savedCflIds);
            log.debug("Subsidy_Program to update [{}]", subsidyProgram);
            InstanceDto updatedInstance = plicanteRestClient.updateInstance(
                buildUpdatingByCofinLevelsRequestDto(subsidyProgram));
            log.debug("Updated Subsidy_Program [{}]", updatedInstance);
        }

        if (unAffectedSps.stream()
            .noneMatch(sp -> sp.getCofinancingLevelIds().isEmpty())) {
            return;
        }

        CreateInstanceRequestDto requestDto = cflMapper.toCreatingDto(getDefaultCfl(), codesMap);
        Long defaultInstanceId = plicanteRestClient.createInstance(requestDto).id();
        for (SubsidyProgram subsidyProgram : unAffectedSps) {
            if (subsidyProgram.getCofinancingLevelIds().isEmpty()) {
                subsidyProgram.setCofinancingLevelIds(List.of(defaultInstanceId));
                plicanteRestClient.updateInstance(
                    buildUpdatingByCofinLevelsRequestDto(subsidyProgram));
            }
        }
    }
}
