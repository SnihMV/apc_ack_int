package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.AckRestClient;
import su.petrosoft.apk_ack_integration.client.ApkPlicanteRestClient;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.dto.request.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.UpsertInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.response.AckGetUpdateMessageResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.xml.CreateCashPlanLimitsXml;
import su.petrosoft.apk_ack_integration.model.xml.UpdateCashPlanLimitXml;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class XmlDataProcessor {

    private final ApkPlicanteService apkService;
    private final AckRestClient ackClient;
    private final ApkPlicanteRestClient apkClient;
    private final CashPlanLimitMapper mapper;
    private final XmlExtractor xmlExtractor;

    public void doUpsert() {
        AckGetUpdateMessageResponseDto message = ackClient.getUpdateMessage();
        UpdateCashPlanLimitXml upsertingXml = xmlExtractor.extractXml(message, UpdateCashPlanLimitXml.class);
        if (upsertingXml == null) {
            return;
        }
        log.debug("Received request for Cash Plan Limit upsert: {}", upsertingXml);

        Map<CodeType, Map<Long, String>> codesMap = apkService.getCodesMap();
        CashPlanLimit cplToUpdate = mapper.toCpl(upsertingXml);

        List<CashPlanLimit> allCashPlanLimits = apkService.getAllCashPlanLimits();
        log.debug("Existed Cash Plan Limits: {}", allCashPlanLimits.size());

        allCashPlanLimits.stream()
                .filter(cpl -> cpl.equals(cplToUpdate))
                .findFirst()
                .ifPresentOrElse(cpl -> {
                            log.debug("Existed Cash Plan Limit with id {} will be updated", cpl.getId());
                            cplToUpdate.setId(cpl.getId());
                            cplToUpdate.setVersion(cpl.getVersion());
                            UpsertInstanceRequestDto upsertDto = mapper.toUpdateDto(cplToUpdate, codesMap);
                            InstanceDto updatedInstance = apkClient.updateInstance(upsertDto);
                            log.info("CashPlanLimit [{}] updated", updatedInstance.id());
                        },
                        () -> {
                            log.debug("Not found Cash Plan Limit for update. Will be create new");
                            CreateInstanceRequestDto createDto = mapper.toCreateDto(cplToUpdate, codesMap);
                            InstanceDto createdInstance = apkClient.createInstance(createDto);
                            log.debug("Instance [{}] created", createdInstance.id());
                        });
    }

    public void doCreate() {
        AckGetUpdateMessageResponseDto message = ackClient.getUpdateMessage();
        CreateCashPlanLimitsXml creatingXml = xmlExtractor.extractXml(message, CreateCashPlanLimitsXml.class);
    }

}
