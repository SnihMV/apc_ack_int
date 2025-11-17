package su.petrosoft.apk_ack_integration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.ApkPlicanteRestClient;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.mapper.FinancingSourceMapper;
import su.petrosoft.apk_ack_integration.mapper.SubsidyProgramMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.FinancingSource;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.dto.request.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.response.GetAttributesListResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.CASH_PLAN_LIMIT_TEMPLATE_ID;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.subsidyProgramRequestDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApkPlicanteService {

    private final ApkPlicanteRestClient apkRestClient;
    private final CashPlanLimitMapper cplMapper;
    private final SubsidyProgramMapper spMapper;
    private final FinancingSourceMapper fsMapper;
    private final ObjectMapper objectMapper;

    public List<CashPlanLimit> getAllCashPlanLimits() {
        List<InstanceDto> dtoList = apkRestClient.getExistedInstances(
            InstanceDto.builder()
                    .templateId(CASH_PLAN_LIMIT_TEMPLATE_ID)
                    .build());
        return dtoList.stream()
            .map(cplMapper::toCpl)
            .toList();
    }

    public List<SubsidyProgram> getAllSubsidyPrograms(Map<Long, Object> filterMap) {
        InstanceDto requestDto = subsidyProgramRequestDto(filterMap);
        log.debug("Request to receive all existing Subsidy Programs with DTO");
        List<InstanceDto> dtoList = apkRestClient.getExistedInstances(requestDto);
        return dtoList.stream()
            .map(spMapper::toEntity)
            .toList();
    }

    public CashPlanLimit createCashPlanLimit(CashPlanLimit cpl, Map<CodeType, Map<Long, String>> codesMap) {
        CreateInstanceRequestDto dto = cplMapper.toCreateDto(cpl, codesMap);
        InstanceDto instance = apkRestClient.createInstance(dto);
        return cplMapper.toCpl(instance);
    }

    public SubsidyProgram createSubsidyProgram(SubsidyProgram sp, Map<CodeType, Map<Long, String>> codesMap) {
        CreateInstanceRequestDto dto = spMapper.toCreateDto(sp, codesMap);
        InstanceDto instance = apkRestClient.createInstance(dto);
        return spMapper.toEntity(instance);
    }

    public Map<CodeType, Map<Long, String>> getCodesMap(CodeType... types) {
        if (types == null || types.length == 0) {
            types = CodeType.values();
        }
        log.debug("Receiving existed codes for types: {}", Arrays.stream(types).map(Enum::name).collect(joining(",")));
        Map<CodeType, Map<Long, String>> codes = new EnumMap<>(CodeType.class);
        for (CodeType codeType : types) {
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
            .collect(toMap(
                GetAttributesListResponseDto::id,
                GetAttributesListResponseDto::shortForm
            ));
    }

    public List<SubsidyProgram> getAllSubsidyPrograms() {
        return getAllSubsidyPrograms(null);
    }

    @SneakyThrows
    public FinancingSource createFinancingSource(FinancingSource financingSource, Map<CodeType, Map<Long, String>> codesMap) {
        CreateInstanceRequestDto createDto = fsMapper.toCreateDto(financingSource, codesMap);
        String json = objectMapper.writeValueAsString(createDto);
        log.debug("JSON to create: [{}]", json);
        InstanceDto created = apkRestClient.createInstance(createDto);
        return fsMapper.toEntity(created);
    }
}
