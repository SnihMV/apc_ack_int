package su.petrosoft.apk_ack_integration.service;

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPEK;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPFK;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPKR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KCSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KFSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KOSGU;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KVR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KVSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.PURPOSE;
import static su.petrosoft.apk_ack_integration.model.enums.Operation.CREATED;
import static su.petrosoft.apk_ack_integration.model.enums.Operation.UPDATED;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.CPL_TITLE;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.recalculateLimits;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.requestDtoToGettingCplEqualsFieldsByCurrentYear;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.requestDtoToGettingExpenseFieldsById;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.FS_TITLE;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.SP_TITLE;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
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
import su.petrosoft.apk_ack_integration.model.data.DescriptedBudgetItemData;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateBudgetItemsResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.response.CreatingInstancesFromFileResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;
import su.petrosoft.apk_ack_integration.model.enums.Operation;

@Slf4j
@Component
@RequiredArgsConstructor
public class BudgetItemService {

    private final ApkPlicanteService apkService;
    private final DictionaryService dictionaryService;
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
        Map<Dictionary, Map<Long, String>> codesMap = apkService.getDictionariesCodesMap(
            Set.of(KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE));
        List<CashPlanLimitData> uniqueRowsByCpl = getNotExistedCplRows(rows, codesMap);
        if (uniqueRowsByCpl.isEmpty()) {
            return CreatingInstancesFromFileResponseDto.builder()
                .incomingCount(rows.size())
                .build();
        }

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

        Map<Dictionary, Map<Long, String>> codesMap = apkService.getDictionariesCodesMap(
            Set.of(KCSR, DOPKR));
        Set<SubsidyProgram> existingSndLvlSubsidyPrograms = subsidyProgramService.getAllSecondLevelSpFromDb(
            codesMap);
        log.info("Found [{}] Subsidy Programs in DB with level 2",
            existingSndLvlSubsidyPrograms.size());

        List<DescriptedBudgetItemData> unknownSpRows = rowDtoList.stream()
            .filter(row -> !existingSndLvlSubsidyPrograms.contains(spMapper.toSecondLevelSP(row, codesMap)))
            .toList();

        Set<SubsidyProgram> createdSP = new LinkedHashSet<>();

