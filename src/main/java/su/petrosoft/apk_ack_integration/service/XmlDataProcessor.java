package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.AckRestClient;
import su.petrosoft.apk_ack_integration.client.ApkPlicanteRestClient;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.dto.request.ChangeInstanceStatusRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.UpsertInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.response.AckGetUpsertMessageResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.response.GetAttributesListResponseDto;
import su.petrosoft.apk_ack_integration.model.xml.CreateCashPlanLimitsXml;
import su.petrosoft.apk_ack_integration.model.xml.UpsertCashPlanLimitXml;

import java.util.List;

import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.CASH_PLAN_LIMIT_TEMPLATE_ID;

@Service
@Slf4j
@RequiredArgsConstructor
public class XmlDataProcessor {

    private final ApkPlicanteService apkService;
    private final AckRestClient ackClient;
    private final ApkPlicanteRestClient apkClient;
    private final CashPlanLimitMapper mapper;
    private final XmlExtractorService xmlExtractor;

    public void doUpsert() {
        AckGetUpsertMessageResponseDto message = ackClient.getUpsertMessage();
        UpsertCashPlanLimitXml upsertingXml = xmlExtractor.extractXml(message, UpsertCashPlanLimitXml.class);
        if (upsertingXml == null) {
            return;
        }
        log.debug("Received request for Cash Plan Limit upsert: {}", upsertingXml);

        CashPlanLimit cashPlanLimitToUpsert = mapper.toCpl(upsertingXml, apkService.getAllCodes());

        List<GetAttributesListResponseDto> existedCashPlanLimitDtoList = apkClient.getTableAttributesList(
                new GetAttributesListRequestDto(CASH_PLAN_LIMIT_TEMPLATE_ID, null));
        log.debug("Existed Cash Plan Limits: {}", existedCashPlanLimitDtoList.size());

        existedCashPlanLimitDtoList.stream()
                .map(mapper::toCpl)
                .filter(cpl -> cpl.equals(cashPlanLimitToUpsert))
                .findFirst()
                .ifPresentOrElse(cpl -> {
                            log.debug("Existed Cash Plan Limit with id {} will be updated", cpl.getId());
                            cashPlanLimitToUpsert.setId(cpl.getId());
                            cashPlanLimitToUpsert.setVersion(cpl.getVersion());
                            UpsertInstanceRequestDto upsertDto = mapper.toUpdateDto(cashPlanLimitToUpsert);
                            InstanceDto updatedInstance = apkClient.updateInstance(upsertDto);
                            log.debug("Instance [{}] updated", updatedInstance.id());
                        },
                        () -> {
                            log.debug("Not found Cash Plan Limit for update. Will be create new");
                            UpsertInstanceRequestDto upsertDto = mapper.toUpdateDto(cashPlanLimitToUpsert);
                            InstanceDto createdInstance = apkClient.updateInstance(upsertDto);
                            log.debug("Instance [{}] created", createdInstance.id());
                        });
    }

    public void doCreate() {
        AckGetUpsertMessageResponseDto message = ackClient.getUpsertMessage();
        CreateCashPlanLimitsXml creatingXml = xmlExtractor.extractXml(message, CreateCashPlanLimitsXml.class);
    }

}
