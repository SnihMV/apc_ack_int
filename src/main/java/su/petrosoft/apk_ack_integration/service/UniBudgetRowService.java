package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.mapper.FinancingSourceMapper;
import su.petrosoft.apk_ack_integration.mapper.SubsidyProgramMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.FinancingSource;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.data.CashPlanLimitData;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;
import su.petrosoft.apk_ack_integration.model.data.DescriptedBudgetItemData;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class UniBudgetRowService {

    private final ApkPlicanteService apkService;
    private final CashPlanLimitMapper cplMapper;
    private final SubsidyProgramMapper spMapper;
    private final FinancingSourceMapper fsMapper;
    private final ExcelRowMapper excelRowMapper;

    public CashPlanLimit saveCashPlanLimit(
            CashPlanLimitData valueObject,
            Map<Dictionary, Map<String, Long>> codesMap
    ) {
        CashPlanLimit cplFromRow = cplMapper.toEntity(valueObject);
        return apkService.createCashPlanLimit(cplFromRow, codesMap);
    }

    public SubsidyProgram getOrCreateSubsidyProgram(
            DescriptedBudgetItemData row,
            Set<SubsidyProgram> existingSP,
            Map<Dictionary, Map<String, Long>> codesMap) {

        SubsidyProgram fstLevelSp = buildFirstLevelSP(row, existingSP, codesMap);
        SubsidyProgram scdLevelSp = buildSecondLevelSP(row, existingSP, codesMap, fstLevelSp);
//        SubsidyProgram trdLevelSp = buildThirdLevelSP(row, existingSP, codesMap, scdLevelSp);
        log.debug("Subsidy Programs count: {}", existingSP.size());
        return scdLevelSp;
    }

    public FinancingSource buildFinancingSource(
            DescriptedBudgetItemData row,
            Set<CashPlanLimit> existingCashPlanLimits,
            Set<SubsidyProgram> allExistingSndLvlSP,
            Map<Dictionary, Map<String, Long>> codesMap) {
        FinancingSource financingSource = fsMapper.toEntity(row);
        existingCashPlanLimits.stream()
                .filter(cpl -> cpl.equals(cplMapper.toEntity(row)))
                .findFirst()
                .map(CashPlanLimit::getId)
                .ifPresentOrElse(financingSource::setCashPlanLimitId,
                        () -> {
                            CashPlanLimit savedCpl = saveCashPlanLimit(row, codesMap);
                            financingSource.setCashPlanLimitId(savedCpl.getId());
                        });
        allExistingSndLvlSP.stream()
                .filter(sp -> sp.equals(spMapper.toSecondLevelSP(row)))
                .findFirst()
                .map(SubsidyProgram::getId)
                .ifPresentOrElse(financingSource::setSubsidyProgramId,
                        () -> {
                            SubsidyProgram sp = getOrCreateSubsidyProgram(row, allExistingSndLvlSP,
                                    codesMap);
                            financingSource.setSubsidyProgramId(sp.getId());
                        });
        return financingSource;
    }

    public List<CashPlanLimit> getLimitsFromExcel(List<DescriptedBudgetItemData> rows) {
        List<CashPlanLimit> limitsFromExcel = rows.stream()
                .map(cplMapper::toEntity)
                .collect(toList());
        log.info("Limits from excel file count: [{}]", limitsFromExcel.size());
        return limitsFromExcel;
    }

    private SubsidyProgram buildFirstLevelSP(
            DescriptedBudgetItemData row,
            Set<SubsidyProgram> existingSp,
            Map<Dictionary, Map<String, Long>> codesMap
    ) {
        SubsidyProgram fstLvlSp = spMapper.toFirstLevelSP(row);
        Long id = obtainSubsidyProgramId(fstLvlSp, existingSp, codesMap);
        fstLvlSp.setId(id);
        log.debug("First level Subsidy Program from excel row: [{}]", fstLvlSp);
        return fstLvlSp;
    }

    private SubsidyProgram buildSecondLevelSP(
            DescriptedBudgetItemData row,
            Set<SubsidyProgram> existingSP,
            Map<Dictionary, Map<String, Long>> codesMap,
            SubsidyProgram fstLevelSp) {

        SubsidyProgram scdLvlSP = spMapper.toSecondLevelSP(row);
        scdLvlSP.setParentId(fstLevelSp.getId());
        Long id = obtainSubsidyProgramId(scdLvlSP, existingSP, codesMap);
        scdLvlSP.setId(id);
        log.debug("Second level Subsidy Program from excel row: [{}]", scdLvlSP);
        return scdLvlSP;
    }

//    private SubsidyProgram buildThirdLevelSP(
//            DescriptedBudgetItemData dto,
//            Set<SubsidyProgram> existingSP,
//            Map<Dictionary, Map<String, Long>> codesMap,
//            SubsidyProgram scdLevelSp) {
//
//        SubsidyProgram trdLvlSP = spMapper.toThirdLevelSP(dto);
//        trdLvlSP.setParentId(scdLevelSp.getId());
//        Long id = obtainSubsidyProgramId(trdLvlSP, existingSP, codesMap);
//        trdLvlSP.setId(id);
//        log.debug("Third level Subsidy Program from excel row: [{}]", trdLvlSP);
//        return trdLvlSP;
//    }

    private Long obtainSubsidyProgramId(
            SubsidyProgram sp,
            Set<SubsidyProgram> existingLevelSp,
            Map<Dictionary, Map<String, Long>> codesMap
    ) {
        return existingLevelSp.stream()
                .filter(existing -> existing.equals(sp))
                .findFirst()
                .map(SubsidyProgram::getId)
                .orElseGet(() -> {
                    log.debug("No such Subsidy Program among existing. Trying to save it");
                    SubsidyProgram saved = apkService.createSubsidyProgram(sp, codesMap);
                    log.debug("Subsidy Program successfully saved with id: [{}]", saved.getId());
                    existingLevelSp.add(saved);
                    return saved.getId();
                });
    }

    public FinancingSource saveFinancingSource(
            DescriptedBudgetItemData row,
            CashPlanLimit savedCpl,
            SubsidyProgram trdLevelSp,
            Map<Dictionary, Map<String, Long>> codesMap) {
        FinancingSource financingSource = fsMapper.toEntity(row);
        financingSource.setCashPlanLimitId(savedCpl.getId());
        financingSource.setSubsidyProgramId(trdLevelSp.getId());
        return apkService.createFinancingSource(financingSource, codesMap);
    }
}