        if (unknownSpRows.isEmpty()) {
            log.info("No one rows with unknown SP found");
            return createdSP;
        }
        log.info("Rows with unknown SP: [{}]", unknownSpRows.size());

//        dictionaryService.updateCodesMap(codesMap, unknownSpRows);
        Set<SubsidyProgram> allSpFromDb = subsidyProgramService.getAllSubsidyProgram(codesMap);
        unknownSpRows.forEach(
            row -> rowProcessor.getOrCreateSubsidyProgram(row, allSpFromDb, codesMap));
        return createdSP;
    }

    public Map<Operation, Map<String, Set<Long>>> upsertBudgetItems(List<DescriptedBudgetItemData> rows) {
        Map<Operation, Map<String, Set<Long>>> statistics = new HashMap<>();
        Map<Dictionary, Map<Long, String>> codesMap = findOrCreateDictionariesFromFile(rows,
            statistics);

        Map<CashPlanLimit, Long> existingCplMap = apkService.findCashPlanLimits(
                requestDtoToGettingCplEqualsFieldsByCurrentYear()).stream()
            .collect(toMap(
                Function.identity(),
                CashPlanLimit::getId
            ));

        Set<CashPlanLimit> incomingCplList = rows.stream()
            .map(row -> cplMapper.toEntity(row, codesMap))
            .collect(toSet());

        Set<CashPlanLimit> toUpdateCplList = incomingCplList.stream()
            .filter(existingCplMap::containsKey)
            .peek(cpl -> cpl.setId(existingCplMap.get(cpl)))
            .collect(toSet());

        Set<Long> updatedIds = updateAllCpl(toUpdateCplList);
        if (!updatedIds.isEmpty()) {
            statistics.computeIfAbsent(UPDATED, k -> new HashMap<>()).put(CPL_TITLE, updatedIds);
        }

        Set<CashPlanLimit> toCreateCplList = new HashSet<>(incomingCplList);
        toCreateCplList.removeAll(existingCplMap.keySet());

        Set<CashPlanLimit> createdCplSet = toCreateCplList.stream()
            .map(apkService::createCashPlanLimit)
            .collect(toSet());
        if (!createdCplSet.isEmpty()) {
            statistics.computeIfAbsent(CREATED, k -> new HashMap<>())
                .put(CPL_TITLE, createdCplSet.stream()
                    .map(CashPlanLimit::getId)
                    .collect(toSet()));
        }

        return statistics;


//        dictionaryService.addNewDictionaryCodes(codesMap, rows);
//        apkService.findCashPlanLimits(requestDtoToGettingCplEqualsFieldsByCurrentYear(), codesMap);
//        return null;
    }

    private Set<CashPlanLimit> upsertCashPlanLimits(List<DescriptedBudgetItemData> rows,
        Map<Dictionary, Map<Long, String>> codesMap,
        Map<Operation, Map<String, Set<Long>>> statistics) {
        Map<CashPlanLimit, Long> existingCplMap = apkService.findCashPlanLimits(
                requestDtoToGettingCplEqualsFieldsByCurrentYear()).stream()
            .collect(toMap(
                Function.identity(),
                CashPlanLimit::getId
            ));

        Set<CashPlanLimit> incomingCplList = rows.stream()
            .map(row -> cplMapper.toEntity(row, codesMap))
            .collect(toSet());

        Set<CashPlanLimit> toUpdateCplList = incomingCplList.stream()
            .filter(existingCplMap::containsKey)
            .peek(cpl -> cpl.setId(existingCplMap.get(cpl)))
            .collect(toSet());

        Set<CashPlanLimit> toCreateCplList = new HashSet<>(incomingCplList);
        toCreateCplList.removeAll(existingCplMap.keySet());

        Set<CashPlanLimit> allCpl = new HashSet<>(existingCplMap.keySet());
        Set<CashPlanLimit> createdCplSet = toCreateCplList.stream()
            .map(apkService::createCashPlanLimit)
            .collect(toSet());
        if (!createdCplSet.isEmpty()) {
            statistics.computeIfAbsent(CREATED, k -> new HashMap<>())
                .put(CPL_TITLE, createdCplSet.stream()
                    .map(CashPlanLimit::getId)
                    .collect(toSet()));
        }
        allCpl.addAll(createdCplSet);

        Set<Long> updatedIds = updateAllCpl(toUpdateCplList);
        if (!updatedIds.isEmpty()) {
            statistics.computeIfAbsent(UPDATED, k -> new HashMap<>()).put(CPL_TITLE, updatedIds);
        }
        return allCpl;
    }

    private Set<Long> updateAllCpl(Collection<CashPlanLimit> toUpdateCplList) {
        Set<Long> createdIds = new HashSet<>();
        for (CashPlanLimit newCpl : toUpdateCplList) {
            CashPlanLimit oldCpl = apkService.findCashPlanLimits(
                requestDtoToGettingExpenseFieldsById(newCpl.getId())).iterator().next();
            CashPlanLimit recalculatedCpl = recalculateLimits(oldCpl, newCpl);
            createdIds.add(apkService.updateCashPlanLimit(recalculatedCpl));
        }
        return createdIds;
    }


    private Map<Dictionary, Map<Long, String>> findOrCreateDictionariesFromFile(
        List<DescriptedBudgetItemData> rows,
        Map<Operation, Map<String, Set<Long>>> statistics
    ) {

        Map<Dictionary, Map<String, String>> dictionariesFromFile = rows.stream()
            .flatMap(row -> row.dictionariesData().entrySet().stream())
            .collect(groupingBy(
                Map.Entry::getKey,
                toMap(
                    entry -> entry.getValue().getKey(),
                    entry -> entry.getValue().getValue(),
                    (v1, v2) -> v1
                )));

        Map<Dictionary, Map<Long, String>> resultMap = new HashMap<>();
        for (Entry<Dictionary, Map<String, String>> entry : dictionariesFromFile.entrySet()) {
            Dictionary dictionary = entry.getKey();
            Map<String, String> dictionaryData = entry.getValue();

            Map<Long, String> foundCodes = dictionaryService.findByCodes(dictionary,
                dictionaryData.keySet());
            resultMap.put(dictionary, foundCodes);

            Set<String> codesToCreate = new HashSet<>(dictionaryData.keySet());
            codesToCreate.removeAll(foundCodes.values());

            Map<Long, String> createdCodes = codesToCreate.stream()
                .map(code -> Map.entry(
                    dictionaryService.createNewDictionaryInstance(dictionary, code,
                        dictionaryData.get(code)),
                    code))
                .collect(toMap(
                    Entry::getKey,
                    Entry::getValue
                ));
            if (!createdCodes.isEmpty()) {
                statistics.computeIfAbsent(CREATED, k -> new HashMap<>())
                    .put(dictionary.name(), createdCodes.keySet());
                resultMap.computeIfAbsent(dictionary, k -> new HashMap<>()).putAll(createdCodes);
            }
        }
        return resultMap;
    }

