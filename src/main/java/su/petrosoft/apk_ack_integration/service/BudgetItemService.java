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
import su.petrosoft.apk_ack_integration.model.dto.response.CreateBudgetItemsResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateInstancesFromFileResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetCodedExcelRow;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.DOPEK;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.DOPFK;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KCSR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.DOPKR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KFSR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KOSGU;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KVR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KVSR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.OWNERSHIP_FORM;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.PURPOSE;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.CPL_TITLE;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.getCplCodesOnlyByCurrentYearRequestDto;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.FS_TITLE;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.OWNERSHIP_FORM_ATTR;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.getAllFsByCurrentYearRequestDto;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.SP_TITLE;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.getAllSubsidyProgramsRequestDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class BudgetItemService {

    private final ApkPlicanteService apkService;
    private final UniBudgetRowService rowProcessor;
    private final CashPlanLimitMapper cplMapper;
    private final SubsidyProgramMapper spMapper;
    private final FinancingSourceMapper fsMapper;
    private final SubsidyProgramService subsidyProgramService;
    private final CashPlanLimitService cashPlanLimitService;

    public CreateInstancesFromFileResponseDto createLimits(List<UniBudgetCodedExcelRow> rows) {
        if (rows.isEmpty()) {
            return CreateInstancesFromFileResponseDto.builder().build();
        }
        List<UniBudgetCodedExcelRow> uniqueRowsByCpl = getNotExistedCplRows(rows);
        if (uniqueRowsByCpl.isEmpty()) {
            return CreateInstancesFromFileResponseDto.builder()
                    .incomingCount(rows.size())
                    .build();
        }
        Map<CodeType, Map<Long, String>> codesMap = apkService.getCodesMap(
                KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE);
        Set<CashPlanLimit> savedCplList = new LinkedHashSet<>();
        for (UniBudgetCodedExcelRow row : uniqueRowsByCpl) {
            savedCplList.add(rowProcessor.saveCashPlanLimit(row, codesMap));
        }
        return CreateInstancesFromFileResponseDto.builder()
                .incomingCount(rows.size())
                .disjointCount(uniqueRowsByCpl.size())
                .persistedIds(savedCplList.stream()
                        .map(CashPlanLimit::getId)
                        .toList())
                .build();
    }

    public Set<SubsidyProgram> createSubsidyProgramsTree(List<UniBudgetCodedExcelRow> rowDtoList) {

        Set<SubsidyProgram> existingSubsidyPrograms = subsidyProgramService.getAllThirdLevelSpFromDb();
        log.info("Found [{}] valid Subsidy Programs in DB with level 3", existingSubsidyPrograms.size());

        Map<CodeType, Map<Long, String>> codesMap = apkService.getCodesMap(KCSR, DOPKR);
        Set<SubsidyProgram> createdSP = new LinkedHashSet<>();
        rowDtoList.forEach(dto -> rowProcessor.getOrCreateSubsidyProgram(dto, existingSubsidyPrograms, codesMap));
        return createdSP;
    }

    public List<FinancingSource> createFinancingSources(List<UniBudgetCodedExcelRow> rowList) {

        Set<FinancingSource> allFinancingSourcesFromDb = apkService.findFinancingSources(getAllFsByCurrentYearRequestDto());

        List<UniBudgetCodedExcelRow> uniqueFinancingSourcesFromExcel = rowList.stream()
                .filter(row -> !allFinancingSourcesFromDb.contains(fsMapper.toEntity(row)))
                .toList();

        if (uniqueFinancingSourcesFromExcel.isEmpty()) {
            log.info("All Financing Sources received from Excel already exist in DB");
            return emptyList();
        }
        log.info("Received [{}] Financing Sources from Excel to save", uniqueFinancingSourcesFromExcel.size());

        Set<CashPlanLimit> existingCashPlanLimits = cashPlanLimitService.getLimitsForCurrentYear();
        Set<SubsidyProgram> existingSubsidyPrograms = subsidyProgramService.getAllThirdLevelSpFromDb();

        Map<CodeType, Map<Long, String>> codesMap = apkService.getCodesMap(
                KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE, OWNERSHIP_FORM);
        List<FinancingSource> list = rowList.stream()
                .map(dto -> rowProcessor.buildFinancingSource(dto, existingCashPlanLimits, existingSubsidyPrograms, codesMap))
                .toList();
        log.info("[{}] Financing Sources ready to save", list.size());
        List<FinancingSource> createdFS = list.stream()
                .map(fs -> apkService.createFinancingSource(fs, codesMap))
                .toList();
        return createdFS;
    }

    public CreateBudgetItemsResponseDto createBudgetItems(List<UniBudgetCodedExcelRow> rows) {
        List<UniBudgetCodedExcelRow> rowsToProcess = getNotExistedCplRows(rows);
        if (rowsToProcess.isEmpty()) {
            log.info("No one unique BudgetItems in excel found to be saved");
            return new CreateBudgetItemsResponseDto(emptyMap());
        }
        log.info("[{}] BudgetItems from excel left as unique to be processed", rowsToProcess.size());
        Map<CodeType, Map<Long, String>> codesMap = apkService.getCodesMap(
                KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE);
        Set<SubsidyProgram> existingSpList = apkService.findSubsidyPrograms(getAllSubsidyProgramsRequestDto());
        log.info("Found [{}] Subsidy Programs in DB", existingSpList.size());

        Set<CashPlanLimit> savedCplList = new LinkedHashSet<>();
        Set<SubsidyProgram> savedSpList = new LinkedHashSet<>();
        Set<FinancingSource> savedFsList = new LinkedHashSet<>();

        log.info("Start processing BudgetItems to save containing objects");
        for (UniBudgetCodedExcelRow row : rowsToProcess) {
            CashPlanLimit savedCpl = rowProcessor.saveCashPlanLimit(row, codesMap);
            savedCplList.add(savedCpl);
            SubsidyProgram thirdLevelSp = rowProcessor.getOrCreateSubsidyProgram(row, existingSpList, codesMap);
            savedSpList.add(thirdLevelSp);
            FinancingSource savedFs = rowProcessor.saveFinancingSource(row, savedCpl, thirdLevelSp, codesMap);
            savedFsList.add(savedFs);
        }
        return buildResponse(savedCplList, savedSpList, savedFsList);
    }

    private List<UniBudgetCodedExcelRow> getNotExistedCplRows(List<UniBudgetCodedExcelRow> rows) {
        Set<CashPlanLimit> currentYearExistingLimits = apkService.findCashPlanLimits(getCplCodesOnlyByCurrentYearRequestDto());
        log.info("Found [{}] CashPlanLimits for [{}] year in DB", currentYearExistingLimits.size(), LocalDateTime.now().getYear());
        List<UniBudgetCodedExcelRow> uniqueCplRows = rows.stream()
                .filter(row -> !currentYearExistingLimits.contains(cplMapper.toCpl(row)))
                .toList();
        return uniqueCplRows;
    }

    private static CreateBudgetItemsResponseDto buildResponse(
            Collection<CashPlanLimit> savedCplList,
            Collection<SubsidyProgram> savedSpList,
            Collection<FinancingSource> savedFsList) {
        return new CreateBudgetItemsResponseDto(
                Map.of(
                        CPL_TITLE, savedCplList.stream()
                                .map(CashPlanLimit::getId)
                                .collect(toSet()),
                        SP_TITLE, savedSpList.stream()
                                .map(SubsidyProgram::getId)
                                .collect(toSet()),
                        FS_TITLE, savedFsList.stream()
                                .map(FinancingSource::getId)
                                .collect(toSet())));
    }
}
