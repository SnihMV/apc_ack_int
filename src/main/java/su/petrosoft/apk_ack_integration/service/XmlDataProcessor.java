package su.petrosoft.apk_ack_integration.service;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPEK;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPFK;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPKR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KCSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KFSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KOSGU;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KVR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KVSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.PURPOSE;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.requestDtoToGettingCplEqualsFieldsByCurrentYear;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.NiFiRestClient;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.data.xml.CreateCashPlanLimitsXml;
import su.petrosoft.apk_ack_integration.model.data.xml.UpdateCashPlanLimitXml;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.response.AckGetUpdateMessageResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

@Service
@Slf4j
@RequiredArgsConstructor
public class XmlDataProcessor {

    private final ApkPlicanteService apkService;
    private final NiFiRestClient ackClient;
    private final PlicanteRestClient apkClient;
    private final CashPlanLimitMapper mapper;
    private final XmlExtractor xmlExtractor;

    public void doUpsert() {
        AckGetUpdateMessageResponseDto message = ackClient.getUpdateMessage();
        UpdateCashPlanLimitXml upsertingXml = xmlExtractor.convertBase64String(message,
            UpdateCashPlanLimitXml.class);
        if (upsertingXml == null) {
            return;
        }
        log.debug("Received request for Cash Plan Limit upsert: {}", upsertingXml);

        Map<Dictionary, Map<String, Long>> codesMap = apkService.getDictionariesCodesMap(
            Set.of(KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE));
        CashPlanLimit cplToUpdate = mapper.toEntity(upsertingXml, codesMap);

        Set<CashPlanLimit> allCashPlanLimits = apkService.findCashPlanLimits(
            requestDtoToGettingCplEqualsFieldsByCurrentYear());
        log.debug("Exist [{}] CashPlanLimits for [{}] year in DB", allCashPlanLimits.size(),
            LocalDateTime.now().getYear());

        allCashPlanLimits.stream()
            .filter(cpl -> cpl.equals(cplToUpdate))
            .findFirst()
            .ifPresentOrElse(cpl -> {
                    log.debug("Existed Cash Plan Limit with id {} will be updated", cpl.getId());
                    cplToUpdate.setId(cpl.getId());
                    cplToUpdate.setVersion(cpl.getVersion());
                    UpdateInstanceRequestDto upsertDto = mapper.toUpdateDto(cplToUpdate);
                    UpdateInstanceResponseDto dto = apkClient.updateInstance(upsertDto);
                    log.info("CashPlanLimit [{}] updated", dto.id());
                },
                () -> {
                    log.debug("Not found Cash Plan Limit for update. Will be create new");
                    CreateInstanceRequestDto createDto = mapper.toCreateDto(cplToUpdate);
                    InstanceDto createdInstance = apkClient.createInstance(createDto);
                    log.debug("Instance [{}] created", createdInstance.id());
                });
    }

    public void doCreate() {
        AckGetUpdateMessageResponseDto message = ackClient.getUpdateMessage();
        CreateCashPlanLimitsXml creatingXml = xmlExtractor.convertBase64String(message,
            CreateCashPlanLimitsXml.class);
    }

}
