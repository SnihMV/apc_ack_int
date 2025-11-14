package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.FinancingSource;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetExcelRowDto;
import su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KADMR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KCSR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KDR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KESR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KFSR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KVR;
import static su.petrosoft.apk_ack_integration.util.DictionaryUtil.getCodeId;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.LEVEL_ATTR;

@Slf4j
@Component
@RequiredArgsConstructor
public class UniBudgetService {

    private final ApkPlicanteService apkService;
    private final ExcelRowParser rowProcessor;

    public Set<SubsidyProgram> createSubsidyProgramsTree(
            List<UniBudgetExcelRowDto> rowDtoList) {

        List<SubsidyProgram> allSubsidyProgramsFromDb = apkService.getAllSubsidyPrograms();
        log.debug("Found [{}] SubsidyPrograms in DB in total", allSubsidyProgramsFromDb.size());

        Set<SubsidyProgram> existingSP = allSubsidyProgramsFromDb.stream()
                .filter(SubsidyProgramUtil::validate)
                .collect(Collectors.toSet());
        log.debug("There are [{}] valid and unique SubsidyPrograms of all", existingSP.size());
        Map<CodeType, Map<Long, String>> codesMap = apkService.getCodesMap(KCSR, KDR);
        Set<SubsidyProgram> createdSP = new LinkedHashSet<>();
        rowDtoList.forEach(dto -> processRow(dto, existingSP, createdSP, codesMap));
        return createdSP;
    }

    public Set<FinancingSource> createFinancingSources(List<UniBudgetExcelRowDto> dtoList) {

        Set<SubsidyProgram> allThirdLevelSpFromExcel = dtoList.stream()
                .map(rowProcessor::getThirdLevelSP)
                .collect(Collectors.toSet());

        List<SubsidyProgram> allThirdLevelSpFromDB = apkService.getAllSubsidyPrograms(Map.of(LEVEL_ATTR, 3L));
        log.info("Found [{}] Subsidy Programs in DB with level 3", allThirdLevelSpFromDB.size());

        Set<SubsidyProgram> allValidThirdLvlSPFromDb = allThirdLevelSpFromDB.stream()
                .filter(SubsidyProgramUtil::validate)
                .collect(Collectors.toSet());

        if (!allValidThirdLvlSPFromDb.containsAll(allThirdLevelSpFromExcel)) {
            log.info("Excel file contains Subsidy Programs which not existing in DB. They will be saved at first...");
            Set<SubsidyProgram> newlyCreatedSP = createSubsidyProgramsTree(dtoList);
            newlyCreatedSP.stream()
                    .peek(sp -> log.debug("Newly created Subsidy Program: [{}]", sp))
                    .filter(sp -> sp.getLevel() == 3)
                    .forEach(allValidThirdLvlSPFromDb::add);
            log.info("Updated count of 3 level Subsidy Programs in DB: [{}]", allThirdLevelSpFromDB.size());
        }
        Map<CodeType, Map<Long, String>> codesMap = apkService.getCodesMap(KADMR, KFSR, KCSR, KVR, KESR);
        dtoList.stream()
                .forEach();
    }

    private SubsidyProgram processRow(
            UniBudgetExcelRowDto dto,
            Set<SubsidyProgram> existingSP,
            Set<SubsidyProgram> createdSP,
            Map<CodeType, Map<Long, String>> codesMap) {

        SubsidyProgram fstLevelSp = getFirstLevelSP(dto, existingSP, createdSP);
        SubsidyProgram scdLevelSp = getSecondLevelSP(dto, existingSP, createdSP, fstLevelSp, codesMap);
        SubsidyProgram trdLevelSp = getThirdLevelSP(dto, existingSP, createdSP, scdLevelSp, codesMap);
        log.debug("Subsidy Program Map count: {}", existingSP);
        return trdLevelSp;
    }

    private SubsidyProgram getFirstLevelSP(
            UniBudgetExcelRowDto dto,
            Set<SubsidyProgram> existingSP,
            Set<SubsidyProgram> createdSP) {
        SubsidyProgram fstLvlSp =
                SubsidyProgram.builder()
                        .level(1L)
                        .code(dto.code())
                        .title("Направление № " + dto.code())
                        .build();
        Long id = obtainSubsidyProgramId(fstLvlSp, existingSP, createdSP);
        fstLvlSp.setId(id);
        log.debug("First level Subsidy Program from excel row: [{}]", fstLvlSp);
        return fstLvlSp;
    }

    private SubsidyProgram getSecondLevelSP(
            UniBudgetExcelRowDto dto,
            Set<SubsidyProgram> existingSP,
            Set<SubsidyProgram> createdSP,
            SubsidyProgram fstLevelSp,
            Map<CodeType, Map<Long, String>> codesMap) {

        SubsidyProgram scdLvlSP = SubsidyProgram.builder()
                .level(2L)
                .parentId(fstLevelSp.getId())
                .title(dto.kcsrTitle())
                .kcsr(getCodeId(codesMap, KCSR, dto.kcsr()))
                .build();
        Long id = obtainSubsidyProgramId(scdLvlSP, existingSP, createdSP);
        scdLvlSP.setId(id);
        log.debug("Second level Subsidy Program from excel row: [{}]", scdLvlSP);
        return scdLvlSP;
    }

    private SubsidyProgram getThirdLevelSP(
            UniBudgetExcelRowDto dto,
            Set<SubsidyProgram> existingSP,
            Set<SubsidyProgram> createdSP,
            SubsidyProgram scdLevelSp,
            Map<CodeType, Map<Long, String>> codesMap) {

        SubsidyProgram trdLvlSP = SubsidyProgram.builder()
                .level(3L)
                .parentId(scdLevelSp.getId())
                .title(dto.dopKrTitle())
                .kcsr(scdLevelSp.getKcsr())
                .dopKr(getCodeId(codesMap, KDR, dto.dopKr()))
                .build();
        Long id = obtainSubsidyProgramId(trdLvlSP, existingSP, createdSP);
        trdLvlSP.setId(id);
        log.debug("Third level Subsidy Program from excel row: [{}]", trdLvlSP);
        return trdLvlSP;
    }

    private Long obtainSubsidyProgramId(
            SubsidyProgram sp,
            Set<SubsidyProgram> existingSP,
            Set<SubsidyProgram> createdSP) {

        return existingSP.stream()
                .filter(existing -> existing.equals(sp))
                .findFirst()
                .map(SubsidyProgram::getId)
                .orElseGet(() -> {
                    log.debug("No such Subsidy Program among existing. Trying to save it");
                    SubsidyProgram saved = apkService.createProgram(sp);
                    log.debug("Subsidy Program successfully saved with id: [{}]", saved.getId());
                    existingSP.add(saved);
                    createdSP.add(saved);
                    return saved.getId();
                });
    }

}
