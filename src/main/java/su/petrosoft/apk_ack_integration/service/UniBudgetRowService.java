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
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetExcelRow;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UniBudgetRowService {
    private final ApkPlicanteService apkService;
    private final CashPlanLimitMapper cplMapper;
    private final SubsidyProgramMapper spMapper;
    private final FinancingSourceMapper fsMapper;

    public CashPlanLimit saveCashPlanLimit(UniBudgetExcelRow dto, Map<CodeType, Map<Long, String>> codesMap) {
        CashPlanLimit cplFromRow = cplMapper.toCpl(dto);
        return apkService.createCashPlanLimit(cplFromRow, codesMap);
    }

    public SubsidyProgram getOrCreateSubsidyProgram(
            UniBudgetExcelRow dto,
            Set<SubsidyProgram> existingSP,
            Map<CodeType, Map<Long, String>> codesMap) {

        SubsidyProgram fstLevelSp = buildFirstLevelSP(dto, existingSP, codesMap);
        SubsidyProgram scdLevelSp = buildSecondLevelSP(dto, existingSP, codesMap, fstLevelSp);
        SubsidyProgram trdLevelSp = buildThirdLevelSP(dto, existingSP, codesMap, scdLevelSp);
        log.debug("Subsidy Program Map count: {}", existingSP);
        return trdLevelSp;
    }

    public FinancingSource buildFinancingSource(UniBudgetExcelRow dto, Set<SubsidyProgram> allValidThirdLvlSPFromDb) {
        FinancingSource financingSource = fsMapper.toEntity(dto);
        allValidThirdLvlSPFromDb.stream()
                .filter(sp -> sp.equals(spMapper.toThirdLevelSP(dto)))
                .findFirst()
                .map(SubsidyProgram::getId)
                .ifPresent(financingSource::setSubsidyProgramId);
        return financingSource;
    }

    private SubsidyProgram buildFirstLevelSP(
            UniBudgetExcelRow dto,
            Set<SubsidyProgram> existingSP,
            Map<CodeType, Map<Long, String>> codesMap
    ) {
        SubsidyProgram fstLvlSp = spMapper.toFirstLevelSP(dto);
        Long id = obtainSubsidyProgramId(fstLvlSp, existingSP, codesMap);
        fstLvlSp.setId(id);
        log.debug("First level Subsidy Program from excel row: [{}]", fstLvlSp);
        return fstLvlSp;
    }

    private SubsidyProgram buildSecondLevelSP(
            UniBudgetExcelRow dto,
            Set<SubsidyProgram> existingSP,
            Map<CodeType, Map<Long, String>> codesMap,
            SubsidyProgram fstLevelSp) {

        SubsidyProgram scdLvlSP = spMapper.toSecondLevelSP(dto);
        scdLvlSP.setParentId(fstLevelSp.getId());
        Long id = obtainSubsidyProgramId(scdLvlSP, existingSP, codesMap);
        scdLvlSP.setId(id);
        log.debug("Second level Subsidy Program from excel row: [{}]", scdLvlSP);
        return scdLvlSP;
    }

    private SubsidyProgram buildThirdLevelSP(
            UniBudgetExcelRow dto,
            Set<SubsidyProgram> existingSP,
            Map<CodeType, Map<Long, String>> codesMap,
            SubsidyProgram scdLevelSp) {

        SubsidyProgram trdLvlSP = spMapper.toThirdLevelSP(dto);
        trdLvlSP.setParentId(scdLevelSp.getId());
        Long id = obtainSubsidyProgramId(trdLvlSP, existingSP, codesMap);
        trdLvlSP.setId(id);
        log.debug("Third level Subsidy Program from excel row: [{}]", trdLvlSP);
        return trdLvlSP;
    }

    private Long obtainSubsidyProgramId(
            SubsidyProgram sp,
            Set<SubsidyProgram> existingSP,
            Map<CodeType, Map<Long, String>> codesMap
    ) {

        return existingSP.stream()
                .filter(existing -> existing.equals(sp))
                .findFirst()
                .map(SubsidyProgram::getId)
                .orElseGet(() -> {
                    log.debug("No such Subsidy Program among existing. Trying to save it");
                    SubsidyProgram saved = apkService.createSubsidyProgram(sp, codesMap);
                    log.debug("Subsidy Program successfully saved with id: [{}]", saved.getId());
                    existingSP.add(saved);
                    return saved.getId();
                });
    }

    public FinancingSource saveFinancingSource(
            UniBudgetExcelRow row,
            CashPlanLimit savedCpl,
            SubsidyProgram trdLevelSp,
            Map<CodeType, Map<Long, String>> codesMap) {
        FinancingSource financingSource = fsMapper.toEntity(row);
        financingSource.setCashPlanLimitId(savedCpl.getId());
        financingSource.setSubsidyProgramId(trdLevelSp.getId());
        return apkService.createFinancingSource(financingSource, codesMap);
    }
}
