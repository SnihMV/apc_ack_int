package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;
import su.petrosoft.apk_ack_integration.mapper.*;
import su.petrosoft.apk_ack_integration.model.*;
import su.petrosoft.apk_ack_integration.model.dto.plicante.*;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.*;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractData;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlicanteInstanceService {

    private final PlicanteRestClient restClient;
    private final CashPlanLimitMapper cplMapper;
    private final SubsidyProgramMapper spMapper;
    private final FinancingSourceMapper fsMapper;
    private final CropProductionMainFormMapper cpmfMapper;
    private final OperationalReportMapper orMapper;
    private final CofinancingLevelMapper cflMapper;

    public <T extends PlicanteInstance> List<T> findAllInstances(
            GetAttributesListRequestDto requestDto,
            Function<InstanceDto, T> mapper
    ) {
        return findInstances(requestDto, mapper, t -> true);
    }

    public <T extends PlicanteInstance> List<T> findValidInstances(
            GetAttributesListRequestDto requestDto,
            Function<InstanceDto, T> mapper
    ) {
        return findInstances(requestDto, mapper, PlicanteInstance::isValid);
    }

    private <T extends PlicanteInstance> List<T> findInstances(
            GetAttributesListRequestDto dto,
            Function<InstanceDto, T> mapper,
            Predicate<T> filter
    ) {
        return restClient.getTableAttributesList(dto).stream()
                .map(mapper)
                .filter(filter)
                .toList();
    }

    public Set<CashPlanLimit> findCashPlanLimits(GetAttributesListRequestDto requestDto) {
        log.info("Getting Existing Cash_Plan_Limits ...");
        List<InstanceDto> dtoList = restClient.getTableAttributesList(requestDto);
        log.info("Found Cash_Plan_Limits count: [{}]", dtoList.size());
        return dtoList.stream()
            .map(cplMapper::toEntity)
            .collect(toSet());
    }

    public Set<SubsidyProgram> findSubsidyPrograms(
        GetAttributesListRequestDto requestDto
    ) {
        List<InstanceDto> dtoList = restClient.getTableAttributesList(requestDto);
        return dtoList.stream()
            .map(spMapper::toEntity)
            .collect(toSet());
    }

    public Set<FinancingSource> findFinancingSources(GetAttributesListRequestDto requestDto) {
        log.info("Getting Existing Financing_Sources ...");
        List<InstanceDto> dtoList = restClient.getTableAttributesList(requestDto);
        log.info("Found Financing_Sources count: [{}]", dtoList.size());
        return dtoList.stream()
            .map(fsMapper::toEntity)
            .collect(toSet());
    }

    public List<OperationalReport> getOperationalReports(GetAttributesListRequestDto requestDto) {
        log.info("Getting Operational_Reports ...");
        List<InstanceDto> dtoList = restClient.getTableAttributesList(requestDto);
        log.info("Found Operational_Reports count: [{}]", dtoList.size());
        return dtoList.stream()
                .map(orMapper::toEntity)
                .toList();
    }

    public CashPlanLimit createCashPlanLimit(CashPlanLimit cpl) {
        CreateInstanceRequestDto dto = cplMapper.toCreateDto(cpl);
        log.info("Creating new Cash_Plan_Limit ...");
        InstanceDto instance = restClient.createInstance(dto);
        log.info("Cash_Plan_Limit created with id [{}]", instance.id());
        return cplMapper.toEntity(instance);
    }

    public SubsidyProgram createSubsidyProgram(SubsidyProgram sp) {
        CreateInstanceRequestDto dto = spMapper.toCreateDto(sp);
        log.info("Creating [{}] level Subsidy_Program ...", sp.getLevel());
        InstanceDto instance = restClient.createInstance(dto);
        log.info("Created [{}] level Subsidy_Program with id [{}]", sp.getLevel(), instance.id());
        return spMapper.toEntity(instance);
    }

    public FinancingSource createFinancingSource(FinancingSource financingSource) {
        CreateInstanceRequestDto createDto = fsMapper.toCreatingDto(financingSource);
        log.info("Creating new Financing_Source ...");
        InstanceDto instance = restClient.createInstance(createDto);
        log.info("Financing_Source created with id [{}]", instance.id());
        return fsMapper.toEntity(instance);
    }

    public CofinancingLevel createCofinancingLevel(CofinancingLevel cofinancingLevel, Map<Dictionary, Map<DictionaryData, Long>> codesMap) {
        CreateInstanceRequestDto creatingDto = cflMapper.toCreatingDto(cofinancingLevel, codesMap);
        InstanceDto created = restClient.createInstance(creatingDto);
        return cflMapper.toEntity(created);
    }

    public long updateCashPlanLimit(CashPlanLimit updatedCpl) {
        UpdateInstanceRequestDto updateDto = cplMapper.toUpdateDto(updatedCpl);
        UpdateInstanceResponseDto updatedInstance = restClient.updateInstance(updateDto);
        return updatedInstance.id();
    }

    public long updateFinancingSource(FinancingSource updatedFs) {
        log.info("Updating Financing_Source [{}] ...", updatedFs.getId());
        UpdateInstanceRequestDto requestDto = fsMapper.toUpdateDto(updatedFs);
        UpdateInstanceResponseDto responseDto = restClient.updateInstance(requestDto);
        log.info("Financing_Source [{}] updated", responseDto.id());
        return responseDto.id();
    }

    public UpdateInstanceResponseDto updateCropProductionMainForm(CropProductionMainForm mainForm) {
        UpdateInstanceRequestDto requestDto = cpmfMapper.toUpdateDto(mainForm);
        UpdateInstanceResponseDto responseDto = restClient.updateInstance(requestDto);
        return responseDto;
    }

    public long updateSubsidyProgram(UpdateInstanceRequestDto dto) {
        log.info("Updating Subsidy_Program [{}] ...", dto.instance().id());
        UpdateInstanceResponseDto responseDto = restClient.updateInstance(dto);
        log.info("Subsidy_Program [{}] updated", responseDto.id());
        return responseDto.version();
    }

    public Map<Dictionary, Map<String, Long>> getDictionariesCodesMap(Set<Dictionary> dictionaries) {
        log.info("Receiving existing codes for types: {}...",
            dictionaries.stream().map(Enum::name).collect(joining(",")));
        Map<Dictionary, Map<String, Long>> dictionaryCodesMap = new EnumMap<>(Dictionary.class);
        for (Dictionary dictionary : dictionaries) {
            Map<String, Long> codesMap = dictionaryCodes(dictionary);
            dictionaryCodesMap.put(dictionary, codesMap);
            log.debug("Received {} codes map", dictionary.name());
        }
        long count = dictionaryCodesMap.values().stream()
            .mapToLong(Map::size)
            .sum();
        log.info("Received [{}] codes for all requested dictionaries", count);
        return dictionaryCodesMap;
    }

    public Map<Dictionary, Map<Long, Entry<String, String>>> getDictionariesNamedCodesMap(
        Set<Dictionary> dictionaries) {
        log.info("Receiving existing codes and descriptions for types: {}...",
            dictionaries.stream().map(Enum::name).collect(joining(",")));
        Map<Dictionary, Map<Long, Entry<String, String>>> codes = new EnumMap<>(Dictionary.class);
        for (Dictionary dictionary : dictionaries) {
            Map<Long, Entry<String, String>> codesMap = dictionaryNamedCodes(dictionary);
            codes.put(dictionary, codesMap);
            log.debug("Received {} codes and descriptions map:\n[{}]", dictionary.name(), codesMap);
        }
        long count = codes.values().stream()
            .mapToLong(Map::size)
            .sum();
        log.info("Received [{}] named codes for all requested dictionaries", count);
        return codes;
    }

    private Map<String, Long> dictionaryCodes(Dictionary dictionary) {
        List<InstanceDto> list = restClient.getTableAttributesList(
            requestDtoForGetDictionaryCodes(dictionary));
        return buildDictionaryCodesMap(dictionary, list);
    }

    private Map<Long, Entry<String, String>> dictionaryNamedCodes(Dictionary dictionary) {
        List<InstanceDto> list = restClient.getTableAttributesList(
            requestDtoForGettingDictionaryNamedCodes(dictionary));
        return buildDictionaryNamedCodesMap(dictionary, list);
    }

    private GetAttributesListRequestDto requestDtoForGetDictionaryCodes(Dictionary dictionary) {
        return GetAttributesListRequestDto.builder()
            .templateId(dictionary.getTemplateId())
            .attributes(List.of(
                new RequestedAttribute(dictionary.getCodeAttrId())
            ))
            .build();
    }

    private static GetAttributesListRequestDto requestDtoForGettingDictionaryNamedCodes(
        Dictionary dictionary) {
        return GetAttributesListRequestDto.builder()
            .templateId(dictionary.getTemplateId())
            .attributes(List.of(
                new RequestedAttribute(dictionary.getCodeAttrId()),
                new RequestedAttribute(dictionary.getDescriptionAttrId())
            ))
            .build();
    }

    private Map<String, Long> buildDictionaryCodesMap(Dictionary dictionary, List<InstanceDto> list) {
        return list.stream()
            .collect(toMap(
                dto -> extractData(dto.attributes(), dictionary.getCodeAttrId()),
                InstanceDto::id
            ));
    }

    private static Map<Long, Entry<String, String>> buildDictionaryNamedCodesMap(
        Dictionary dictionary,
        List<InstanceDto> list) {
        return list.stream()
            .collect(toMap(
                InstanceDto::id,
                dto -> Map.entry(
                    extractData(dto.attributes(), dictionary.getCodeAttrId()),
                    extractData(dto.attributes(), dictionary.getDescriptionAttrId())
                )));
    }

    public <T extends PlicanteInstance> T createInstance(
            CreateInstanceRequestDto requestDto,
            Function<InstanceDto, T> mapper
    ) {
        InstanceDto instanceDto = restClient.createInstance(requestDto);
        return mapper.apply(instanceDto);
    }
}
