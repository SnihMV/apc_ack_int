package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.CharacterEncodingFilter;
import su.petrosoft.apk_ack_integration.client.EsbRestClient;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.CodeType;
import su.petrosoft.apk_ack_integration.model.dto.request.ChangeInstanceStatusRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.UpsertInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.Instance;
import su.petrosoft.apk_ack_integration.model.dto.response.EsbGetMessageResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.response.GetAttributesListResponseDto;
import su.petrosoft.apk_ack_integration.model.xml.CreateCashPlanLimitsXml;
import su.petrosoft.apk_ack_integration.model.xml.UpsertCashPlanLimitXml;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.CASH_PLAN_LIMIT_TEMPLATE_ID;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.STATUS_ACTUAL_ID;

@Service
@Slf4j
@RequiredArgsConstructor
public class XmlDataProcessor {

    private final EsbRestClient esbClient;
    private final PlicanteRestClient plicanteClient;
    private final CashPlanLimitMapper mapper;
    private final XmlExtractorService xmlExtractor;
    private final CharacterEncodingFilter characterEncodingFilter;

    public void doUpsert() {
        EsbGetMessageResponseDto message = esbClient.getMessage("api/esb/getMessage");
        UpsertCashPlanLimitXml upsertingXml = xmlExtractor.extractXml(message, UpsertCashPlanLimitXml.class);
        if (upsertingXml == null) {
            return;
        }
        log.debug("Received request for Cash Plan Limit upsert: {}", upsertingXml);

        CashPlanLimit cashPlanLimitToUpsert = mapper.toEntity(upsertingXml, getAllCodes());

        List<GetAttributesListResponseDto> existedCashPlanLimitDtoList = plicanteClient.getTableAttributesList(
                new GetAttributesListRequestDto(CASH_PLAN_LIMIT_TEMPLATE_ID, STATUS_ACTUAL_ID));
        log.debug("Existed Cash Plan Limits: {}", existedCashPlanLimitDtoList.size());

        existedCashPlanLimitDtoList.stream()
                .map(mapper::toEntity)
                .filter(cpl -> cpl.equals(cashPlanLimitToUpsert))
                .findFirst()
                .ifPresentOrElse(cpl -> {
                            log.debug("Existed Cash Plan Limit with id {} will be updated", cpl.getId());
                            cashPlanLimitToUpsert.setId(cpl.getId());
                            cashPlanLimitToUpsert.setVersion(cpl.getVersion());
                            UpsertInstanceRequestDto upsertDto = mapper.toUpsertDto(cashPlanLimitToUpsert);
                            Instance updatedInstance = plicanteClient.updateInstance(upsertDto);
                            log.debug("Instance [{}] updated", updatedInstance.id());
                        },
                        () -> {
                            log.debug("Not found Cash Plan Limit for update. Will be create new");
                            UpsertInstanceRequestDto upsertDto = mapper.toUpsertDto(cashPlanLimitToUpsert);
                            Instance draft = plicanteClient.updateInstance(upsertDto);
                            log.debug("Instance [{}] created. Status DRAFT", draft.id());
                            ChangeInstanceStatusRequestDto changeStatusDto = new ChangeInstanceStatusRequestDto(
                                    draft.id(), draft.status().id(), STATUS_ACTUAL_ID);
                            plicanteClient.changeStatus(changeStatusDto);
                            log.debug("Instance [{}] status changed to ACTUAL", draft.id());
                        });
    }

    public void doCreate() {
        EsbGetMessageResponseDto message = esbClient.getMessage("api/esb/getMessageKP");
        CreateCashPlanLimitsXml creatingXml = xmlExtractor.extractXml(message, CreateCashPlanLimitsXml.class);
    }

    private Map<CodeType, Map<Long, String>> getAllCodes() {
        Map<CodeType, Map<Long, String>> codes = new EnumMap<>(CodeType.class);
        for (CodeType codeType : CodeType.values()) {
            codes.put(codeType, getCodeTypeCodes(codeType));
            log.debug("{} code map", codeType.name());
            log.debug(codes.get(codeType).toString());
        }
        return codes;
    }

    private Map<Long, String> getCodeTypeCodes(CodeType codeType) {
        List<GetAttributesListResponseDto> list = plicanteClient.getTableAttributesList(
                new GetAttributesListRequestDto(codeType.getTemplateId(), null));
        return list.stream()
                .filter(dto -> dto.shortForm() != null)
                .collect(Collectors.toMap(
                        GetAttributesListResponseDto::id,
                        GetAttributesListResponseDto::shortForm
                ));
    }
}
