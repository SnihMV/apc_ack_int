package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.mapper.FinancingSourceMapper;
import su.petrosoft.apk_ack_integration.mapper.SubsidyProgramMapper;
import su.petrosoft.apk_ack_integration.model.FinancingSource;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.CashPlanLimitExcelRow;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetExcelRowDto;

import java.util.Map;
import java.util.Set;

import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KCSR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KDR;
import static su.petrosoft.apk_ack_integration.util.DictionaryUtil.getCodeId;

@Service
@Slf4j
@RequiredArgsConstructor
public class UniBudgetRowService {
    private final ApkPlicanteService apkService;
    private final CashPlanLimitMapper cplMapper;
    private final SubsidyProgramMapper spMapper;
    private final FinancingSourceMapper fsMapper;
    private final ExcelExtractor extractor;

    public SubsidyProgram createSubsidyPrograms(
            UniBudgetExcelRowDto dto,
            Set<SubsidyProgram> existingSP,
            Map<CodeType, Map<Long, String>> codesMap) {

        SubsidyProgram fstLevelSp = buildFirstLevelSP(dto, existingSP, codesMap);
        SubsidyProgram scdLevelSp = buildSecondLevelSP(dto, existingSP, codesMap, fstLevelSp);
        SubsidyProgram trdLevelSp = buildThirdLevelSP(dto, existingSP, codesMap, scdLevelSp);
        log.debug("Subsidy Program Map count: {}", existingSP);
        return trdLevelSp;
    }

    public FinancingSource buildFinancingSource(UniBudgetExcelRowDto dto, Set<SubsidyProgram> allValidThirdLvlSPFromDb) {
        FinancingSource financingSource = fsMapper.fromUniBudgetDto(dto);
        allValidThirdLvlSPFromDb.stream()
                .filter(sp -> sp.equals(spMapper.toThirdLevelSP(dto)))
                .findFirst()
                .map(SubsidyProgram::getId)
                .ifPresent(financingSource::setSubsidyProgramId);
        return financingSource;
    }

    private SubsidyProgram buildFirstLevelSP(
            UniBudgetExcelRowDto dto,
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
            UniBudgetExcelRowDto dto,
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
            UniBudgetExcelRowDto dto,
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
}
