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
import su.petrosoft.apk_ack_integration.model.SubsidyRecipient;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.RequestedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
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

    public Set<CashPlanLimit> findCashPlanLimits(
            GetAttributesListRequestDto requestDto,
            Map<Dictionary, Map<Long, Entry<String, String>>> codesMap
    ) {
        log.info("Getting Cash_Plan_Limits ...");
        List<InstanceDto> dtoList = apkRestClient.getTableAttributesList(requestDto);
        log.info("Found Cash_Plan_Limits count: [{}]", dtoList.size());
        return dtoList.stream()
                .map(dto -> cplMapper.toEntity(dto, codesMap))
                .collect(toSet());
    }

    public Set<SubsidyProgram> findSubsidyPrograms(
            GetAttributesListRequestDto requestDto,
            Map<Dictionary, Map<Long, Entry<String, String>>> codesMap
            ) {
        List<InstanceDto> dtoList = apkRestClient.getTableAttributesList(requestDto);
        return dtoList.stream()
                .map(dto -> spMapper.toEntity(dto, codesMap))
                .collect(toSet());
    }

    public Set<FinancingSource> findFinancingSources(GetAttributesListRequestDto requestDto, Map<Dictionary, Map<Long, Entry<String, String>>> codesMap) {
        log.info("Getting Financing_Sources ...");
        List<InstanceDto> dtoList = apkRestClient.getTableAttributesList(requestDto);
        log.info("Found Financing_Sources count: [{}]", dtoList.size());
        return dtoList.stream()
                .map(dto -> fsMapper.toEntity(dto, codesMap))
                .collect(toSet());
    }

    public List<OperationalReport> getOperationalReports(GetAttributesListRequestDto requestDto) {
        log.info("Getting Operational_Reports ...");
        List<InstanceDto> dtoList = apkRestClient.getTableAttributesList(requestDto);
        log.info("Found Operational_Reports count: [{}]", dtoList.size());
        return dtoList.stream()
                .map(orMapper::toEntity)
                .toList();
    }

    public CashPlanLimit createCashPlanLimit(CashPlanLimit cpl, Map<Dictionary, Map<Long, Entry<String, String>>> codesMap) {
        CreateInstanceRequestDto dto = cplMapper.toCreateDto(cpl, codesMap);
        log.info("Creating new Cash_Plan_Limit ...");
        InstanceDto instance = apkRestClient.createInstance(dto);
        log.info("Cash_Plan_Limit created with id [{}]", instance.id());
        return cplMapper.toEntity(instance, codesMap);
    }

    public SubsidyProgram createSubsidyProgram(SubsidyProgram sp, Map<Dictionary, Map<Long, Entry<String, String>>> codesMap) {
        CreateInstanceRequestDto dto = spMapper.toCreateDto(sp, codesMap);
        log.info("Creating [{}] level Subsidy_Program ...", sp.getLevel());
        InstanceDto instance = apkRestClient.createInstance(dto);
        log.info("Created [{}] level Subsidy_Program with id [{}]", sp.getLevel(), instance.id());
        return spMapper.toEntity(instance, codesMap);
    }

    public FinancingSource createFinancingSource(FinancingSource financingSource, Map<Dictionary, Map<Long, Entry<String, String>>> codesMap) {
        CreateInstanceRequestDto createDto = fsMapper.toCreatingDto(financingSource, codesMap);
        log.info("Creating new Financing_Source ...");
        InstanceDto instance = apkRestClient.createInstance(createDto);
        log.info("Financing_Source created with id [{}]", instance.id());
        return fsMapper.toEntity(instance, codesMap);
    }

    public CofinancingLevel createCofinancingLevel(CofinancingLevel cofinancingLevel, Map<Dictionary, Map<Long, Entry<String, String>>> codesMap) {
        CreateInstanceRequestDto creatingDto = cflMapper.toCreatingDto(cofinancingLevel, codesMap);
        InstanceDto created = apkRestClient.createInstance(creatingDto);
        return cflMapper.toEntity(created, codesMap);
    }

    public long updateCashPlanLimit(CashPlanLimit updatedCpl, Map<Dictionary, Map<Long, Entry<String, String>>> codesMap) {
        UpdateInstanceRequestDto updateDto = cplMapper.toUpdateDto(updatedCpl);
        UpdateInstanceResponseDto updatedInstance = apkRestClient.updateInstance(updateDto);
        return updatedInstance.id();
    }

    public long updateFinancingSource(FinancingSource updatedFs, Map<Dictionary, Map<Long, Entry<String, String>>> codesMap) {
        log.info("Updating Financing_Source [{}] ...", updatedFs.getId());
        UpdateInstanceRequestDto requestDto = fsMapper.toUpdateDto(updatedFs);
        UpdateInstanceResponseDto responseDto = apkRestClient.updateInstance(requestDto);
        log.info("Financing_Source [{}] updated", responseDto.id());
        return responseDto.id();
    }

    public UpdateInstanceResponseDto updateCropProductionMainForm(CropProductionMainForm mainForm) {
        UpdateInstanceRequestDto requestDto = cpmfMapper.toUpdateDto(mainForm);
        UpdateInstanceResponseDto responseDto = apkRestClient.updateInstance(requestDto);
        return responseDto;
    }

    public UpdateInstanceResponseDto updateSubsidyProgram(SubsidyProgram subsidyProgram, Map<Dictionary, Map<Long, Entry<String, String>>> codesMap) {
        UpdateInstanceRequestDto dto = spMapper.toUpdatingDto(subsidyProgram);
        UpdateInstanceResponseDto responseDto = apkRestClient.updateInstance(dto);
        return responseDto;
    }

    public Map<Dictionary, Map<Long, Entry<String, String>>> getDictionariesCodesMap(Set<Dictionary> dictionaries) {
        log.info("Receiving existing codes for types: {}...",
                dictionaries.stream().map(Enum::name).collect(joining(",")));
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
