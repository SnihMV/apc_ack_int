package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.mapper.FinancingSourceMapper;
import su.petrosoft.apk_ack_integration.mapper.SubsidyProgramMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.FinancingSource;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.CashPlanLimitExcelRow;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetExcelRowDto;
import su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.*;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KADMR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KCSR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KDR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KESR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KFSR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KVR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.LEVEL_ATTR;

@Slf4j
@Component
@RequiredArgsConstructor
public class UniBudgetService {

    private final ApkPlicanteService apkService;
    private final UniBudgetRowService rowProcessor;
    private final SubsidyProgramMapper spMapper;
    private final FinancingSourceMapper fsMapper;

    public Set<SubsidyProgram> createSubsidyProgramsTree(List<UniBudgetExcelRowDto> rowDtoList) {

        List<SubsidyProgram> allSubsidyProgramsFromDb = apkService.getAllSubsidyPrograms();
        log.debug("Found [{}] SubsidyPrograms in DB in total", allSubsidyProgramsFromDb.size());

        Set<SubsidyProgram> existingSP = allSubsidyProgramsFromDb.stream()
                .filter(SubsidyProgramUtil::validate)
                .collect(toSet());
        log.debug("There are [{}] valid and unique SubsidyPrograms of all", existingSP.size());

        Map<CodeType, Map<Long, String>> codesMap = apkService.getCodesMap(KCSR, KDR);
        Set<SubsidyProgram> createdSP = new LinkedHashSet<>();
        rowDtoList.forEach(dto -> rowProcessor.createSubsidyPrograms(dto, existingSP, codesMap));
        return createdSP;
    }

    public List<FinancingSource> createFinancingSources(List<UniBudgetExcelRowDto> dtoList) {

        Set<FinancingSource> allFsFromDb = apkService.getAllFinancingSources();
        List<FinancingSource> allFsFromExcel = dtoList.stream()
                .map(fsMapper::fromUniBudgetDto)
                .collect(toList());
        allFsFromExcel.removeAll(allFsFromDb);
        if (allFsFromExcel.isEmpty()) {
            return Collections.emptyList();
        }

        Set<SubsidyProgram> allThirdLevelSpFromExcel = dtoList.stream()
                .map(spMapper::toThirdLevelSP)
                .collect(toSet());

        List<SubsidyProgram> allThirdLevelSpFromDB = apkService.getAllSubsidyPrograms(Map.of(LEVEL_ATTR, 3L));
        log.debug("Found [{}] Subsidy Programs in DB with level 3", allThirdLevelSpFromDB.size());

        Set<SubsidyProgram> allValidThirdLvlSPFromDb = allThirdLevelSpFromDB.stream()
                .filter(SubsidyProgramUtil::validate)
                .collect(toSet());
        log.info("Found [{}] valid Subsidy Programs in DB with level 3", allValidThirdLvlSPFromDb.size());


        if (!allValidThirdLvlSPFromDb.containsAll(allThirdLevelSpFromExcel)) {
            log.info("Excel file contains Subsidy Programs which not existing in DB. They will be saved at first...");
            Set<SubsidyProgram> newlyCreatedSP = createSubsidyProgramsTree(dtoList);
            newlyCreatedSP.stream()
                    .peek(sp -> log.debug("Newly created Subsidy Program: [{}]", sp))
                    .filter(sp -> sp.getLevel() == 3)
                    .forEach(allValidThirdLvlSPFromDb::add);
            log.info("Updated count of 3 level Subsidy Programs in DB: [{}]", allValidThirdLvlSPFromDb.size());
        }

        List<FinancingSource> list = dtoList.stream()
                .map(dto -> rowProcessor.buildFinancingSource(dto, allValidThirdLvlSPFromDb))
                .toList();
        log.info("[{}] Financing Sources ready to save", list.size());
        Map<CodeType, Map<Long, String>> codesMap = apkService.getCodesMap();
        List<FinancingSource> createdFS = list.stream()
                .map(fs -> apkService.createFinancingSource(fs, codesMap))
                .toList();
        return createdFS;
    }


}
