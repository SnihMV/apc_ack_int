package su.petrosoft.apk_ack_integration.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.mapper.FinancingSourceMapper;
import su.petrosoft.apk_ack_integration.mapper.SubsidyProgramMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.FinancingSource;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.data.CashPlanLimitData;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateBudgetItemsResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.response.CreatingInstancesFromFileResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;
import su.petrosoft.apk_ack_integration.model.data.DescriptedBudgetItemData;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPEK;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPFK;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KCSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPKR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KFSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KOSGU;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KVR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KVSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.OWNERSHIP_FORM;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.PURPOSE;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.CPL_TITLE;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.getCplCodesOnlyByCurrentYearRequestDto;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.requestDtoToFindCashPlanLimitsByIds;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.FS_TITLE;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.getAllFsByCurrentYearRequestDto;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.updateCodesMap;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.SP_TITLE;

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
    private final PlicanteRestClient plicanteRestClient;

    public CreatingInstancesFromFileResponseDto createLimits(List<CashPlanLimitData> rows) {
        if (rows.isEmpty()) {
            return CreatingInstancesFromFileResponseDto.builder().build();
        }
        List<CashPlanLimitData> uniqueRowsByCpl = getNotExistedCplRows(rows);
        if (uniqueRowsByCpl.isEmpty()) {
            return CreatingInstancesFromFileResponseDto.builder()
                .incomingCount(rows.size())
                .build();
        }
        Map<Dictionary, Map<String, Long>> codesMap = apkService.getDictionariesCodesMap(
            KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE);
        Set<CashPlanLimit> savedCplList = new LinkedHashSet<>();
        for (CashPlanLimitData row : uniqueRowsByCpl) {
            savedCplList.add(rowProcessor.saveCashPlanLimit(row, codesMap));
        }
        return CreatingInstancesFromFileResponseDto.builder()
            .incomingCount(rows.size())
            .disjointCount(uniqueRowsByCpl.size())
            .persistedIds(savedCplList.stream()
                .map(CashPlanLimit::getId)
                .toList())
            .build();
    }

    public Set<SubsidyProgram> createSubsidyProgramsTree(
        List<DescriptedBudgetItemData> rowDtoList) {

        Set<SubsidyProgram> existingSndLvlSubsidyPrograms = subsidyProgramService.getAllSecondLevelSpFromDb();
        log.info("Found [{}] Subsidy Programs in DB with level 2",
            existingSndLvlSubsidyPrograms.size());

        List<DescriptedBudgetItemData> unknownSpRows = rowDtoList.stream()
            .filter(row -> !existingSndLvlSubsidyPrograms.contains(spMapper.toSecondLevelSP(row)))
            .toList();

        Set<SubsidyProgram> createdSP = new LinkedHashSet<>();

        if (unknownSpRows.isEmpty()) {
            log.info("No one rows with unknown SP found");
            return createdSP;
        }
        log.info("Rows with unknown SP: [{}]", unknownSpRows.size());

        Set<SubsidyProgram> allSpFromDb = subsidyProgramService.getAllSubsidyProgram();
        Map<Dictionary, Map<String, Long>> codesMap = apkService.getDictionariesCodesMap(KCSR,
            DOPKR);
        updateCodesMap(codesMap, unknownSpRows, plicanteRestClient);
        unknownSpRows.forEach(
            row -> rowProcessor.getOrCreateSubsidyProgram(row, allSpFromDb, codesMap));
        return createdSP;
    }


    public List<FinancingSource> createFinancingSources(List<DescriptedBudgetItemData> rows) {
        List<FinancingSource> result = new ArrayList<>();
        if (rows == null || rows.isEmpty()) {
            return result;
        }
        Map<FinancingSource, Set<CashPlanLimit>> excelEntitiesMap = rows.stream()
            .collect(groupingBy(
                fsMapper::toEntity,
                mapping(cplMapper::toEntity, toSet())));
        log.info("Found [{}] Financing_Sources in Excel file", excelEntitiesMap.size());

        Map<Dictionary, Map<Long, Entry<String, String>>> codesMap = apkService.getDictionariesCodesMap(
            KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE, OWNERSHIP_FORM);

        log.info("Getting Existing Financing_Sources ...");
        Set<FinancingSource> existingFS = apkService.findFinancingSources(
            getAllFsByCurrentYearRequestDto(), codesMap);
        log.info("Existing Financing_Sources count: [{}]", existingFS.size());

        updateCodesMap(codesMap, rows, plicanteRestClient);

        HashSet<FinancingSource> finSourcesToUpdate = new HashSet<>(existingFS);
        finSourcesToUpdate.retainAll(excelEntitiesMap.keySet());
        log.info("Count of Existing Financing_Sources found in file: [{}]",finSourcesToUpdate.size());

        HashMap<FinancingSource, Set<CashPlanLimit>> entitiesToCreate = new HashMap<>(excelEntitiesMap);
        entitiesToCreate.keySet().removeAll(existingFS);
        log.info("Count of New Financing_Sources found in file: [{}]", entitiesToCreate);

        Map<Long, List<Long>> updatedFinancingSources =
            updateFinancingSources(finSourcesToUpdate, excelEntitiesMap, codesMap);

        createNewFinancingSources(entitiesToCreate, codesMap);

//        createNewFinancingSources(excelEntitiesMap, codesMap);

//        updatedByFile = updateSubsidyProgramsByCofinLevelsFromFile(affectedFsList, excelEntitiesMap,
//            codesMap);
//        updatedByDefault = updateSubsidyProgramsByDefaultCofinLevel(unAffectedSps, codesMap);

        List<DescriptedBudgetItemData> notExistingFsRows = rows.stream()
            .filter(row -> !existingFS.contains(fsMapper.toEntity(row)))
            .toList();

        if (notExistingFsRows.isEmpty()) {
            log.info("All received Financing Sources already exist in DB");
            return emptyList();
        }
        log.info("Received [{}] new Financing Sources", notExistingFsRows.size());

        Set<CashPlanLimit> existingCashPlanLimits = cashPlanLimitService.getLimitsForCurrentYear();
        Set<SubsidyProgram> existingSubsidyPrograms = subsidyProgramService.getAllSubsidyProgram();

        List<FinancingSource> list = notExistingFsRows.stream()
            .map(row -> rowProcessor.buildFinancingSource(row, existingCashPlanLimits,
                existingSubsidyPrograms, codesMap))
            .toList();
        log.info("[{}] Financing Sources ready to save", list.size());
        List<FinancingSource> createdFS = list.stream()
            .map(fs -> apkService.createFinancingSource(fs, codesMap))
            .toList();
        return createdFS;
    }

    private void createNewFinancingSources(
        HashMap<FinancingSource, Set<CashPlanLimit>> entitiesToCreate,
        Map<Dictionary, Map<String, Long>> codesMap
    ) {
        Set<SubsidyProgram> existingSpList = new HashSet<>();
        if (!entitiesToCreate.isEmpty()) {
            existingSpList = subsidyProgramService.getAllSubsidyProgram();
        }
        for (Entry<FinancingSource, Set<CashPlanLimit>> entry : entitiesToCreate.entrySet()) {
            buildFinancingSource(entry.getKey(), entry.getValue(), existingSpList, codesMap);
        }


    }

    public void buildFinancingSource(
        FinancingSource financingSource,
        Set<CashPlanLimit> newCashPlanLimits,
        Set<SubsidyProgram> existingSubsidyPrograms,
        Map<Dictionary, Map<String, Long>> codesMap
    ) {
        List<Long> createdCplIds = new ArrayList<>();
        for (CashPlanLimit cpl : newCashPlanLimits) {
            CashPlanLimit createdCpl = apkService.createCashPlanLimit(cpl, codesMap);
            createdCplIds.add(createdCpl.getId());
        }
        financingSource.setCashPlanLimitIds(createdCplIds);
        SubsidyProgram scdLevelProgramSearchKey = financingSource.extractSubsidyProgram();
        for (SubsidyProgram existingSp : existingSubsidyPrograms) {
            if (scdLevelProgramSearchKey.equals(existingSp)) {
                financingSource.setSubsidyProgramId(existingSp.getId());
                return;
            }
        }
        long createdSpId = createSubsidyProgram(scdLevelProgramSearchKey, existingSubsidyPrograms, codesMap);


        existingSubsidyPrograms.stream()
            .filter(sp -> sp.equals(scdLevelProgramSearchKey))
            .findFirst()
            .map(SubsidyProgram::getId)
            .ifPresentOrElse(financingSource::setSubsidyProgramId,
                () -> {
                    SubsidyProgram sp = getOrCreateSubsidyProgram(row, existingSubsidyPrograms,
                        codesMap);
                    financingSource.setSubsidyProgramId(sp.getId());
                });
        return financingSource;
    }

    private long createSubsidyProgram(
        SubsidyProgram scdLevelSubsidyProgram,
        Set<SubsidyProgram> existingSubsidyPrograms,
        Map<Dictionary, Map<String, Long>> codesMap
    ) {
        SubsidyProgram fstLevelProgramSearchKey = SubsidyProgram.builder()
            .level(1L)
            .kcsr(scdLevelSubsidyProgram.getKcsr())
            .build();
        long parentId;
        for (SubsidyProgram existingSp : existingSubsidyPrograms) {
            if (fstLevelProgramSearchKey.equals(existingSp)) {
                parentId = existingSp.getId();
                break;
            }
        }
        parentId = apkService.createSubsidyProgram(fstLevelProgramSearchKey, codesMap).getId();

    }

    private Map<Long, List<Long>> updateFinancingSources(
        HashSet<FinancingSource> updatedFsList,
        Map<FinancingSource, Set<CashPlanLimit>> excelEntitiesMap,
        Map<Dictionary, Map<String, Long>> codesMap
    ) {
        Map<Long, List<Long>> updatedFsIds = new HashMap<>();
        for (FinancingSource updatedFs : updatedFsList) {
            Long id = updatedFs.getId();
            log.info("Analyzing Financing_Source [{}] ...", id);
            Set<CashPlanLimit> existingCplList = apkService.findCashPlanLimits(
                requestDtoToFindCashPlanLimitsByIds(updatedFs.getCashPlanLimitIds()));
            log.debug("Existing Cash_Plan_Limits count: [{}]", existingCplList.size());
            List<CashPlanLimit> excelCplList = excelEntitiesMap.get(updatedFs);
            log.debug("From excel Cash_Plan_Limits count: [{}]", existingCplList.size());
            excelCplList.removeAll(existingCplList);
            List<Long> savedCplIds = createNewCpls(excelCplList, codesMap);
            if (!savedCplIds.isEmpty()) {
                log.info("Updating Financing_Source [{}] by new Cash_Plan_Limits with ids: {}", id,
                    savedCplIds);
                updatedFs.getCashPlanLimitIds().addAll(savedCplIds);
                apkService.updateFinancingSource(updatedFs);
                updatedFsIds.put(id, savedCplIds);
            } else {
                log.info("New Cash_Plan_Limits not found");
            }
        }
        return updatedFsIds;
    }

    private List<Long> createNewCpls(List<CashPlanLimit> excelCplList,
        Map<Dictionary, Map<String, Long>> codesMap) {
        List<Long> savedCplIds = new ArrayList<>();
        for (CashPlanLimit cpl : excelCplList) {
            log.info("Creating new Cash_Plan_Limit ...");
            CashPlanLimit createdCpl = apkService.createCashPlanLimit(cpl, codesMap);
            log.info("New Cash_Plan_Limit created with id [{}]", createdCpl.getId());
            savedCplIds.add(createdCpl.getId());
        }
        return savedCplIds;
    }


    public CreateBudgetItemsResponseDto createBudgetItems(
        List<? extends DescriptedBudgetItemData> rows) {
        List<? extends DescriptedBudgetItemData> rowsToProcess = getNotExistedCplRows(rows);
        if (rowsToProcess.isEmpty()) {
            log.info("No one unique BudgetItems in excel found to be saved");
            return new CreateBudgetItemsResponseDto(emptyMap());
        }
        log.info("[{}] BudgetItems from excel left as unique to be processed",
            rowsToProcess.size());
        Map<Dictionary, Map<String, Long>> codesMap = apkService.getDictionariesCodesMap(
            KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE);
        Set<SubsidyProgram> existingSpList = subsidyProgramService.getAllSubsidyProgram();
        log.info("Found [{}] Subsidy Programs in DB", existingSpList.size());

        Set<CashPlanLimit> savedCplList = new LinkedHashSet<>();
        Set<SubsidyProgram> savedSpList = new LinkedHashSet<>();
        Set<FinancingSource> savedFsList = new LinkedHashSet<>();

        log.info("Start processing BudgetItems to save containing objects");
        for (DescriptedBudgetItemData row : rowsToProcess) {
            CashPlanLimit savedCpl = rowProcessor.saveCashPlanLimit(row, codesMap);
            savedCplList.add(savedCpl);
            SubsidyProgram thirdLevelSp = rowProcessor.getOrCreateSubsidyProgram(row,
                existingSpList, codesMap);
            savedSpList.add(thirdLevelSp);
            FinancingSource savedFs = rowProcessor.saveFinancingSource(row, savedCpl, thirdLevelSp,
                codesMap);
            savedFsList.add(savedFs);
        }
        return buildResponse(savedCplList, savedSpList, savedFsList);
    }

    private <T extends CashPlanLimitData> List<T> getNotExistedCplRows(List<T> rows) {
        Set<CashPlanLimit> currentYearExistingLimits = apkService.findCashPlanLimits(
            getCplCodesOnlyByCurrentYearRequestDto());
        log.info("Found [{}] CashPlanLimits for [{}] year in DB", currentYearExistingLimits.size(),
            LocalDateTime.now().getYear());
        return rows.stream()
            .filter(row -> !currentYearExistingLimits.contains(cplMapper.toEntity(row)))
            .toList();
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
