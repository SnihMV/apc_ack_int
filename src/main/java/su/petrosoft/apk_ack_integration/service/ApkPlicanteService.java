package su.petrosoft.apk_ack_integration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.ApkPlicanteRestClient;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.mapper.CropProductionMainFormMapper;
import su.petrosoft.apk_ack_integration.mapper.FinancingSourceMapper;
import su.petrosoft.apk_ack_integration.mapper.OperationalReportMapper;
import su.petrosoft.apk_ack_integration.mapper.SubsidyProgramMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.CropProductionMainForm;
import su.petrosoft.apk_ack_integration.model.FinancingSource;
import su.petrosoft.apk_ack_integration.model.OperationalReport;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.FillingMainFormRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static su.petrosoft.apk_ack_integration.util.OperationalReportUtil.buildGetReportsForFillingMainFormRequestDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApkPlicanteService {

    private final ApkPlicanteRestClient apkRestClient;
    private final CashPlanLimitMapper cplMapper;
    private final SubsidyProgramMapper spMapper;
    private final FinancingSourceMapper fsMapper;
    private final CropProductionMainFormMapper cpmfMapper;
    private final OperationalReportMapper orMapper;
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
        log.info("Received [{}] Financing Sources in DB", dtoList.size());
        return dtoList.stream()
                .map(fsMapper::toEntity)
                .collect(toSet());
    }

    @SneakyThrows
    public List<OperationalReport> getReportsForSowingCampaignFilling(FillingMainFormRequestDto dto) {
        GetAttributesListRequestDto createDto = buildGetReportsForFillingMainFormRequestDto(dto.date());
        String ss = objectMapper.writeValueAsString(createDto);
        log.debug("Reports Getting JSON [{}]", ss);
        List<InstanceDto> dtoList = apkRestClient.getTableAttributesList(createDto);
        String s = objectMapper.writeValueAsString(dtoList);
        log.debug("Reports Received JSON [{}]", s);
        return dtoList.stream()
            .map(orMapper::toEntity)
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

    public CashPlanLimit updateCashPlanLimit(CashPlanLimit updatedCpl) {
        UpdateInstanceRequestDto updateDto = cplMapper.toUpdateDto(updatedCpl);
        InstanceDto updatedInstance = apkRestClient.updateInstance(updateDto);
        return cplMapper.toCpl(updatedInstance);
    }

    @SneakyThrows
    public CropProductionMainForm updateCropProductionMainForm(CropProductionMainForm updatedMainForm) {
        UpdateInstanceRequestDto dto = cpmfMapper.toUpdateDto(updatedMainForm);
        String updatingJson = objectMapper.writeValueAsString(dto);
        log.debug("Crop Production Main Form updating JSON: [{}]", updatingJson);
        InstanceDto instanceDto = apkRestClient.updateInstance(dto);
        String updatedJson = objectMapper.writeValueAsString(instanceDto);
        log.debug("Updated Crop Production Main Form JSON [{}]", updatedJson);
        return cpmfMapper.toEntity(instanceDto);
    }

    private Map<Long, String> getCodesByType(CodeType codeType) {
        log.debug("Receiving codes for type {}", codeType.name());
        List<InstanceDto> list = apkRestClient.getTableAttributesList(
                GetAttributesListRequestDto.builder()
                        .templateId(codeType.getTemplateId())
                        .build());
        return list.stream()
                .filter(dto -> dto.shortForm() != null)
                .collect(toMap(
                        InstanceDto::id,
                        InstanceDto::shortForm
                ));
    }
}
