package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.ApkPlicanteRestClient;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.CodeType;
import su.petrosoft.apk_ack_integration.model.dto.request.ChangeInstanceStatusRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.response.GetAttributesListResponseDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.CASH_PLAN_LIMIT_TEMPLATE_ID;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.STATUS_ACTUAL_ID;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.STATUS_DRAFT_ID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApkPlicanteService {
    private final ApkPlicanteRestClient apkRestClient;
    private final CashPlanLimitMapper cashPlanLimitMapper;

    public List<CashPlanLimit> getAllCashPlanLimits() {
        List<GetAttributesListResponseDto> dtoList = apkRestClient.getTableAttributesList(
                new GetAttributesListRequestDto(CASH_PLAN_LIMIT_TEMPLATE_ID, STATUS_ACTUAL_ID));
        return dtoList.stream()
                .map(cashPlanLimitMapper::toCpl)
                .toList();
    }

    public List<InstanceDto> createAll(Collection<CashPlanLimit> limitsToCreate) {
        return limitsToCreate.stream()
                .map(cashPlanLimitMapper::toCreateDto)
                .map(apkRestClient::createInstance)
                .toList();
    }

    public Map<CodeType, Map<Long, String>> getAllCodes() {
        log.debug("Receiving all existed codes");
        Map<CodeType, Map<Long, String>> codes = new EnumMap<>(CodeType.class);
        for (CodeType codeType : CodeType.values()) {
            codes.put(codeType, getCodesByType(codeType));
            log.debug("{} code map", codeType.name());
            log.debug(codes.get(codeType).toString());
        }
        long count = codes.values().stream()
                .flatMap(map -> map.entrySet().stream())
                .count();
        log.debug("Found {} codes overall", count);
        return codes;
    }

    private Map<Long, String> getCodesByType(CodeType codeType) {
        log.debug("Receiving codes for type {}", codeType.name());
        List<GetAttributesListResponseDto> list = apkRestClient.getTableAttributesList(
                new GetAttributesListRequestDto(codeType.getTemplateId(), null));
        return list.stream()
                .filter(dto -> dto.shortForm() != null)
                .collect(Collectors.toMap(
                        GetAttributesListResponseDto::id,
                        GetAttributesListResponseDto::shortForm
                ));
    }
}
