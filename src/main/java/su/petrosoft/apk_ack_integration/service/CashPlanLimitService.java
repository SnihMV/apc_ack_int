package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.client.NiFiRestClient;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.data.CashPlanLimitData;
import su.petrosoft.apk_ack_integration.model.dto.response.AckGetUpdateMessageResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.response.CreatingInstancesFromFileResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.response.UpdateCashPlanLimitResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;
import su.petrosoft.apk_ack_integration.model.data.DescriptedBudgetItemData;
import su.petrosoft.apk_ack_integration.model.data.xml.UpdateCashPlanLimitXml;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.*;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.getCplCodesOnlyByCurrentYearRequestDto;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.creatingInstancesFromFileResponseDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class CashPlanLimitService {

    private final ApkPlicanteService apkService;
    private final NiFiRestClient niFiRestClient;
    private final ExcelExtractor excelExtractor;
    private final XmlExtractor xmlExtractor;
    private final CashPlanLimitMapper mapper;
    private final UniBudgetRowService uniBudgetRowService;
    private final ExcelRowMapper excelRowMapper;

    public CreatingInstancesFromFileResponseDto createFromRosterKBKExcel(MultipartFile file) {

        List<? extends CashPlanLimitData> dtoList = excelExtractor.getRosterKbkRows(file);
        log.debug("Extracted from excel file: [{}] CashPlanLimit rows", dtoList.size());
        List<CashPlanLimit> createdLimits = new ArrayList<>();
        if (!dtoList.isEmpty()) {
            Map<Dictionary, Map<Long, Map.Entry<String, String>>> codesMap = apkService.getDictionariesCodesMap(
                Set.of(
                    KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE));
            Set<CashPlanLimit> existedLimits = apkService.findCashPlanLimits(
                getCplCodesOnlyByCurrentYearRequestDto(), codesMap);
            log.debug("Found in Plicante {} CashPlanLimits in total", existedLimits.size());

            List<CashPlanLimit> limitsFromExcel = dtoList.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList());

            limitsFromExcel.removeAll(existedLimits);
            if (!limitsFromExcel.isEmpty()) {

                limitsFromExcel.stream()
                    .map(cpl -> apkService.createCashPlanLimit(cpl, codesMap))
                    .forEach(createdLimits::add);
            }
        }
        return creatingInstancesFromFileResponseDto(dtoList, createdLimits, CashPlanLimit::getId);
    }

    public CreatingInstancesFromFileResponseDto createFromUniBudgetExcel(MultipartFile file) {
//        List<BaseUniBudgetExcelRow> dtoList = excelExtractor.getUniBudgetCodedRows(file);
//        List<DescriptedBudgetItemData> dtoList = excelExtractor.getUniBudget2026ClarifiedRows(file);
        List<DescriptedBudgetItemData> dtoList = excelExtractor.getUniBudget20262801Rows(file);
        Set<CashPlanLimit> existingCPL = getLimitsForCurrentYear();
        List<CashPlanLimit> fromExcelCPL = uniBudgetRowService.getLimitsFromExcel(dtoList);
        fromExcelCPL.removeAll(existingCPL);
        log.info("Limits to save count: [{}]", fromExcelCPL.size());

        List<CashPlanLimit> createdLimits = new ArrayList<>();
        if (!fromExcelCPL.isEmpty()) {
            Map<Dictionary, Map<Long, Map.Entry<String, String>>> codesMap = apkService.getDictionariesCodesMap(
                Set.of(
                    KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE));

            createdLimits = fromExcelCPL.stream()
                .map(cpl -> apkService.createCashPlanLimit(cpl, codesMap))
                .toList();
        }
        return creatingInstancesFromFileResponseDto(dtoList, createdLimits, CashPlanLimit::getId);
    }

    public UpdateCashPlanLimitResponseDto updateByXml() {
        UpdateCashPlanLimitResponseDto response = UpdateCashPlanLimitResponseDto.builder()
            .updatedIds(new ArrayList<>())
            .build();
        AckGetUpdateMessageResponseDto message = niFiRestClient.getUpdateMessage();
        UpdateCashPlanLimitXml updatingXml = xmlExtractor.convertBase64String(message,
            UpdateCashPlanLimitXml.class);
        if (updatingXml == null) {
            return response;
        }
        log.debug("Received request for Cash Plan Limit update: [{}]", updatingXml);

        CashPlanLimit updatingCPL = mapper.toEntity(updatingXml);
        log.debug("Mapped to CashPlanLimit: [{}]", updatingCPL);
        Map<Dictionary, Map<Long, Map.Entry<String, String>>> codesMap = apkService.getDictionariesCodesMap(
            Set.of(
                KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE));

        Set<CashPlanLimit> allCashPlanLimits = apkService.findCashPlanLimits(
            getCplCodesOnlyByCurrentYearRequestDto(), codesMap);
        log.debug("Exist [{}] CashPlanLimits for [{}] year in DB", allCashPlanLimits.size(),
            LocalDateTime.now().getYear());

        CashPlanLimit cplToUpdate = allCashPlanLimits.stream()
            .filter(cpl -> cpl.equals(updatingCPL))
            .findFirst()
            .orElse(null);
        if (cplToUpdate == null) {
            return response;
        }
        log.debug("Trying to update CashPlanLimit [{}]", cplToUpdate.getId());
        updatingCPL.setId(cplToUpdate.getId());
        updatingCPL.setVersion(cplToUpdate.getVersion());
        long updatedCplId = apkService.updateCashPlanLimit(updatingCPL, codesMap);
        log.info("CashPlanLimit [{}] updated", updatedCplId);
        response.updatedIds().add(updatedCplId);
        return response;
    }

    public Set<CashPlanLimit> getLimitsForCurrentYear() {
        Map<Dictionary, Map<Long, Map.Entry<String, String>>> codesMap = apkService.getDictionariesCodesMap(
            Set.of(
                KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE));
        Set<CashPlanLimit> allSplByCurrentYear = apkService.findCashPlanLimits(
            getCplCodesOnlyByCurrentYearRequestDto(), codesMap);
        log.info("Found [{}] Cash Plan Limits for [{}] year in DB", allSplByCurrentYear.size(),
            LocalDateTime.now().getYear());
        return allSplByCurrentYear;
    }

    public UpdateCashPlanLimitResponseDto updateByExcel(MultipartFile file) {

        List<DescriptedBudgetItemData> uniBudgetExcelRows = excelExtractor.uniBudgetExcelRows(file);
        Set<CashPlanLimit> limitsFromExcel = uniBudgetExcelRows.stream()
            .map(mapper::toEntity)
            .collect(Collectors.toSet());
        Set<CashPlanLimit> existingCurrentYearLimits = getLimitsForCurrentYear();

        Set<CashPlanLimit> intersection = new HashSet<>(limitsFromExcel);
        intersection.retainAll(existingCurrentYearLimits);
        ArrayList<Long> updatedCplIds = new ArrayList<>();
        if (!intersection.isEmpty()) {
            log.info("[{}] CashPlanLimits found to be updated", intersection.size());
            Map<Dictionary, Map<Long, Map.Entry<String, String>>> codesMap = apkService.getDictionariesCodesMap(
                Set.of(
                    KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE));
            for (CashPlanLimit excelCpl : intersection) {
                for (CashPlanLimit existingCpl : existingCurrentYearLimits) {
                    if (existingCpl.equals(excelCpl)) {
                        excelCpl.setId(existingCpl.getId());
                        excelCpl.setVersion(existingCpl.getVersion());
                        long updatedCplId = apkService.updateCashPlanLimit(excelCpl, codesMap);
                        updatedCplIds.add(updatedCplId);
                    }
                }
            }
        }
        return UpdateCashPlanLimitResponseDto.builder()
            .incomingCount(limitsFromExcel.size())
            .intersectedCount(intersection.size())
            .updatedIds(updatedCplIds)
            .build();
    }
}
