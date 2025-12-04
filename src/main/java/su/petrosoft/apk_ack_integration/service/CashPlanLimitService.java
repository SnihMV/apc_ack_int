package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.client.AckRestClient;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.dto.response.AckGetUpdateMessageResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateInstancesFromFileResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.response.UpdateCashPlanLimitResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.RosterKbkExcelRow;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetCodedExcelRow;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetExcelRow;
import su.petrosoft.apk_ack_integration.model.xml.UpdateCashPlanLimitXml;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.getCplCodesOnlyByCurrentYearRequestDto;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.getCreationInstancesFromFileResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class CashPlanLimitService {

    private final ApkPlicanteService apkService;
    private final AckRestClient ackRestClient;
    private final ExcelExtractor excelExtractor;
    private final XmlExtractor xmlExtractor;
    private final CashPlanLimitMapper mapper;
    private final UniBudgetRowService uniBudgetRowService;
    private final ExcelRowMapper excelRowMapper;

    public CreateInstancesFromFileResponseDto createFromRosterKBKExcel(MultipartFile file) {

        List<RosterKbkExcelRow> dtoList = excelExtractor.getRosterKbkRows(file);
        log.debug("Extracted from excel file: [{}] CashPlanLimit rows", dtoList.size());
        List<CashPlanLimit> createdLimits = new ArrayList<>();
        if (!dtoList.isEmpty()) {
            Set<CashPlanLimit> existedLimits = apkService.findCashPlanLimits(getCplCodesOnlyByCurrentYearRequestDto());
            log.debug("Found in Plicante {} CashPlanLimits in total", existedLimits.size());

            List<CashPlanLimit> limitsFromExcel = dtoList.stream()
                    .map(mapper::toCpl)
                    .collect(Collectors.toList());

            limitsFromExcel.removeAll(existedLimits);
            if (!limitsFromExcel.isEmpty()) {
                Map<CodeType, Map<Long, String>> codesMap = apkService.getCodesMap();
                limitsFromExcel.stream()
                        .map(cpl -> apkService.createCashPlanLimit(cpl, codesMap))
                        .forEach(createdLimits::add);
            }
        }
        return getCreationInstancesFromFileResponse(dtoList, createdLimits, CashPlanLimit::getId);
    }

    public CreateInstancesFromFileResponseDto createFromUniBudgetExcel(MultipartFile file) {
        List<UniBudgetCodedExcelRow> dtoList = excelExtractor.getUniBudgetCodedRows(file);
        Set<CashPlanLimit> existingCPL = getLimitsForCurrentYear();
        List<CashPlanLimit> fromExcelCPL = uniBudgetRowService.getLimitsFromExcel(dtoList);
        fromExcelCPL.removeAll(existingCPL);
        log.info("Limits to save count: [{}]", fromExcelCPL.size());

        List<CashPlanLimit> createdLimits = new ArrayList<>();
        if (!fromExcelCPL.isEmpty()) {
            Map<CodeType, Map<Long, String>> codesMap = apkService.getCodesMap();

            createdLimits = fromExcelCPL.stream()
                    .map(cpl -> apkService.createCashPlanLimit(cpl, codesMap))
                    .toList();
        }
        return getCreationInstancesFromFileResponse(dtoList, createdLimits, CashPlanLimit::getId);
    }

    public UpdateCashPlanLimitResponseDto updateByXml() {
        UpdateCashPlanLimitResponseDto response = UpdateCashPlanLimitResponseDto.builder()
                .updatedIds(new ArrayList<>())
                .build();
        AckGetUpdateMessageResponseDto message = ackRestClient.getUpdateMessage();
        UpdateCashPlanLimitXml updatingXml = xmlExtractor.convertBase64String(message, UpdateCashPlanLimitXml.class);
        if (updatingXml == null) {
            return response;
        }
        log.debug("Received request for Cash Plan Limit update: [{}]", updatingXml);

        CashPlanLimit updatingCPL = mapper.toCpl(updatingXml);
        log.debug("Mapped to CashPlanLimit: [{}]", updatingCPL);

        Set<CashPlanLimit> allCashPlanLimits = apkService.findCashPlanLimits(getCplCodesOnlyByCurrentYearRequestDto());
        log.debug("Exist [{}] CashPlanLimits for [{}] year in DB", allCashPlanLimits.size(), LocalDateTime.now().getYear());

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
        CashPlanLimit updatedCpl = apkService.updateCashPlanLimit(updatingCPL);
        log.info("CashPlanLimit [{}] updated", updatedCpl.getId());
        response.updatedIds().add(updatedCpl.getId());
        return response;
    }

    public Set<CashPlanLimit> getLimitsForCurrentYear() {
        Set<CashPlanLimit> allSplByCurrentYear = apkService.findCashPlanLimits(getCplCodesOnlyByCurrentYearRequestDto());
        log.info("Found [{}] Cash Plan Limits for [{}] year in DB", allSplByCurrentYear.size(), LocalDateTime.now().getYear());
        return allSplByCurrentYear;
    }

    public UpdateCashPlanLimitResponseDto updateByExcel(MultipartFile file) {

        List<UniBudgetExcelRow> uniBudgetExcelRows = excelExtractor.uniBudgetExcelRows(file);
        Set<CashPlanLimit> limitsFromExcel = uniBudgetExcelRows.stream()
                .map(mapper::toCpl)
                .collect(Collectors.toSet());
        Set<CashPlanLimit> existingCurrentYearLimits = getLimitsForCurrentYear();

        Set<CashPlanLimit> intersection = new HashSet<>(limitsFromExcel);
        intersection.retainAll(existingCurrentYearLimits);
        ArrayList<Long> updatedCplIds = new ArrayList<>();
        if (!intersection.isEmpty()) {
            log.info("[{}] CashPlanLimits found to be updated", intersection.size());
            for (CashPlanLimit excelCpl : intersection) {
                for (CashPlanLimit existingCpl : existingCurrentYearLimits) {
                    if (existingCpl.equals(excelCpl)) {
                        excelCpl.setId(existingCpl.getId());
                        excelCpl.setVersion(existingCpl.getVersion());
                        CashPlanLimit updatedCpl = apkService.updateCashPlanLimit(excelCpl);
                        updatedCplIds.add(updatedCpl.getId());
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
