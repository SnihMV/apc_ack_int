package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.client.AckRestClient;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.dto.response.AckGetUpdateMessageResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateFromExcelResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.response.UpdateCashPlanLimitResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.CashPlanLimitExcelRow;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetExcelRow;
import su.petrosoft.apk_ack_integration.model.xml.UpdateCashPlanLimitXml;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.cashPlanLimitsCodesByCurrentYear;

@Service
@Slf4j
@RequiredArgsConstructor
public class CashPlanLimitService {

    private final ApkPlicanteService apkService;
    private final AckRestClient ackRestClient;
    private final ExcelExtractor excelExtractor;
    private final XmlExtractor xmlExtractor;
    private final CashPlanLimitMapper mapper;

    public CreateFromExcelResponseDto createFromRosterKBKExcel(MultipartFile file) {

        List<CashPlanLimitExcelRow> dtoList = excelExtractor.getCashPlanLimitsRows(file);
        log.debug("Extracted from excel file: [{}] CashPlanLimit rows", dtoList.size());
        List<CashPlanLimit> createdLimits = new ArrayList<>();
        if (!dtoList.isEmpty()) {
            Set<CashPlanLimit> existedLimits = apkService.getAllCashPlanLimits(cashPlanLimitsCodesByCurrentYear());
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
        return new CreateFromExcelResponseDto(
                dtoList.size(),
                createdLimits.size(),
                createdLimits.stream()
                        .map(CashPlanLimit::getId)
                        .toList());
    }

    public CreateFromExcelResponseDto createFromUniBudgetExcel(List<UniBudgetExcelRow> dtoList) {
        Set<CashPlanLimit> existingCPL = apkService.getAllCashPlanLimits(cashPlanLimitsCodesByCurrentYear());
        log.info("Existing Limits in DB count: [{}]", existingCPL.size());
        List<CashPlanLimit> candidateCPL = dtoList.stream()
                .map(mapper::toCpl)
                .collect(toList());
        log.info("Limits from excel file count: [{}]", candidateCPL.size());

        candidateCPL.removeAll(existingCPL);
        log.info("Limits to save count: [{}]", candidateCPL.size());
        List<CashPlanLimit> createdLimits = new ArrayList<>();
        if (!candidateCPL.isEmpty()) {
            Map<CodeType, Map<Long, String>> codesMap = apkService.getCodesMap();
            createdLimits = candidateCPL.stream()
                    .map(cpl -> apkService.createCashPlanLimit(cpl, codesMap))
                    .toList();
        }
        return new CreateFromExcelResponseDto(
                dtoList.size(),
                createdLimits.size(),
                createdLimits.stream()
                        .map(CashPlanLimit::getId)
                        .toList());
    }

    public UpdateCashPlanLimitResponseDto updateByXml() {
        UpdateCashPlanLimitResponseDto response = new UpdateCashPlanLimitResponseDto(new ArrayList<>());
        AckGetUpdateMessageResponseDto message = ackRestClient.getUpdateMessage();
        UpdateCashPlanLimitXml updatingXml = xmlExtractor.extractXml(message, UpdateCashPlanLimitXml.class);
        if (updatingXml == null) {
            return response;
        }
        log.debug("Received request for Cash Plan Limit update: [{}]", updatingXml);

        CashPlanLimit updatingCPL = mapper.toCpl(updatingXml);
        log.debug("Mapped to CashPlanLimit: [{}]", updatingCPL);

        Set<CashPlanLimit> allCashPlanLimits = apkService.getAllCashPlanLimits(cashPlanLimitsCodesByCurrentYear());
        log.debug("Exist [{}] CashPlanLimits for [{}] year in DB", allCashPlanLimits.size(), LocalDateTime.now().getYear());

        CashPlanLimit cplToUpdate = allCashPlanLimits.stream()
                .filter(cpl -> cpl.equals(updatingCPL))
                .findFirst()
                .orElse(null);
        if (cplToUpdate == null) {
            return response;
        }
        log.debug("Trying to update CashPlanLimit [{}]", cplToUpdate.getId());
        Map<CodeType, Map<Long, String>> codesMap = apkService.getCodesMap();
        updatingCPL.setId(cplToUpdate.getId());
        updatingCPL.setVersion(cplToUpdate.getVersion());
        CashPlanLimit updatedCpl = apkService.updateInstance(updatingCPL, codesMap);
        log.info("CashPlanLimit [{}] updated", updatedCpl.getId());
        response.updatedInstances().add(updatedCpl.getId());
        return response;
    }
}
