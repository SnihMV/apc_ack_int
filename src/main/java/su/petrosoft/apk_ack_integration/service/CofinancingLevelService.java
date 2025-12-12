package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.mapper.CofinancingLevelMapper;
import su.petrosoft.apk_ack_integration.mapper.SubsidyProgramMapper;
import su.petrosoft.apk_ack_integration.model.CofinancingLevel;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.CofinancingLevelExcelRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.FINANCING_FORM;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.buildGettingSubsidyProgramsForCreationCofinancingLevelsRequestDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class CofinancingLevelService {
    private final ExcelExtractor excelExtractor;
    private final ApkPlicanteService plicanteService;
    private final CofinancingLevelMapper cflMapper;
    private final SubsidyProgramMapper spMapper;

    public void createFromExcel(MultipartFile file) {
        List<CofinancingLevelExcelRow> rows = excelExtractor.getCofinancingLevelRows(file);
        if (rows.isEmpty()) {
            return;
        }

        Map<SubsidyProgram, Set<CofinancingLevel>> excelEntitiesMap = rows.stream()
                .collect(groupingBy(
                        spMapper::toEntity,
                        mapping(cflMapper::toEntity, toSet())));
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
        Map<CodeType, Map<Long, String>> codesMap = plicanteService.getCodesMap(FINANCING_FORM);

        for (SubsidyProgram updatedSP : existedSPs) {
            Set<CofinancingLevel> cflListToSave = excelEntitiesMap.get(updatedSP);
            List<CofinancingLevel> savedCfls = new ArrayList<>();
            for (CofinancingLevel cflToSave : cflListToSave) {
//                TODO: continue developing

            }
        }

        for (Map.Entry<SubsidyProgram, Set<CofinancingLevel>> entry : excelEntitiesMap.entrySet()) {
            List<Long> savedCflIds = entry.getValue().stream()
                    .map(cfl -> plicanteService.createCofinancingLevel(cfl, codesMap))
                    .map(CofinancingLevel::getId)
                    .toList();
            SubsidyProgram sp = entry.getKey();
            sp.getCofinancingLevelIds().addAll(savedCflIds);
            plicanteService.updateSubsidyProgram(sp);
            log.debug("=== UPDATED SP === [{}]", sp);
        }


//        for (CofinancingLevelExcelRow row : rows) {
//            SubsidyProgram sp = spMapper.toEntity(row);
//            log.debug("+++ EXCEL SP +++ [{}]", sp);
//            Optional<SubsidyProgram> optionalSP = existedSPs.stream()
//                    .filter(sp::equals)
//                    .findFirst();
//            if (optionalSP.isEmpty()) {
//                continue;
//            }
//            SubsidyProgram foundSP = optionalSP.get();
//            CofinancingLevel excelCFL = cflMapper.toEntity(row);
//            CofinancingLevel createdCFL = plicanteService.createCofinancingLevel(excelCFL, codesMap);
//            ArrayList<Long> copiedCflIds = new ArrayList<>(foundSP.getCofinancingLevelIds());
//            copiedCflIds.add(createdCFL.getId());
//            foundSP.setCofinancingLevelIds(copiedCflIds);
//            SubsidyProgram updatedSP = plicanteService.updateSubsidyProgram(foundSP);
//            log.debug("=== UPDATED SP === [{}]", updatedSP);
//        }
    }
}
