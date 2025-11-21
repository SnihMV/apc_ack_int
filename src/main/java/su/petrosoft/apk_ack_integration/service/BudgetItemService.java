package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.mapper.FinancingSourceMapper;
import su.petrosoft.apk_ack_integration.mapper.SubsidyProgramMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.FinancingSource;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateBudgetItemsResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateFromExcelResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetExcelRow;
import su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.*;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KCSR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.DOPKR;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.cashPlanLimitsCodesByCurrentYear;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.LEVEL_ATTR;

@Slf4j
@Component
@RequiredArgsConstructor
public class BudgetItemService {

    private final ApkPlicanteService apkService;
    private final UniBudgetRowService rowProcessor;
    private final CashPlanLimitMapper cplMapper;
    private final SubsidyProgramMapper spMapper;
    private final FinancingSourceMapper fsMapper;

    public CreateFromExcelResponseDto createLimits(MultipartFile file) {
        List<UniBudgetExcelRow> dtoList = excelExtractor.getUniBudgetRows(file);

        CreateFromExcelResponseDto responseDto = CreateFromExcelResponseDto.builder()
                .persistedIds(Collections.emptyList())
                .build();

        if (!dtoList.isEmpty()) {
            responseDto = cplService.createFromUniBudgetExcel(dtoList);
        }
        return responseDto;
    }

    public Set<SubsidyProgram> createSubsidyProgramsTree(List<UniBudgetExcelRow> rowDtoList) {

        Set<SubsidyProgram> allSubsidyProgramsFromDb = apkService.getAllSubsidyPrograms();
        log.debug("Found [{}] SubsidyPrograms in DB in total", allSubsidyProgramsFromDb.size());

        Set<SubsidyProgram> existingSP = allSubsidyProgramsFromDb.stream()
                .filter(SubsidyProgramUtil::validate)
                .collect(toSet());
        log.debug("There are [{}] valid and unique SubsidyPrograms of all", existingSP.size());

        Map<CodeType, Map<Long, String>> codesMap = apkService.getCodesMap(KCSR, DOPKR);
        Set<SubsidyProgram> createdSP = new LinkedHashSet<>();
        rowDtoList.forEach(dto -> rowProcessor.getOrCreateSubsidyProgram(dto, existingSP, codesMap, savedSpIds));
        return createdSP;
    }

    public List<FinancingSource> createFinancingSources(List<UniBudgetExcelRow> dtoList) {

        Set<FinancingSource> allFsFromDb = apkService.getAllFinancingSources();
        List<FinancingSource> allFsFromExcel = dtoList.stream()
                .map(fsMapper::toEntity)
                .collect(toList());
        allFsFromExcel.removeAll(allFsFromDb);
        if (allFsFromExcel.isEmpty()) {
            return Collections.emptyList();
        }

        Set<SubsidyProgram> allThirdLevelSpFromExcel = dtoList.stream()
                .map(spMapper::toThirdLevelSP)
                .collect(toSet());

        Set<SubsidyProgram> allThirdLevelSpFromDB = apkService.getAllSubsidyPrograms(Map.of(LEVEL_ATTR, 3L));
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


    public CreateBudgetItemsResponseDto createAll(List<UniBudgetExcelRow> dtoList) {

        Set<CashPlanLimit> currentYearLimits = apkService.getAllCashPlanLimits(cashPlanLimitsCodesByCurrentYear());
        Set<CashPlanLimit> limitsToSave = dtoList.stream()
                .map(cplMapper::toCpl)
                .collect(toSet());

        limitsToSave.removeAll(currentYearLimits);
        Set<CashPlanLimit> createdLimits = new HashSet<>();
        if (limitsToSave.isEmpty()) {
            createdLimits = Collections.emptySet();
        }
        Map<CodeType, Map<Long, String>> codesMap = apkService.getCodesMap();
        limitsToSave.stream()
                .map(cpl -> apkService.createCashPlanLimit(cpl, codesMap))
                .forEach(createdLimits::add);
        currentYearLimits.addAll(createdLimits);



    }

    public CreateBudgetItemsResponseDto createBudgetItems(List<UniBudgetExcelRow> rows) {
        Set<CashPlanLimit> currentYearExistingLimits = apkService.getAllCashPlanLimits(cashPlanLimitsCodesByCurrentYear());
        List<UniBudgetExcelRow> rowsToProcess = rows.stream()
                .filter(row -> !currentYearExistingLimits.contains(cplMapper.toCpl(row)))
                .toList();
        if (rowsToProcess.isEmpty()) {
            return new CreateBudgetItemsResponseDto(Collections.emptyMap());
        }
        Map<CodeType, Map<Long, String>> codesMap = apkService.getCodesMap();
        Set<SubsidyProgram> existingSpList = apkService.getAllSubsidyPrograms();
        Set<CashPlanLimit> savedCplList = new LinkedHashSet<>();
        Set<CashPlanLimit> savedSpList = new LinkedHashSet<>();
        Set<CashPlanLimit> savedFsList = new LinkedHashSet<>();
        for (UniBudgetExcelRow row : rowsToProcess) {
            CashPlanLimit savedCpl = rowProcessor.saveCashPlanLimit(row, codesMap);
            savedCplList.add(savedCpl.getId());
            SubsidyProgram trdLevelSp = rowProcessor.getOrCreateSubsidyProgram(row, existingSpList, codesMap, savedSpIds);
            savedSpIds.add(trdLevelSp)
            FinancingSource financingSource = rowProcessor.saveFinancingSource(row, savedCpl, trdLevelSp, codesMap);
        }
    }
}
