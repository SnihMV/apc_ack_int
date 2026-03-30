package su.petrosoft.apk_ack_integration.service;

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
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
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.TEMPLATE_TITLE;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.recalculateValues;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.requestDtoToGettingCplEqualsFieldsByCurrentYear;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.requestDtoToGettingExpenseFieldsById;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.FS_TITLE;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.requestDtoForGettingAllFsByCurrentYear;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.SP_TITLE;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.requestDtoForUpdatingParentId;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.requestDtoToFindAllSubsidyPrograms;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.mapper.SubsidyProgramMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.FinancingSource;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.data.CashPlanLimitData;
import su.petrosoft.apk_ack_integration.model.data.DescriptedBudgetItemData;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateBudgetItemsResponseDto;
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
    private final SubsidyProgramService subsidyProgramService;

    public Set<SubsidyProgram> createSubsidyProgramsTree(
        List<DescriptedBudgetItemData> rowDtoList) {

        Map<Dictionary, Map<String, Long>> codesMap = apkService.getDictionariesCodesMap(
            Set.of(KCSR, DOPKR));
        Set<SubsidyProgram> existingSndLvlSubsidyPrograms = subsidyProgramService.getAllSecondLevelSpFromDb(
            codesMap);
        log.info("Found [{}] Subsidy Programs in DB with level 2",
            existingSndLvlSubsidyPrograms.size());

        List<DescriptedBudgetItemData> unknownSpRows = rowDtoList.stream()
            .filter(row -> !existingSndLvlSubsidyPrograms.contains(
                spMapper.toSecondLevelSP(row, codesMap)))
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

    public Map<Operation, Map<String, Set<Long>>> upsertBudgetItems(
        List<DescriptedBudgetItemData> rows) {
        Map<Operation, Map<String, Set<Long>>> statistics = new HashMap<>();
        Map<Dictionary, Map<String, Long>> codesMap = findOrCreateDictionariesFromFile(rows,
            statistics);

        Map<CashPlanLimit, Long> updatedCplMap = upsertCashPlanLimits(rows, codesMap, statistics);

        Map<SubsidyProgram, Long> updatedSpMap = upsertSubsidyPrograms(rows, codesMap, statistics);

        upsertFinancingSources(updatedCplMap, updatedSpMap, codesMap, statistics);

        return statistics;
    }

    private void upsertFinancingSources(
        Map<CashPlanLimit, Long> existingCplMap,
        Map<SubsidyProgram, Long> existingSpMap,
        Map<Dictionary, Map<String, Long>> codesMap,
        Map<Operation, Map<String, Set<Long>>> statistics
    ) {
        Map<FinancingSource, Set<Long>> fsToCplMap = existingCplMap.entrySet()
            .stream()
            .collect(groupingBy(
                entry -> extractFsFromCpl(entry.getKey()),
                mapping(
                    Entry::getValue,
                    toSet())));

        Set<FinancingSource> existingFsList = apkService.findFinancingSources(
            requestDtoForGettingAllFsByCurrentYear());

        Map<FinancingSource, Set<Long>> toUpdateFsMap = existingFsList.stream()
            .filter(fsToCplMap::containsKey)
            .collect(toMap(
                Function.identity(),
                fsToCplMap::get
            ));

        Set<Long> updatedFsIds = updateAllFs(toUpdateFsMap, existingSpMap);
        if (!updatedFsIds.isEmpty()) {
            statistics.computeIfAbsent(UPDATED, k -> new HashMap<>()).put(FS_TITLE, updatedFsIds);
        }

        Map<FinancingSource, Set<Long>> toCreateFsMap = fsToCplMap.entrySet().stream()
            .filter(entry -> !existingFsList.contains(entry.getKey()))
            .collect(toMap(
                Entry::getKey,
                Entry::getValue
            ));

        createAllFs(toCreateFsMap, existingSpMap, codesMap, statistics);
    }

    private Map<SubsidyProgram, Long> upsertSubsidyPrograms(
        List<DescriptedBudgetItemData> rows,
        Map<Dictionary, Map<String, Long>> codesMap,
        Map<Operation, Map<String, Set<Long>>> statistics
    ) {
        Map<SubsidyProgram, Long> existingSpMap = apkService.findSubsidyPrograms(
                requestDtoToFindAllSubsidyPrograms()).stream()
            .collect(toMap(
                Function.identity(),
                SubsidyProgram::getId
            ));

        Map<SubsidyProgram, SubsidyProgram> incomingSpMap = rows.stream()
            .map(row -> Map.entry(
                spMapper.toSecondLevelSP(row, codesMap),
                spMapper.toFirstLevelSP(row, codesMap)
            ))
            .collect(toMap(
                Entry::getKey,
                Entry::getValue,
                (v1, v2) -> v1
            ));

        Map<SubsidyProgram, Long> existingScdLvlSpMap = existingSpMap.entrySet().stream()
            .filter(entry -> entry.getKey().getLevel() == 2)
            .collect(toMap(
                Entry::getKey,
                Entry::getValue
            ));

        Set<SubsidyProgram> toCreateScdLvlSpSet = incomingSpMap.keySet().stream()
            .filter(sp -> !existingScdLvlSpMap.containsKey(sp))
            .collect(toSet());

        Set<SubsidyProgram> toUpdateScdLvlSpSet = existingScdLvlSpMap.keySet().stream()
            .filter(incomingSpMap::containsKey)
            .collect(toSet());

        Set<Long> createdSp = createAllSp(toCreateScdLvlSpSet, existingSpMap, incomingSpMap);
        if (!createdSp.isEmpty()) {
            statistics.computeIfAbsent(CREATED, k -> new HashMap<>()).put(SP_TITLE, createdSp);
        }
        updateAllSp(toUpdateScdLvlSpSet, existingSpMap, incomingSpMap, statistics);
        return existingSpMap;
    }

    private void updateAllSp(
        Set<SubsidyProgram> toUpdateScdLvlSpSet,
        Map<SubsidyProgram, Long> existingSpMap,
        Map<SubsidyProgram, SubsidyProgram> incomingSpMap,
        Map<Operation, Map<String, Set<Long>>> statistics
    ) {
        Set<Long> createdSp = new HashSet<>();
        Set<Long> updatedSp = new HashSet<>();
        for (SubsidyProgram sp : toUpdateScdLvlSpSet) {
            if (sp.getParentId() == null) {
                Optional<Long> parentId = assignParentId(sp, existingSpMap, incomingSpMap);
                if (sp.getParentId() == null) {
                    statistics.computeIfAbsent(Operation.FAILED_UPDATE, k -> new HashMap<>())
                        .computeIfAbsent(SP_TITLE, k -> new HashSet<>()).add(sp.getId());
                    continue;
                }
                parentId.ifPresent(createdSp::add);
                long updatedSpId = apkService.updateSubsidyProgram(requestDtoForUpdatingParentId(sp));
                updatedSp.add(updatedSpId);
            }
        }
        if (!createdSp.isEmpty()) {
            statistics.computeIfAbsent(CREATED, k -> new HashMap<>()).put(SP_TITLE, createdSp);
        }
        if (!updatedSp.isEmpty()) {
            statistics.computeIfAbsent(UPDATED, k -> new HashMap<>()).put(SP_TITLE, updatedSp);
        }
    }

    private Set<Long> createAllSp(
        Set<SubsidyProgram> toCreateScdLvlSpSet,
        Map<SubsidyProgram, Long> existingSpMap,
        Map<SubsidyProgram, SubsidyProgram> incomingSpMap
    ) {
        Set<Long> createdSpIds = new HashSet<>();
        for (SubsidyProgram sp : toCreateScdLvlSpSet) {
            Optional<Long> parentId = assignParentId(sp, existingSpMap, incomingSpMap);
            parentId.ifPresent(createdSpIds::add);
            SubsidyProgram createdSp = apkService.createSubsidyProgram(sp);
            Long createdSpId = createdSp.getId();
            existingSpMap.put(createdSp, createdSpId);
            createdSpIds.add(createdSpId);
        }
        return createdSpIds;
    }

    private Optional<Long> assignParentId(
        SubsidyProgram scdLvlSp,
        Map<SubsidyProgram, Long> existingSpMap,
        Map<SubsidyProgram, SubsidyProgram> incomingSpMap
    ) {
        SubsidyProgram fstLvlSpSearchKey = SubsidyProgram.builder()
            .level(1L)
            .kcsr(scdLvlSp.getKcsr())
            .build();
        Long foundParentId = existingSpMap.get(fstLvlSpSearchKey);
        if (foundParentId != null) {
            scdLvlSp.setParentId(foundParentId);
            return Optional.empty();
        }
        SubsidyProgram fstLvlSpToCreate = incomingSpMap.get(scdLvlSp);
        if (fstLvlSpToCreate == null) {
            return Optional.empty();
        }
        SubsidyProgram createdFstLvlSp = apkService.createSubsidyProgram(fstLvlSpToCreate);
        Long createdFstLvlSpId = createdFstLvlSp.getId();
        existingSpMap.put(createdFstLvlSp, createdFstLvlSpId);
        scdLvlSp.setParentId(createdFstLvlSpId);
        return Optional.of(createdFstLvlSpId);
    }

    private Map<CashPlanLimit, Long> upsertCashPlanLimits(
        List<DescriptedBudgetItemData> rows,
        Map<Dictionary, Map<String, Long>> codesMap,
        Map<Operation, Map<String, Set<Long>>> statistics
    ) {
        Map<CashPlanLimit, Long> existingCplMap = apkService.findCashPlanLimits(
                requestDtoToGettingCplEqualsFieldsByCurrentYear()).stream()
            .collect(toMap(
                Function.identity(),
                CashPlanLimit::getId
            ));

        Set<CashPlanLimit> incomingCplSet = rows.stream()
            .map(row -> cplMapper.toEntity(row, codesMap))
            .collect(toSet());

        Set<CashPlanLimit> toUpdateCplList = incomingCplSet.stream()
            .filter(existingCplMap::containsKey)
            .peek(cpl -> cpl.setId(existingCplMap.get(cpl)))
            .collect(toSet());

        Set<Long> updatedIds = updateAllCpl(toUpdateCplList);
        if (!updatedIds.isEmpty()) {
            statistics.computeIfAbsent(UPDATED, k -> new HashMap<>()).put(TEMPLATE_TITLE, updatedIds);
        }

        Set<CashPlanLimit> toCreateCplList = new HashSet<>(incomingCplSet);
        toCreateCplList.removeAll(existingCplMap.keySet());

        Map<CashPlanLimit, Long> createdCplMap = toCreateCplList.stream()
            .map(apkService::createCashPlanLimit)
            .collect(toMap(
                Function.identity(),
                CashPlanLimit::getId
            ));
        if (!createdCplMap.isEmpty()) {
            statistics.computeIfAbsent(CREATED, k -> new HashMap<>())
                .put(TEMPLATE_TITLE, new HashSet<>(createdCplMap.values()));
            existingCplMap.putAll(createdCplMap);
        }
        return existingCplMap;
    }

    private Set<Long> createAllFs(
        Map<FinancingSource, Set<Long>> toCreateFsMap,
        Map<SubsidyProgram, Long> existingSpMap,
        Map<Dictionary, Map<String, Long>> codesMap,
        Map<Operation, Map<String, Set<Long>>> statistics
    ) {
        Set<Long> createdFsIds = new HashSet<>();
        for (Entry<FinancingSource, Set<Long>> entry : toCreateFsMap.entrySet()) {
            FinancingSource fs = entry.getKey();
            assignSubsidyProgramId(fs, existingSpMap);
            fs.setCashPlanLimitIds(entry.getValue());
            FinancingSource createdFs = apkService.createFinancingSource(fs, codesMap);
            createdFsIds.add(createdFs.getId());
        }
        if (!createdFsIds.isEmpty()) {
            statistics.computeIfAbsent(CREATED, k -> new HashMap<>()).put(FS_TITLE, createdFsIds);
        }
        return createdFsIds;
    }

    private void assignSubsidyProgramId(
        FinancingSource fs,
        Map<SubsidyProgram, Long> existingSpMap
    ) {
        SubsidyProgram spSearchKey = SubsidyProgram.builder()
            .kcsr(fs.getKcsr())
            .dopKr(fs.getDopKr())
            .build();
        Long spId = existingSpMap.get(spSearchKey);
        fs.setSubsidyProgramId(spId);
        if (spId == null) {
            throw new IllegalStateException(
                "Something went wrong. Could not find SP with kcsr = [%s] and dopKr = [%s]".
                    formatted(spSearchKey.getKcsr(), spSearchKey.getDopKr()));
        }
    }

    private Set<Long> updateAllFs(
        Map<FinancingSource, Set<Long>> toUpdateFsMap,
        Map<SubsidyProgram, Long> existingSpMap
    ) {
        Set<Long> updatedFsIds = new HashSet<>();
        for (Entry<FinancingSource, Set<Long>> entry : toUpdateFsMap.entrySet()) {
            FinancingSource fs = entry.getKey();
            boolean needToUpdate = false;
            Set<Long> actualCplSet = entry.getValue();
            if (!actualCplSet.equals(fs.getCashPlanLimitIds())) {
                fs.setCashPlanLimitIds(actualCplSet);
                needToUpdate = true;
            }
            Long existingSpId = fs.getSubsidyProgramId();
            Long actualSpId = existingSpMap.get(extractScdLvlSpFromFs(fs));
            if (existingSpId == null
//                || !existingSpId.equals(actualSpId)
            ) {
                fs.setSubsidyProgramId(actualSpId);
                needToUpdate = true;
            }
            if (needToUpdate) {
                updatedFsIds.add(apkService.updateFinancingSource(fs));
            }
        }
        return updatedFsIds;
    }

    private FinancingSource extractFsFromCpl(CashPlanLimit cpl) {
        return FinancingSource.builder()
            .year(cpl.getYear())
            .kvsr(cpl.getKvsr())
            .kfsr(cpl.getKfsr())
            .kcsr(cpl.getKcsr())
            .kvr(cpl.getKvr())
            .kosgu(cpl.getKosgu())
            .dopFk(cpl.getDopFk())
            .dopEk(cpl.getDopEk())
            .dopKr(cpl.getDopKr())
            .purpose(cpl.getPurpose())
            .build();
    }

    private SubsidyProgram extractScdLvlSpFromFs(FinancingSource fs) {
        return SubsidyProgram.builder()
            .level(2L)
            .kcsr(fs.getKcsr())
            .dopKr(fs.getDopKr())
            .build();
    }

    private Set<Long> updateAllCpl(Collection<CashPlanLimit> toUpdateCplList) {
        Set<Long> updatedCplIds = new HashSet<>();
        for (CashPlanLimit newCpl : toUpdateCplList) {
            CashPlanLimit oldCpl = apkService.findCashPlanLimits(
                requestDtoToGettingExpenseFieldsById(newCpl.getId())).iterator().next();
            if (hasDifference(oldCpl, newCpl)) {
                CashPlanLimit recalculatedCpl = recalculateValues(oldCpl, newCpl);
                updatedCplIds.add(apkService.updateCashPlanLimit(recalculatedCpl));
            }
        }
        return updatedCplIds;
    }

    private boolean hasDifference(CashPlanLimit oldCpl, CashPlanLimit newCpl) {

        return oldCpl.getTotalLimit().compareTo(newCpl.getTotalLimit()) != 0
               || oldCpl.getFederalBudget().compareTo(newCpl.getFederalBudget()) != 0
               || oldCpl.getRegionalBudget().compareTo(newCpl.getRegionalBudget()) != 0
               || oldCpl.getJanLimit().compareTo(newCpl.getJanLimit()) != 0
               || oldCpl.getFebLimit().compareTo(newCpl.getFebLimit()) != 0
               || oldCpl.getMarLimit().compareTo(newCpl.getMarLimit()) != 0
               || oldCpl.getAprLimit().compareTo(newCpl.getAprLimit()) != 0
               || oldCpl.getMayLimit().compareTo(newCpl.getMayLimit()) != 0
               || oldCpl.getJunLimit().compareTo(newCpl.getJunLimit()) != 0
               || oldCpl.getJulLimit().compareTo(newCpl.getJulLimit()) != 0
               || oldCpl.getAugLimit().compareTo(newCpl.getAugLimit()) != 0
               || oldCpl.getSepLimit().compareTo(newCpl.getSepLimit()) != 0
               || oldCpl.getOctLimit().compareTo(newCpl.getOctLimit()) != 0
               || oldCpl.getNovLimit().compareTo(newCpl.getNovLimit()) != 0
               || oldCpl.getDecLimit().compareTo(newCpl.getDecLimit()) != 0;
    }

    private Map<Dictionary, Map<String, Long>> findOrCreateDictionariesFromFile(
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

        Map<Dictionary, Map<String, Long>> resultMap = new HashMap<>();
        for (Entry<Dictionary, Map<String, String>> entry : dictionariesFromFile.entrySet()) {
            Dictionary dictionary = entry.getKey();
            Map<String, String> dictionaryData = entry.getValue();

            Map<String, Long> foundCodes = dictionaryService.findByCodes(dictionary,
                dictionaryData.keySet());
            resultMap.put(dictionary, foundCodes);

            Set<String> codesToCreate = new HashSet<>(dictionaryData.keySet());
            codesToCreate.removeAll(foundCodes.keySet());

            Map<String, Long> createdCodes = codesToCreate.stream()
                .map(code -> Map.entry(
                    dictionaryService.createNewDictionaryInstance(dictionary, code,
                        dictionaryData.get(code)),
                    code))
                .collect(toMap(
                    Entry::getValue,
                    Entry::getKey
                ));
            if (!createdCodes.isEmpty()) {
                statistics.computeIfAbsent(CREATED, k -> new HashMap<>())
                    .put(dictionary.name(), new HashSet<>(createdCodes.values()));
                resultMap.computeIfAbsent(dictionary, k -> new HashMap<>()).putAll(createdCodes);
            }
        }
        return resultMap;
    }

    public CreateBudgetItemsResponseDto createNewBudgetItems(
        List<? extends DescriptedBudgetItemData> rows) {
        Map<Dictionary, Map<String, Long>> codesMap = apkService.getDictionariesCodesMap(
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
        Map<Dictionary, Map<String, Long>> codesMap) {
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
                    TEMPLATE_TITLE, savedCplList.stream()
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