//    public Map<String, Set<Long>> createBudgetItems(List<DescriptedBudgetItemData> rows) {
//        Map<String, Set<Long>> createdEntities = new HashMap<>();
//        if (rows == null || rows.isEmpty()) {
//            return createdEntities;
//        }
//        Map<FinancingSource, Set<CashPlanLimit>> excelEntitiesMap = rows.stream()
//            .collect(groupingBy(
//                row->fsMapper.toEntity(row, ),
//                mapping(cplMapper::toEntity, toSet())));
//        log.info("Found [{}] Financing_Sources in Excel file", excelEntitiesMap.size());
//
//        Map<Dictionary, Map<Long, String>> codesMap = apkService.getDictionariesCodesMap(
//            Set.of(KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE, OWNERSHIP_FORM));
//        Set<FinancingSource> existingFS = apkService.findFinancingSources(
//            requestDtoForGettingAllFsByCurrentYear(), codesMap);
//
//        Map<Dictionary, Set<Long>> createdDictionaries = dictionaryService.addNewDictionaryCodes(
//            codesMap, rows);
//        createdDictionaries.forEach((key, value) -> createdEntities.put(key.name(), value));
//
//        HashSet<FinancingSource> finSourcesToUpdate = new HashSet<>(existingFS);
//        finSourcesToUpdate.retainAll(excelEntitiesMap.keySet());
//        log.info("Count of Existing Financing_Sources found in file: [{}]",
//            finSourcesToUpdate.size());
//
//        HashMap<FinancingSource, Set<CashPlanLimit>> entitiesToCreate = new HashMap<>(
//            excelEntitiesMap);
//        entitiesToCreate.keySet().removeAll(existingFS);
//        log.info("Count of New Financing_Sources found in file: [{}]", entitiesToCreate.size());
//
//        Set<SubsidyProgram> existingSPs;
//        Set<CashPlanLimit> existingCPLs;
//        if (!finSourcesToUpdate.isEmpty() || !entitiesToCreate.isEmpty()) {
//            existingSPs = apkService.findSubsidyPrograms(requestDtoToFindAllSubsidyPrograms(),
//                codesMap);
//            existingCPLs = apkService.findCashPlanLimits(
//                requestDtoToGettingCplEqualsFieldsByCurrentYear(), codesMap);
//
//            createNewFinancingSources(entitiesToCreate, existingSPs, existingCPLs, codesMap,
//                createdEntities);
//
//            Set<Long> updateFsIds = updateFinancingSources(finSourcesToUpdate, excelEntitiesMap,
//                existingSPs, existingCPLs, codesMap, createdEntities);
//            if (!updateFsIds.isEmpty()) {
//                createdEntities.put("Обновленные источники финансирования", updateFsIds);
//            }
//        }
//
//        return createdEntities;
//    }

    private void createNewFinancingSources(
        HashMap<FinancingSource, Set<CashPlanLimit>> entitiesToCreate,
        Set<SubsidyProgram> existingSP,
        Set<CashPlanLimit> existingCPLs,
        Map<Dictionary, Map<Long, String>> codesMap,
        Map<String, Set<Long>> createdEntities
    ) {
        if (!entitiesToCreate.isEmpty()) {
            for (Entry<FinancingSource, Set<CashPlanLimit>> entry : entitiesToCreate.entrySet()) {
                FinancingSource financingSource = entry.getKey();
                Long createdSpId = obtainSubsidyProgramId(financingSource, existingSP, codesMap,
                    createdEntities);
                Set<Long> createdCplIds = obtainCashPlanLimitIds(entry.getValue(), existingCPLs,
                    codesMap, createdEntities);
                financingSource.setSubsidyProgramId(createdSpId);
                financingSource.setCashPlanLimitIds(createdCplIds);
                FinancingSource createdFs = apkService.createFinancingSource(financingSource,
                    codesMap);
                createdEntities.computeIfAbsent(FS_TITLE, k -> new HashSet<>())
                    .add(createdFs.getId());
            }
        }
    }

    private Set<Long> obtainCashPlanLimitIds(
        Set<CashPlanLimit> associatedCPLs,
        Set<CashPlanLimit> existingCPLs,
        Map<Dictionary, Map<Long, String>> codesMap,
        Map<String, Set<Long>> createdEntities
    ) {
        Set<Long> result = new HashSet<>();
        Set<CashPlanLimit> obtained = existingCPLs.stream()
            .filter(associatedCPLs::contains)
            .collect(toSet());

        associatedCPLs.removeAll(obtained);
        associatedCPLs.forEach(cpl -> {
                CashPlanLimit createdCpl = apkService.createCashPlanLimit(cpl);
                obtained.add(createdCpl);
                createdEntities.computeIfAbsent(CPL_TITLE, k -> new HashSet<>())
                    .add(createdCpl.getId());
            }
        );

        return obtained.stream()
            .map(CashPlanLimit::getId)
            .collect(toSet());

    }

    private Long obtainSubsidyProgramId(
        FinancingSource buildedFinancingSource,
        Set<SubsidyProgram> existingSubsidyPrograms,
        Map<Dictionary, Map<Long, String>> codesMap,
        Map<String, Set<Long>> createdEntities
    ) {
        SubsidyProgram scdLevelProgramDummy = SubsidyProgram.builder()
            .level(2L)
            .kcsr(buildedFinancingSource.getKcsr())
            .dopKr(buildedFinancingSource.getDopKr())
//                .title(dictionaryCodeDescription(codesMap, DOPKR, buildedFinancingSource.getDopKr()))
            .build();

        Long createdSpId = existingSubsidyPrograms.stream()
            .filter(scdLevelProgramDummy::equals)
            .findFirst()
            .map(SubsidyProgram::getId)
            .orElseGet(() -> completeSubsidyProgramsBranch(
                scdLevelProgramDummy, existingSubsidyPrograms, codesMap, createdEntities)
            );
        return createdSpId;
    }

    private long completeSubsidyProgramsBranch(
        SubsidyProgram scdLevelSubsidyProgram,
        Set<SubsidyProgram> existingSubsidyPrograms,
        Map<Dictionary, Map<Long, String>> codesMap,
        Map<String, Set<Long>> createdEntities
    ) {
        SubsidyProgram fstLevelProgramDummy = SubsidyProgram.builder()
            .level(1L)
            .kcsr(scdLevelSubsidyProgram.getKcsr())
//                .title(dictionaryCodeDescription(codesMap, KCSR, scdLevelSubsidyProgram.getKcsr()))
            .build();
        Long parentId = existingSubsidyPrograms.stream()
            .filter(fstLevelProgramDummy::equals)
            .findFirst()
            .map(SubsidyProgram::getId)
            .orElseGet(() -> createSubsidyProgram(
                fstLevelProgramDummy, existingSubsidyPrograms, codesMap, createdEntities)
            );
        scdLevelSubsidyProgram.setParentId(parentId);
        return createSubsidyProgram(scdLevelSubsidyProgram, existingSubsidyPrograms, codesMap,
            createdEntities);
    }

    private Long createSubsidyProgram(
        SubsidyProgram fstLevelProgramDummy,
        Set<SubsidyProgram> existingSubsidyPrograms,
        Map<Dictionary, Map<Long, String>> codesMap,
        Map<String, Set<Long>> createdEntities
    ) {
        SubsidyProgram createdSp = apkService.createSubsidyProgram(fstLevelProgramDummy, codesMap);
        existingSubsidyPrograms.add(createdSp);
        createdEntities.computeIfAbsent(SP_TITLE, k -> new HashSet<>()).add(createdSp.getId());
        return createdSp.getId();
    }

    private Set<Long> updateFinancingSources(
        HashSet<FinancingSource> updatedFsList,
        Map<FinancingSource, Set<CashPlanLimit>> excelEntitiesMap,
        Set<SubsidyProgram> existingSP,
        Set<CashPlanLimit> existingCPLs,
        Map<Dictionary, Map<Long, String>> codesMap,
        Map<String, Set<Long>> createdEntities
    ) {
        Set<Long> updatedFsIds = new HashSet<>();
        for (FinancingSource updatedFs : updatedFsList) {
            Long id = updatedFs.getId();
            boolean needUpdate = false;
            log.info("Analyzing Financing_Source [{}] to update ...", id);

            if (updatedFs.getSubsidyProgramId() == null) {
                Long createdSpId = obtainSubsidyProgramId(updatedFs, existingSP, codesMap,
                    createdEntities);
                updatedFs.setSubsidyProgramId(createdSpId);
                needUpdate = true;
            }

            Set<CashPlanLimit> updatedFsExistingCplList = existingCPLs.stream()
                .filter(cpl -> updatedFs.getCashPlanLimitIds().contains(cpl.getId()))
                .collect(toSet());
            log.info("Existing Cash_Plan_Limits count: [{}]", updatedFsExistingCplList.size());

            Set<CashPlanLimit> updatedFsExcelCplList = excelEntitiesMap.get(updatedFs);
            log.info("From excel Cash_Plan_Limits count: [{}]", updatedFsExcelCplList.size());

            updatedFsExcelCplList.removeAll(updatedFsExistingCplList);
            if (!updatedFsExcelCplList.isEmpty()) {
                Set<Long> savedCplIds = createNewCpls(updatedFsExcelCplList, codesMap,
                    createdEntities);
                updatedFs.getCashPlanLimitIds().addAll(savedCplIds);
                needUpdate = true;
            }
            if (needUpdate) {
                apkService.updateFinancingSource(updatedFs);
                updatedFsIds.add(id);
            } else {
                log.info("Financing_Source [{}] is up to date", id);
            }
        }
        return updatedFsIds;
    }

    private Set<Long> createNewCpls(
        Set<CashPlanLimit> excelCplList,
        Map<Dictionary, Map<Long, String>> codesMap,
        Map<String, Set<Long>> createdEntities
    ) {
        Set<Long> savedCplIds = new HashSet<>();
        for (CashPlanLimit cpl : excelCplList) {
            CashPlanLimit createdCpl = apkService.createCashPlanLimit(cpl);
            savedCplIds.add(createdCpl.getId());
        }
        if (!savedCplIds.isEmpty()) {
            createdEntities.computeIfAbsent(CPL_TITLE, k -> new HashSet<>()).addAll(savedCplIds);
        }
        return savedCplIds;
    }


    public CreateBudgetItemsResponseDto createNewBudgetItems(
        List<? extends DescriptedBudgetItemData> rows) {
        Map<Dictionary, Map<Long, String>> codesMap = apkService.getDictionariesCodesMap(
            Set.of(KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE));
        List<? extends DescriptedBudgetItemData> rowsToProcess = getNotExistedCplRows(rows,
            codesMap);
        if (rowsToProcess.isEmpty()) {
            log.info("No one unique BudgetItems in excel found to be saved");
            return new CreateBudgetItemsResponseDto(emptyMap());
        }
        log.info("[{}] BudgetItems from excel left as unique to be processed",
            rowsToProcess.size());

        Set<SubsidyProgram> existingSpList = subsidyProgramService.getAllSubsidyProgram(codesMap);
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

    private <T extends CashPlanLimitData> List<T> getNotExistedCplRows(List<T> rows,
        Map<Dictionary, Map<Long, String>> codesMap) {
        Set<CashPlanLimit> currentYearExistingLimits = apkService.findCashPlanLimits(
            requestDtoToGettingCplEqualsFieldsByCurrentYear());
        log.info("Found [{}] CashPlanLimits for [{}] year in DB", currentYearExistingLimits.size(),
            LocalDateTime.now().getYear());
        return rows.stream()
            .filter(row -> !currentYearExistingLimits.contains(cplMapper.toEntity(row, codesMap)))
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
