package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.client.AckRestClient;
import su.petrosoft.apk_ack_integration.exception.InstanceNotFoundException;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.dto.request.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.UpsertInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.response.AckGetUpdateMessageResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateFromExcelResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.CashPlanLimitExcelRow;
import su.petrosoft.apk_ack_integration.model.xml.UpdateCashPlanLimitXml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CashPlanLimitService {

    private final ApkPlicanteService apkService;
    private final AckRestClient ackRestClient;
    private final ExcelExtractor excelExtractor;
    private final XmlExtractor xmlExtractor;
    private final CashPlanLimitMapper mapper;

    public CreateFromExcelResponseDto createFromExcel(MultipartFile file) {

        List<CashPlanLimitExcelRow> dtoList = excelExtractor.getCashPlanLimitsRows(file);
        log.debug("Extracted from excel file: [{}] CashPlanLimit rows", dtoList.size());
        List<CashPlanLimit> createdLimits = new ArrayList<>();
        if (!dtoList.isEmpty()) {
            List<CashPlanLimit> existedLimits = apkService.getAllCashPlanLimits();
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

    public void updateByXml() {
        AckGetUpdateMessageResponseDto message = ackRestClient.getUpdateMessage();
        UpdateCashPlanLimitXml updatingXml = xmlExtractor.extractXml(message, UpdateCashPlanLimitXml.class);
        if (updatingXml == null) {
            return;
        }
        log.debug("Received request for Cash Plan Limit update: [{}]", updatingXml);

        CashPlanLimit updatingCPL = mapper.toCpl(updatingXml);

        List<CashPlanLimit> allCashPlanLimits = apkService.getAllCashPlanLimits();
        log.debug("Existed Cash Plan Limits: {}", allCashPlanLimits.size());

        CashPlanLimit cplToBeUpdated = allCashPlanLimits.stream()
                .filter(cpl -> cpl.equals(updatingCPL))
                .findFirst()
                .orElseThrow(() -> new InstanceNotFoundException("Updating CashPlanLimit not found"));

        log.debug("Trying to update CashPlanLimit [{}]", cplToBeUpdated.getId());
        Map<CodeType, Map<Long, String>> codesMap = apkService.getCodesMap();
        updatingCPL.setId(cplToBeUpdated.getId());
        updatingCPL.setVersion(cplToBeUpdated.getVersion());
        CashPlanLimit updatedCpl = apkService.updateInstance(updatingCPL, codesMap);
        log.info("CashPlanLimit [{}] updated", updatedCpl.getId());
    }
}
