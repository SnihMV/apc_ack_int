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
import su.petrosoft.apk_ack_integration.model.dto.request.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.response.GetAttributesListResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApkPlicanteService {

    private final ApkPlicanteRestClient apkRestClient;
    private final CashPlanLimitMapper cplMapper;
    private final SubsidyProgramMapper spMapper;
    private final FinancingSourceMapper fsMapper;
    private final ObjectMapper objectMapper;

    public Set<CashPlanLimit> findCashPlanLimits(InstanceDto requestDto) {
        List<InstanceDto> dtoList = apkRestClient.getExistedInstances(requestDto);
        return dtoList.stream()
                .map(cplMapper::toCpl)
                .collect(toSet());
    }

    public Set<SubsidyProgram> findSubsidyPrograms(InstanceDto requestDto) {
        List<InstanceDto> dtoList = apkRestClient.getExistedInstances(requestDto);
        return dtoList.stream()
                .map(spMapper::toEntity)
                .collect(toSet());
    }

    public Set<FinancingSource> findFinancingSources(InstanceDto requestDto) {
        List<InstanceDto> dtoList = apkRestClient.getExistedInstances(requestDto);
        return dtoList.stream()
                .map(fsMapper::toEntity)
                .collect(toSet());
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
        log.info("Receiving existed codes for types: {}", Arrays.stream(types).map(Enum::name).collect(joining(",")));
        Map<CodeType, Map<Long, String>> codes = new EnumMap<>(CodeType.class);
        for (CodeType codeType : types) {
            codes.put(codeType, getCodesByType(codeType));
            log.debug("{} code map:\n[{}]", codeType.name(), codes.get(codeType).toString());
        }
        long count = codes.values().stream()
                .flatMap(map -> map.entrySet().stream())
                .count();
        log.info("Extracted {} codes", count);
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

    @SneakyThrows
    public FinancingSource createFinancingSource(FinancingSource financingSource, Map<CodeType, Map<Long, String>> codesMap) {
        CreateInstanceRequestDto createDto = fsMapper.toCreateDto(financingSource, codesMap);
        String ss = objectMapper.writeValueAsString(createDto);
        log.debug("Financing Source Creating JSON [{}]", ss);
        InstanceDto created = apkRestClient.createInstance(createDto);
        String s = objectMapper.writeValueAsString(created);
        log.debug("Financing Source Created JSON [{}]", s);
        return fsMapper.toEntity(created);
    }

    public CashPlanLimit updateCashPlanLimit(CashPlanLimit updatedCpl) {
        UpdateInstanceRequestDto updateDto = cplMapper.toUpdateDto(updatedCpl);
        InstanceDto updatedInstance = apkRestClient.updateInstance(updateDto);
        return cplMapper.toCpl(updatedInstance);
    }
}
