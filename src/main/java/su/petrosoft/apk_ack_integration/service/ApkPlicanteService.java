package su.petrosoft.apk_ack_integration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
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

    public List<SubsidyProgram> findSubsidyPrograms(GetAttributesListRequestDto requestDto) {
        List<InstanceDto> dtoList = apkRestClient.getTableAttributesList(requestDto);
        return dtoList.stream()
                .map(spMapper::toEntity)
                .toList();
    }

    public Set<FinancingSource> findFinancingSources(GetAttributesListRequestDto requestDto, Map<Dictionary, Map<String, Long>> codesMap) {
        List<InstanceDto> dtoList = apkRestClient.getTableAttributesList(requestDto);
        return dtoList.stream()
                .map(dto->fsMapper.toEntity(dto, codesMap))
                .collect(toSet());
    }

    public List<OperationalReport> getOperationalReports(ReportType reportType, long date) {
        GetAttributesListRequestDto createDto = buildGettingOperationalReportsRequestDto(reportType, date);
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

    public FinancingSource createFinancingSource(FinancingSource financingSource, Map<Dictionary, Map<String, Long>> codesMap) {
        CreateInstanceRequestDto createDto = fsMapper.toCreatingDto(financingSource, codesMap);
        InstanceDto created = apkRestClient.createInstance(createDto);
        return fsMapper.toEntity(created, codesMap);
    }

    public CofinancingLevel createCofinancingLevel(CofinancingLevel cofinancingLevel, Map<Dictionary, Map<String, Long>> codesMap) {
        CreateInstanceRequestDto creatingDto = cflMapper.toCreatingDto(cofinancingLevel, codesMap);
        InstanceDto created = apkRestClient.createInstance(creatingDto);
        return cflMapper.toEntity(created, codesMap);
    }

    public CashPlanLimit updateCashPlanLimit(CashPlanLimit updatedCpl) {
        UpdateInstanceRequestDto updateDto = cplMapper.toUpdateDto(updatedCpl);
        InstanceDto updatedInstance = apkRestClient.updateInstance(updateDto);
        return cplMapper.toEntity(updatedInstance);
    }

    public CropProductionMainForm updateCropProductionMainForm(CropProductionMainForm mainForm) {
        UpdateInstanceRequestDto dto = cpmfMapper.toUpdateDto(mainForm);
        InstanceDto instanceDto = apkRestClient.updateInstance(dto);
        return cpmfMapper.toEntity(instanceDto);
    }

    public SubsidyProgram updateSubsidyProgram(SubsidyProgram subsidyProgram) {
        UpdateInstanceRequestDto dto = spMapper.toUpdatingDto(subsidyProgram);
        InstanceDto updatedSP = apkRestClient.updateInstance(dto);
        return spMapper.toEntity(updatedSP);
    }

    public Map<Dictionary, Map<Long, Entry<String, String>>> getDictionariesCodesMap(Dictionary... dictionaries) {
        log.info("Receiving existing codes for types: {}...",
                Arrays.stream(dictionaries).map(Enum::name).collect(joining(",")));
        Map<Dictionary, Map<Long, Entry<String, String>>> codes = new EnumMap<>(Dictionary.class);
        for (Dictionary dictionary : dictionaries) {
            Map<Long, Entry<String, String>> codesMap = dictionaryCodesMap(dictionary);
            codes.put(dictionary, codesMap);
            log.debug("Received {} codes map:\n[{}]", dictionary.name(), codesMap);
        }
        long count = codes.values().stream()
                .mapToLong(Map::size)
                .sum();
        log.info("Received [{}] codes for all requested dictionaries", count);
        return codes;
    }

    private Map<Long, Entry<String, String>> dictionaryCodesMap(Dictionary dictionary) {
        List<InstanceDto> list = apkRestClient.getTableAttributesList(gettingAllDictionaryCodesRequestDto(dictionary));
        return buildDictionaryCodesMap(dictionary, list);
    }

    private static GetAttributesListRequestDto gettingAllDictionaryCodesRequestDto(Dictionary dictionary) {
        return GetAttributesListRequestDto.builder()
                .templateId(dictionary.getTemplateId())
                .attributes(List.of(
                        new RequestedAttribute(dictionary.getCodeAttrId()),
                        new RequestedAttribute(dictionary.getDescriptionAttrId())
                ))
                .build();
    }

    private static Map<Long, Entry<String, String>> buildDictionaryCodesMap(Dictionary dictionary, List<InstanceDto> list) {
        return list.stream()
                .collect(toMap(
                    InstanceDto::id,
                    dto -> Map.entry(
                        extractData(dto.attributes(), dictionary.getCodeAttrId()),
                        extractData(dto.attributes(), dictionary.getDescriptionAttrId())
                )));
    }
}
