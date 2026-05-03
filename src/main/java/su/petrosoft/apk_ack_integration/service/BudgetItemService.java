package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;
import su.petrosoft.apk_ack_integration.exception.ExcelFileException;
import su.petrosoft.apk_ack_integration.exception.InstanceDuplicateException;
import su.petrosoft.apk_ack_integration.exception.StaleVersionException;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.mapper.FinancingSourceMapper;
import su.petrosoft.apk_ack_integration.mapper.SubsidyProgramMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.DictionaryData;
import su.petrosoft.apk_ack_integration.model.FinancingSource;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.data.CashPlanLimitData;
import su.petrosoft.apk_ack_integration.model.data.DescriptedBudgetItemData;
import su.petrosoft.apk_ack_integration.model.data.DictionaryDataContaining;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateBudgetItemsResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;
import su.petrosoft.apk_ack_integration.model.enums.UpsertAction;
import su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil;
import su.petrosoft.apk_ack_integration.util.FinancingSourceUtil;
import su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

import static java.util.Collections.emptyMap;
import static java.util.Map.entry;
import static java.util.Map.of;
import static java.util.stream.Collectors.*;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.*;
import static su.petrosoft.apk_ack_integration.model.enums.UpsertAction.CREATED;
import static su.petrosoft.apk_ack_integration.model.enums.UpsertAction.UPDATED;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.*;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.*;
import static su.petrosoft.apk_ack_integration.util.FinancingSourceUtil.*;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.findDuplicates;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class BudgetItemService {

    private final PlicanteInstanceService apkService;
    private final DictionaryService dictionaryService;
    private final UniBudgetRowService rowProcessor;
    private final CashPlanLimitMapper cplMapper;
    private final FinancingSourceMapper fsMapper;
    private final SubsidyProgramMapper spMapper;
    private final SubsidyProgramService subsidyProgramService;
    private final InstanceUpdater instanceUpdater;
    private final PlicanteRestClient plicanteRestClient;
    private final ExcelExtractor excelExtractor;

    public Set<SubsidyProgram> createSubsidyProgramsTree(
            List<DescriptedBudgetItemData> rowDtoList) {

        Map<Dictionary, Map<DictionaryData, Long>> codesMap = dictionaryService.getDataMap(Set.of(KCSR, DOPKR));
        Set<SubsidyProgram> existingSndLvlSubsidyPrograms = subsidyProgramService.getAllSecondLevelSpFromDb(codesMap);
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

    public Map<UpsertAction, Map<Long, Set<Long>>> upsertBudgetItems(List<DescriptedBudgetItemData> rows) {

        Map<UpsertAction, Map<Long, Set<Long>>> statistics = new HashMap<>(of(
                CREATED, new HashMap<>(),
                UPDATED, new HashMap<>()
        ));

        Map<Dictionary, Map<DictionaryData, Long>> codesMap = new HashMap<>();

        fillDictionaryMapByRows(codesMap, rows, statistics);

        Map<CashPlanLimit, CashPlanLimit> updatedCplMap = upsertCashPlanLimits(rows, codesMap, statistics);

        Map<SubsidyProgram, Long> updatedSpMap = upsertSubsidyPrograms(rows, codesMap, statistics);

        upsertFinancingSources(updatedCplMap, updatedSpMap, codesMap, statistics);

        return statistics;
    }

    private void upsertFinancingSources(
            Map<CashPlanLimit, CashPlanLimit> existingCplMap,
            Map<SubsidyProgram, Long> existingSpMap,
            Map<Dictionary, Map<DictionaryData, Long>> codesMap,
            Map<UpsertAction, Map<Long, Set<Long>>> statistics
    ) {
        Map<FinancingSource, Set<Long>> fsToCplIdsMap = existingCplMap.keySet()
                .stream()
                .collect(groupingBy(
                        FinancingSourceUtil::sourceKeyFromLimit,
                        mapping(
                                CashPlanLimit::getId,
                                toSet()
                        )));

        Set<FinancingSource> existingFsList = apkService.findFinancingSources(
                requestDtoToGetSourcesByYear(LocalDate.now().getYear()));

        Map<FinancingSource, Set<Long>> fsMapToUpdate = existingFsList.stream()
                .filter(fsToCplIdsMap::containsKey)
                .collect(toMap(
                        Function.identity(),
                        fsToCplIdsMap::get
                ));

        Set<Long> updatedFsIds = updateAllFs(fsMapToUpdate, existingSpMap);
        if (!updatedFsIds.isEmpty()) {
            statistics.computeIfAbsent(UPDATED, k -> new HashMap<>()).put(FinancingSourceUtil.TEMPLATE_ID, updatedFsIds);
        }

        Map<FinancingSource, Set<Long>> toCreateFsMap = fsToCplIdsMap.entrySet().stream()
                .filter(entry -> !existingFsList.contains(entry.getKey()))
                .collect(toMap(
                        Entry::getKey,
                        Entry::getValue
                ));

        createAllFs(toCreateFsMap, existingSpMap, statistics);
    }

    private Map<SubsidyProgram, Long> upsertSubsidyPrograms(
            List<DescriptedBudgetItemData> rows,
            Map<Dictionary, Map<DictionaryData, Long>> codesMap,
            Map<UpsertAction, Map<Long, Set<Long>>> statistics
    ) {
        Map<SubsidyProgram, Long> existingSpMap = apkService.findSubsidyPrograms(
                        requestDtoToGetAllPrograms()).stream()
                .collect(toMap(
                        Function.identity(),
                        SubsidyProgram::getId
                ));

        Map<SubsidyProgram, SubsidyProgram> incomingSpMap = rows.stream()
                .map(row -> entry(
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
            statistics.computeIfAbsent(CREATED, k -> new HashMap<>()).put(SubsidyProgramUtil.TEMPLATE_ID, createdSp);
        }
        updateAllSp(toUpdateScdLvlSpSet, existingSpMap, incomingSpMap, statistics);
        return existingSpMap;
    }

    private void updateAllSp(
            Set<SubsidyProgram> toUpdateScdLvlSpSet,
            Map<SubsidyProgram, Long> existingSpMap,
            Map<SubsidyProgram, SubsidyProgram> incomingSpMap,
            Map<UpsertAction, Map<Long, Set<Long>>> statistics
    ) {
        Set<Long> createdSp = new HashSet<>();
        Set<Long> updatedSp = new HashSet<>();
        for (SubsidyProgram sp : toUpdateScdLvlSpSet) {
            if (sp.getParentId() == null) {
                Optional<Long> parentId = assignParentId(sp, existingSpMap, incomingSpMap);
                if (sp.getParentId() == null) {
                    statistics.computeIfAbsent(UpsertAction.FAILED_UPDATE, k -> new HashMap<>())
                            .computeIfAbsent(SubsidyProgramUtil.TEMPLATE_ID, k -> new HashSet<>()).add(sp.getId());
                    continue;
                }
                parentId.ifPresent(createdSp::add);
                long updatedSpId = apkService.updateSubsidyProgram(requestDtoToUpdatingProgramByParentId(sp));
                updatedSp.add(updatedSpId);
            }
        }
        if (!createdSp.isEmpty()) {
            statistics.computeIfAbsent(CREATED, k -> new HashMap<>()).put(SubsidyProgramUtil.TEMPLATE_ID, createdSp);
        }
        if (!updatedSp.isEmpty()) {
            statistics.computeIfAbsent(UPDATED, k -> new HashMap<>()).put(SubsidyProgramUtil.TEMPLATE_ID, updatedSp);
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

    private Map<CashPlanLimit, CashPlanLimit> upsertCashPlanLimits(
            List<DescriptedBudgetItemData> rows,
            Map<Dictionary, Map<DictionaryData, Long>> codesMap,
            Map<UpsertAction, Map<Long, Set<Long>>> statistics
    ) {

        Map<CashPlanLimit, CashPlanLimit> existingCplMap = plicanteRestClient.getTableAttributesList(
                        requestDtoToGetLimitsByYear(LocalDate.now().getYear())).stream()
                .map(cplMapper::toEntity)
                .collect(toMap(
                        Function.identity(),
                        Function.identity()
                ));

        Set<CashPlanLimit> incomingCplSet = rows.stream()
                .map(row -> cplMapper.toEntity(row, codesMap))
                .collect(toSet());

        Set<Long> updatedIds = new HashSet<>();
        for (CashPlanLimit incoming : incomingCplSet) {
            CashPlanLimit existing = existingCplMap.get(incoming);
            if (existing == null) {
                continue;
            }
            List<Attribute<?>> attrs = getAttributesToUpdate(existing, incoming);
            if (attrs.isEmpty()) {
                continue;
            }
            updateWithRetry(existing, incoming, attrs).ifPresent(updatedIds::add);
        }
        statistics.get(UPDATED).put(CashPlanLimitUtil.TEMPLATE_ID, updatedIds);

        Set<Long> createdIds = new HashSet<>();
        for (CashPlanLimit incoming : incomingCplSet) {
            if (!existingCplMap.containsKey(incoming)) {
                Long id = plicanteRestClient.createInstance(requestDtoToCreateCpl(incoming)).id();
                incoming.setId(id);
                createdIds.add(id);
                existingCplMap.put(incoming, incoming);
            }
        }
        statistics.get(CREATED).put(CashPlanLimitUtil.TEMPLATE_ID, createdIds);
        return existingCplMap;
    }

    private Optional<Long> updateWithRetry(CashPlanLimit existing, CashPlanLimit incoming, List<Attribute<?>> attrs) {
        try {
            return Optional.of(plicanteRestClient.updateInstance(
                            requestDtoForUpdate(existing.getId(), existing.getVersion(), attrs))
                    .id());
        } catch (StaleVersionException e) {
            return instanceUpdater.updateCpl(incoming, existing.getId());
        }
    }

    private Set<Long> createAllFs(
            Map<FinancingSource, Set<Long>> toCreateFsMap,
            Map<SubsidyProgram, Long> existingSpMap,
            Map<UpsertAction, Map<Long, Set<Long>>> statistics
    ) {
        Set<Long> createdFsIds = new HashSet<>();
        for (Entry<FinancingSource, Set<Long>> entry : toCreateFsMap.entrySet()) {
            FinancingSource fs = entry.getKey();
            assignSubsidyProgramId(fs, existingSpMap);
            fs.setCashPlanLimitIds(entry.getValue());
            FinancingSource createdFs = apkService.createFinancingSource(fs);
            createdFsIds.add(createdFs.getId());
        }
        if (!createdFsIds.isEmpty()) {
            statistics.computeIfAbsent(CREATED, k -> new HashMap<>()).put(FinancingSourceUtil.TEMPLATE_ID, createdFsIds);
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
            Long actualSpId = existingSpMap.get(extractScdLvlProgramKeyFromSource(fs));
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

    private void fillDictionaryMapByRows(
            Map<Dictionary, Map<DictionaryData, Long>> dictionaryMap,
            List<? extends DictionaryDataContaining> rows,
            Map<UpsertAction, Map<Long, Set<Long>>> statistics
    ) {
        Map<Dictionary, Set<DictionaryData>> dictionariesDataFromRows = collectDictionaryData(rows);

        for (Entry<Dictionary, Set<DictionaryData>> entry : dictionariesDataFromRows.entrySet()) {
            Dictionary dictionary = entry.getKey();
            Set<DictionaryData> data = entry.getValue();

            Map<DictionaryData, Long> foundData = findDictionaryData(dictionary, data);

            HashSet<DictionaryData> dataToCreate = new HashSet<>(dictionariesDataFromRows.get(dictionary));
            dataToCreate.removeAll(foundData.keySet());

            Map<DictionaryData, Long> createdData = createData(dictionary, dataToCreate);
            foundData.putAll(createdData);

            dictionaryMap.computeIfAbsent(dictionary, k -> new HashMap<>()).putAll(foundData);

            if (!createdData.isEmpty()) {
                statistics.get(CREATED)
                        .computeIfAbsent(dictionary.getTemplateId(), k -> new HashSet<>())
                        .addAll(createdData.values());
            }
        }
    }

    private Map<DictionaryData, Long> findDictionaryData(Dictionary dictionary, Set<DictionaryData> inputData) {
        Set<String> codes = inputData.stream()
                .map(DictionaryData::getCode)
                .collect(toSet());
        Map<DictionaryData, Long> foundData = dictionaryService.findByCodes(dictionary, codes);
//        if (!foundData.isEmpty()) {
//            checkSafety(dictionary, inputData, foundData);
//        }
        return foundData;
    }

    private static void checkSafety(Dictionary dictionary, Set<DictionaryData> inputData, Map<DictionaryData, Long> foundData) {
        Map<DictionaryData, String> searchingMap = inputData.stream()
                .collect(toMap(
                        Function.identity(),
                        DictionaryData::getDescription
                ));
        for (DictionaryData data : foundData.keySet()) {
            String foundDescription = data.getDescription().trim();
            String inputDescription = searchingMap.get(data);
            if (inputDescription != null && !inputDescription.isBlank()
                    && !foundDescription.equalsIgnoreCase(inputDescription)) {
                throw new ExcelFileException(MISMATCH_CODE_DESCRIPTIONS.formatted(
                        data.getCode(), dictionary.getName(), inputDescription, foundDescription));
            }
        }
    }

    private Map<Dictionary, Set<DictionaryData>> collectDictionaryData(List<? extends DictionaryDataContaining> rows) {
        return rows.stream()
                .flatMap(row -> row.dictionariesData().entrySet().stream())
                .collect(groupingBy(
                        Entry::getKey,
                        mapping(Entry::getValue, toSet())
                ));
    }

    private Map<DictionaryData, Long> createData(Dictionary dictionary, HashSet<DictionaryData> dataToCreate) {
        Map<DictionaryData, Long> createdData = new HashMap<>();
        for (DictionaryData data : dataToCreate) {
            if (data.getDescription() == null || data.getDescription().isBlank()) {
                throw new ExcelFileException(FILE_NO_CODE_DESCRIPTION.formatted(data.getCode(), dictionary));
            }
            long createdId = dictionaryService.createNewDictionaryInstance(dictionary, data);
            createdData.put(data, createdId);
        }
        return createdData;
    }

    public CreateBudgetItemsResponseDto createNewBudgetItems(
            List<? extends DescriptedBudgetItemData> rows) {
        Map<Dictionary, Map<DictionaryData, Long>> codesMap = dictionaryService.getDataMap(
                Set.of(KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE));
        List<? extends DescriptedBudgetItemData> rowsToProcess = getNotExistedCplRows(rows, codesMap);
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

        log.info("Start processing BudgetItems to save containing amountXmlList");
        for (DescriptedBudgetItemData row : rowsToProcess) {
            CashPlanLimit savedCpl = rowProcessor.saveCashPlanLimit(row, codesMap);
            savedCplList.add(savedCpl);
            SubsidyProgram thirdLevelSp = rowProcessor.getOrCreateSubsidyProgram(row, existingSpList, codesMap);
            savedSpList.add(thirdLevelSp);
            FinancingSource savedFs = rowProcessor.saveFinancingSource(row, savedCpl, thirdLevelSp, codesMap);
            savedFsList.add(savedFs);
        }
        return buildResponse(savedCplList, savedSpList, savedFsList);
    }

    private <T extends CashPlanLimitData> List<T> getNotExistedCplRows(List<T> rows,
                                                                       Map<Dictionary, Map<DictionaryData, Long>> codesMap) {
        Set<CashPlanLimit> currentYearExistingLimits = apkService.findCashPlanLimits(
                requestDtoToGetCplIdentAttrsByCurrentYear());
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
                of(
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

    public Map<UpsertAction, Map<Long, Set<Long>>> saveAndUpdateBudgetItems(MultipartFile file) {

        List<DescriptedBudgetItemData> excelRows = excelExtractor.getUniBudget2026ClarifiedRows(file);
        if (excelRows.isEmpty()) {
            throw new ExcelFileException(FILE_IS_EMPTY.formatted(file.getOriginalFilename()));
        }
        BudgetItemContext ctx = initContext();
        rebuildContext(ctx);

        return ctx.statistics;
    }

    public Map<UpsertAction, Map<Long, Set<Long>>> restoreBudgetItemsConsistency() {
        long start = System.currentTimeMillis();
        BudgetItemContext ctx = initContext();
        long initCtx = System.currentTimeMillis();
        System.out.println("=== init ctx time: " + (initCtx - start) + "ms");
        rebuildContext(ctx);
        long rebuildCtx = System.currentTimeMillis();
        System.out.println("=== rebuild ctx time: " + (rebuildCtx - initCtx) + "ms");

        return ctx.statistics;
    }

    private void rebuildContext(BudgetItemContext ctx) {
        Map<FinancingSource, Set<Long>> sourceKeysFromExistingLimits = ctx.existingLimits.keySet().stream()
                .collect(groupingBy(
                        FinancingSourceUtil::sourceKeyFromLimit,
                        mapping(CashPlanLimit::getId, toSet())
                ));
        createMissingSources(sourceKeysFromExistingLimits, ctx);
        relinkSourceToLimits(sourceKeysFromExistingLimits, ctx);

        Map<SubsidyProgram, Set<Long>> programKeysFromExistingSources = ctx.existingSources.keySet().stream()
                .collect(groupingBy(
                        SubsidyProgramUtil::extractScdLvlProgramKeyFromSource,
                        mapping(FinancingSource::getId, toSet())
                ));
        createMissingPrograms(programKeysFromExistingSources, ctx);
        relinkProgramToSource(programKeysFromExistingSources, ctx);
    }

    private void relinkProgramToSource(
            Map<SubsidyProgram, Set<Long>> programsFromExistingSources,
            BudgetItemContext ctx
    ) {
        for (SubsidyProgram sp : ctx.existingPrograms.keySet()) {
            Set<Long> properIds = programsFromExistingSources.get(sp);
            if (properIds == null) {
                continue;
            }
            Set<Long> actualIds = sp.getFinancingSourceIds();
            if (!properIds.equals(actualIds)) {
                plicanteRestClient.updateInstance(requestDtoForUpdateBySources(sp.getId(), sp.getVersion(), properIds));
                addStat(ctx, UPDATED, SubsidyProgramUtil.TEMPLATE_ID, sp.getId());
            }
        }
    }

    private void createMissingPrograms(
            Map<SubsidyProgram, Set<Long>> maxLevelProgramKeys,
            BudgetItemContext ctx
    ) {
        Set<Deque<SubsidyProgram>> programsChain = maxLevelProgramKeys.entrySet().stream()
                .map(entry -> entry.getKey().toBuilder()
                        .financingSourceIds(entry.getValue())
                        .build())
                .map(SubsidyProgramUtil::getHierarchicalChain)
                .collect(toSet());
        Set<SubsidyProgram> uniquePrograms = programsChain.stream()
                .flatMap(Collection::stream)
                .filter(p -> !ctx.existingPrograms.containsKey(p))
                .collect(toSet());
        dictionaryService.completeDictionaryMapForRequesters(uniquePrograms, ctx.dictionaryMap);
        for (Entry<SubsidyProgram, Set<Long>> entry : maxLevelProgramKeys.entrySet()) {
            SubsidyProgram programKey = entry.getKey();
            programKey.setFinancingSourceIds(entry.getValue());
            createHierarchically(programKey, ctx);
        }
    }

    private void createHierarchically(SubsidyProgram programKey, BudgetItemContext ctx) {
        while (true) {
            SubsidyProgram program = ctx.existingPrograms.get(programKey);
            if (program == null) {
                program = createProgram(programKey, ctx);
            }
            if (program.getLevel() == MIN_LEVEL) {
                break;
            }
            programKey = extractParentKey(program);
        }
    }

    private SubsidyProgram createProgram(SubsidyProgram program, BudgetItemContext ctx) {
        program.setTitle(defineTitle(program, ctx.dictionaryMap));
        if (program.getLevel() > MIN_LEVEL) {
            SubsidyProgram searchKey = extractParentKey(program);
            SubsidyProgram parent = ctx.existingPrograms.computeIfAbsent(searchKey,
                    p -> createProgram(p, ctx));
            program.setParentId(parent.getId());
        }
        InstanceDto dto = plicanteRestClient.createInstance(requestDtoToCreateSubsidyProgram(program));
        program.setId(dto.id());
        program.setVersion(dto.version());
        addStat(ctx, CREATED, SubsidyProgramUtil.TEMPLATE_ID, dto.id());
        return program;
    }

    private BudgetItemContext initContext() {
        BudgetItemContext ctx = new BudgetItemContext();

        addCurrentYearLimits(ctx);
        addCurrentYearSources(ctx);
        addAllSubsidyPrograms(ctx);

        return ctx;
    }

    private void createMissingSources(
            Map<FinancingSource, Set<Long>> sourceKeysFromExistingLimits,
            BudgetItemContext ctx
    ) {
        Set<FinancingSource> missingSources = sourceKeysFromExistingLimits.entrySet().stream()
                .filter(entry -> !ctx.existingSources.containsKey(entry.getKey()))
                .map(entry -> entry.getKey().toBuilder()
                        .cashPlanLimitIds(entry.getValue())
                        .build())
                .collect(toSet());
        if (!missingSources.isEmpty()) {
            ctx.dictionaryMap.put(OWNERSHIP_FORM, dictionaryService.getDictionaryData(OWNERSHIP_FORM));
            dictionaryService.completeDictionaryMapForRequesters(missingSources, ctx.dictionaryMap);
        }
        for (FinancingSource source : missingSources) {
            source.setConcatenatedKBK(buildConcatKBK(source, ctx.dictionaryMap));
            source.setOwnershipForm(defineOwnershipForm(source, ctx.dictionaryMap));
            FinancingSource createdSource = apkService.createInstance(
                    requestDtoToCreateFinancingSource(source), fsMapper::toEntity);

            ctx.existingSources.put(createdSource, createdSource);
            addStat(ctx, CREATED, FinancingSourceUtil.TEMPLATE_ID, createdSource.getId());
        }
    }

    private void relinkSourceToLimits(
            Map<FinancingSource, Set<Long>> sourcesFromExistingLimits,
            BudgetItemContext ctx
    ) {
        for (FinancingSource existingSource : ctx.existingSources.keySet()) {

            Set<Long> properIds = sourcesFromExistingLimits.get(existingSource);
            if (properIds == null) {
                continue;
            }
            Set<Long> actualIds = existingSource.getCashPlanLimitIds();

            if (!properIds.equals(actualIds)) {
                plicanteRestClient.updateInstance(requestDtoForUpdateByLimits(existingSource.getId(), existingSource.getVersion(), properIds));
                addStat(ctx, UPDATED, FinancingSourceUtil.TEMPLATE_ID, existingSource.getId());
            }
        }
    }

    private void addCurrentYearLimits(BudgetItemContext ctx) {
        List<CashPlanLimit> foundLimits = apkService.findValidInstances(
                requestDtoToGetLimitsByYear(LocalDate.now().getYear()), cplMapper::toEntity);

        List<List<Long>> duplicates = findDuplicates(foundLimits);
        if (!duplicates.isEmpty()) {
            throw new InstanceDuplicateException(CashPlanLimitUtil.TEMPLATE_ID, duplicates);
        }

        ctx.existingLimits.putAll(foundLimits.stream()
                .collect(toMap(Function.identity(), Function.identity())));
    }

    private void addCurrentYearSources(BudgetItemContext ctx) {
        List<FinancingSource> foundSources = apkService.findValidInstances(
                requestDtoToGetSourcesByYear(LocalDate.now().getYear()), fsMapper::toEntity);

        List<List<Long>> duplicates = findDuplicates(foundSources);
        if (!duplicates.isEmpty()) {
            throw new InstanceDuplicateException(FinancingSourceUtil.TEMPLATE_ID, duplicates);
        }
        ctx.existingSources.putAll(foundSources.stream()
                .collect(toMap(Function.identity(), Function.identity())));
    }

    private void addAllSubsidyPrograms(BudgetItemContext ctx) {
        List<SubsidyProgram> foundPrograms = apkService.findValidInstances(
                requestDtoToGetAllPrograms(), spMapper::toEntity);

        List<List<Long>> duplicates = findDuplicates(foundPrograms);
        if (!duplicates.isEmpty()) {
            throw new InstanceDuplicateException(SubsidyProgramUtil.TEMPLATE_ID, duplicates);
        }

        ctx.existingPrograms.putAll(foundPrograms.stream()
                .collect(toMap(Function.identity(), Function.identity())));
    }

    private void addStat(
            BudgetItemContext ctx,
            UpsertAction action,
            long templateId,
            long instanceId
    ) {
        ctx.statistics.computeIfAbsent(action, k -> new HashMap<>())
                .computeIfAbsent(templateId, k -> new HashSet<>())
                .add(instanceId);
    }

    private record BudgetItemContext(
            Map<CashPlanLimit, CashPlanLimit> existingLimits,
            Map<FinancingSource, FinancingSource> existingSources,
            Map<SubsidyProgram, SubsidyProgram> existingPrograms,
            Map<Dictionary, Map<DictionaryData, Long>> dictionaryMap,
            Map<UpsertAction, Map<Long, Set<Long>>> statistics
    ) {
        private BudgetItemContext() {
            this(
                    new HashMap<>(),
                    new HashMap<>(),
                    new HashMap<>(),
                    new EnumMap<>(Dictionary.class),
                    new EnumMap<>(UpsertAction.class));
        }
    }
}
