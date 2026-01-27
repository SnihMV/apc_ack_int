package su.petrosoft.apk_ack_integration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.mapper.CofinancingLevelMapper;
import su.petrosoft.apk_ack_integration.mapper.CropProductionMainFormMapper;
import su.petrosoft.apk_ack_integration.mapper.FinancingSourceMapper;
import su.petrosoft.apk_ack_integration.mapper.OperationalReportMapper;
import su.petrosoft.apk_ack_integration.mapper.SubsidyProgramMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.CofinancingLevel;
import su.petrosoft.apk_ack_integration.model.CropProductionMainForm;
import su.petrosoft.apk_ack_integration.model.FinancingSource;
import su.petrosoft.apk_ack_integration.model.OperationalReport;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.RequestedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;
import su.petrosoft.apk_ack_integration.model.enums.ReportType;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static su.petrosoft.apk_ack_integration.util.OperationalReportUtil.buildGettingOperationalReportsRequestDto;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractData;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApkPlicanteService {

    private final PlicanteRestClient apkRestClient;
    private final CashPlanLimitMapper cplMapper;
    private final SubsidyProgramMapper spMapper;
    private final FinancingSourceMapper fsMapper;
    private final CropProductionMainFormMapper cpmfMapper;
    private final OperationalReportMapper orMapper;
    private final CofinancingLevelMapper cflMapper;
    private final ObjectMapper objectMapper;

    public Set<CashPlanLimit> findCashPlanLimits(GetAttributesListRequestDto requestDto) {
        List<InstanceDto> dtoList = apkRestClient.getTableAttributesList(requestDto);
        return dtoList.stream()
                .map(cplMapper::toEntity)
                .collect(toSet());
    }

    @SneakyThrows
    public Set<SubsidyProgram> findSubsidyPrograms(GetAttributesListRequestDto requestDto) {
        String ss = objectMapper.writeValueAsString(requestDto);
        log.debug("Subsidy_Program Getting Request JSON [{}]", ss);
        List<InstanceDto> dtoList = apkRestClient.getTableAttributesList(requestDto);
        String s = objectMapper.writeValueAsString(dtoList);
        log.debug("Subsidy_Program Getting Response JSON [{}]", s);
        return dtoList.stream()
                .map(spMapper::toEntity)
                .collect(toSet());
    }

    public Set<FinancingSource> findFinancingSources(GetAttributesListRequestDto requestDto) {
        List<InstanceDto> dtoList = apkRestClient.getTableAttributesList(requestDto);
        log.info("Received [{}] Financing Sources in DB", dtoList.size());
        return dtoList.stream()
                .map(fsMapper::toEntity)
                .collect(toSet());
    }

    @SneakyThrows
    public List<OperationalReport> getOperationalReports(ReportType reportType, long date) {
        GetAttributesListRequestDto createDto = buildGettingOperationalReportsRequestDto(reportType, date);
        String requestJson = objectMapper.writeValueAsString(createDto);
        log.debug("Operational Reports Getting JSON [{}]", requestJson);
        List<InstanceDto> dtoList = apkRestClient.getTableAttributesList(createDto);
        return dtoList.stream()
            .map(orMapper::toEntity)
            .toList();
    }

    public CashPlanLimit createCashPlanLimit(CashPlanLimit cpl, Map<Dictionary, Map<String, Long>> codesMap) {
        CreateInstanceRequestDto dto = cplMapper.toCreateDto(cpl, codesMap);
        InstanceDto instance = apkRestClient.createInstance(dto);
        return cplMapper.toEntity(instance);
    }

    public SubsidyProgram createSubsidyProgram(SubsidyProgram sp, Map<Dictionary, Map<String, Long>> codesMap) {
        CreateInstanceRequestDto dto = spMapper.toCreateDto(sp, codesMap);
        InstanceDto instance = apkRestClient.createInstance(dto);
        return spMapper.toEntity(instance);
    }

    @SneakyThrows
    public FinancingSource createFinancingSource(FinancingSource financingSource, Map<Dictionary, Map<String, Long>> codesMap) {
        CreateInstanceRequestDto createDto = fsMapper.toCreatingDto(financingSource, codesMap);
        String ss = objectMapper.writeValueAsString(createDto);
        log.debug("Financing_Source Creating Request body [{}]", ss);
        InstanceDto created = apkRestClient.createInstance(createDto);
        String s = objectMapper.writeValueAsString(created);
        log.debug("Financing_Source Creating Response body [{}]", s);
        return fsMapper.toEntity(created);
    }

    @SneakyThrows
    public CofinancingLevel createCofinancingLevel(CofinancingLevel cofinancingLevel, Map<Dictionary, Map<String, Long>> codesMap) {
        CreateInstanceRequestDto creatingDto = cflMapper.toCreatingDto(cofinancingLevel, codesMap);
        String creatingJSON = objectMapper.writeValueAsString(creatingDto);
        log.debug("Cofinancing Level Creating JSON [{}]", creatingJSON);
        InstanceDto created = apkRestClient.createInstance(creatingDto);
        String createdJSON = objectMapper.writeValueAsString(created);
        log.debug("Cofinancing Level Created JSON [{}]", createdJSON);
        return cflMapper.toEntity(created);
    }

    public CashPlanLimit updateCashPlanLimit(CashPlanLimit updatedCpl) {
        UpdateInstanceRequestDto updateDto = cplMapper.toUpdateDto(updatedCpl);
        InstanceDto updatedInstance = apkRestClient.updateInstance(updateDto);
        return cplMapper.toEntity(updatedInstance);
    }

    @SneakyThrows
    public CropProductionMainForm updateCropProductionMainForm(CropProductionMainForm mainForm) {
        UpdateInstanceRequestDto dto = cpmfMapper.toUpdateDto(mainForm);
        String updatingJson = objectMapper.writeValueAsString(dto);
        log.debug("Crop Production Main Form updating JSON: [{}]", updatingJson);

        InstanceDto instanceDto = apkRestClient.updateInstance(dto);
        return cpmfMapper.toEntity(instanceDto);
    }

    @SneakyThrows
    public SubsidyProgram updateSubsidyProgram(SubsidyProgram subsidyProgram) {
        UpdateInstanceRequestDto dto = spMapper.toUpdatingDto(subsidyProgram);
        String updatingJson = objectMapper.writeValueAsString(dto);
        log.debug("Subsidy Program updating JSON: [{}]", updatingJson);

        InstanceDto updatedSP = apkRestClient.updateInstance(dto);
        String updatedJson = objectMapper.writeValueAsString(dto);
        log.debug("Subsidy Program updated JSON: [{}]", updatedJson);
        return spMapper.toEntity(updatedSP);
    }

    public Map<Dictionary, Map<String, Long>> getCodesMap(Dictionary... types) {

        log.info("Receiving existed codes for types: {}", Arrays.stream(types).map(Enum::name).collect(joining(",")));
        Map<Dictionary, Map<String, Long>> codes = new EnumMap<>(Dictionary.class);
        for (Dictionary dictionary : types) {
            codes.put(dictionary, getCodesByType(dictionary));
            log.debug("{} code map:\n[{}]", dictionary.name(), codes.get(dictionary).toString());
        }
        long count = codes.values().stream()
                .flatMap(map -> map.entrySet().stream())
                .count();
        log.info("Extracted {} codes", count);
        return codes;
    }

    private Map<String, Long> getCodesByType(Dictionary dictionary) {
        List<InstanceDto> list = apkRestClient.getTableAttributesList(
                GetAttributesListRequestDto.builder()
                        .templateId(dictionary.getTemplateId())
                        .attributes(List.of(
                                new RequestedAttribute(dictionary.getCodeAttrId())))
                        .build());
        return list.stream()
                .collect(toMap(
                        dto -> extractData(dto.attributes(), dictionary.getCodeAttrId()),
                        InstanceDto::id,
                        (k1, k2) -> k1
                        ));
    }
}
