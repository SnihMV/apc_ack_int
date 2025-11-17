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
import su.petrosoft.apk_ack_integration.model.dto.response.UpdateCashPlanLimitResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.CashPlanLimitExcelRow;
import su.petrosoft.apk_ack_integration.model.xml.UpdateCashPlanLimitXml;

import java.util.ArrayList;
import java.util.Collections;
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

        List<CashPlanLimit> allCashPlanLimits = apkService.getAllCashPlanLimits();
        log.debug("Existing CashPlanLimits in DB: {}", allCashPlanLimits.size());

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
