package su.petrosoft.apk_ack_integration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.ResourceTransformer;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;
import su.petrosoft.apk_ack_integration.mapper.CofinancingLevelMapper;
import su.petrosoft.apk_ack_integration.mapper.SubsidyProgramMapper;
import su.petrosoft.apk_ack_integration.model.CofinancingLevel;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;
import su.petrosoft.apk_ack_integration.model.data.excel.CofinancingLevelExcelRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.FINANCING_FORM;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.OWNERSHIP_FORM;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.*;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.*;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.buildGettingSubsidyProgramsForCreationCofinancingLevelsRequestDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class CofinancingLevelService {
    private final ExcelExtractor excelExtractor;
    private final ApkPlicanteService plicanteService;
    private final CofinancingLevelMapper cflMapper;
    private final SubsidyProgramMapper spMapper;
    private final PlicanteRestClient plicanteRestClient;
    private final ResourceTransformer resourceTransformer;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void createFromExcel(MultipartFile file) {
        List<CofinancingLevelExcelRow> rows = excelExtractor.getCofinancingLevelRows(file);
        if (rows.isEmpty()) {
            return;
        }

        Map<SubsidyProgram, Set<CofinancingLevel>> excelEntitiesMap = rows.stream()
                .collect(groupingBy(
                        spMapper::toEntity,
                        mapping(cflMapper::toEntity, toSet())));
        log.debug("=== EXCEL ROW === [{}]", excelEntitiesMap);
        log.info("Unique Subsidy Programs from Excel count [{}]", excelEntitiesMap.size());

        Set<SubsidyProgram> existedSPs = plicanteService.findSubsidyPrograms(
                buildGettingSubsidyProgramsForCreationCofinancingLevelsRequestDto());
        log.info("Found Subsidy Programs in DB count [{}]", existedSPs.size());

        existedSPs.retainAll(excelEntitiesMap.keySet());
        log.info("Subsidy Programs to update count [{}]", existedSPs.size());

        if (existedSPs.isEmpty()) {
            log.info("No Subsidy Programs to update");
            return;
        }
        Map<Dictionary, Map<String, Long>> codesMap = plicanteService.getCodesMap(OWNERSHIP_FORM, FINANCING_FORM);

        for (SubsidyProgram updatedSP : existedSPs) {
            log.debug("=== Existed SP === [{}]", updatedSP);

            List<Long> currentCflIds = updatedSP.getCofinancingLevelIds();
            Set<CofinancingLevel> cflListToSave = excelEntitiesMap.get(updatedSP);
            
            List<Long> savedCflIds = new ArrayList<>();
            for (CofinancingLevel cflToSave : cflListToSave) {
                CreateInstanceRequestDto creatingRequestDto = getCreatingRequestDto(cflToSave, codesMap);
                String s = objectMapper.writeValueAsString(creatingRequestDto);
                log.debug("=== CFL creating JSON === [{}]", s);
                InstanceDto savedInstance = plicanteRestClient.createInstance(
                        creatingRequestDto);
                savedCflIds.add(savedInstance.id());
            }
            currentCflIds.addAll(savedCflIds);
            log.debug("=== Updated SP === [{}]", updatedSP);
            InstanceDto updatedInstance = plicanteRestClient.updateInstance(
                    buildUpdatingByCofinLevelsRequestDto(updatedSP));
            log.debug("=== Updated SP Instance === [{}]", updatedInstance);

        }
    }
}
