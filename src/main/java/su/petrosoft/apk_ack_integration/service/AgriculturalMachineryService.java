package su.petrosoft.apk_ack_integration.service;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DISTRICT;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DIS_BEN_GEN;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.IZD_AVT_PR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KOM_KOR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KOM_ZER;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.MAS_KART;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.MAS_SH;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.MAS_ZH;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.MAS_ZH_PT_KOR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.OTHER_TECH;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.PROD_COUNTRY;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.TECH_FISHING;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.TECH_STATE;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.TR_V_M;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.values;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryReportUtil.JSON_FILE_ATTR;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryReportUtil.RECIPIENT_ID;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryReportUtil.requestDtoForGetReportById;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryReportUtil.requestDtoForUpdateReportByParks;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.FAILED_TO_PARSE_JSON_FILE;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.INSTANCE_NOT_FOUND_BY_ID;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.INVALID_FIELD_NAME;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.JSON_FIELDS_ABSENT;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.JSON_NODE_ABSENT;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.MANAGED_DICTIONARY_NOT_FOUND;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.REPORT_READING_PROBLEM;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.dictionaryIdByCode;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractAllData;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractData;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.DISTRICT_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.MACHINE_PARK_ATTR;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.TEMPLATE_ID;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.requestDtoForUpdateRecipientByParks;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.requestDtoToFindRecipientById;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;
import su.petrosoft.apk_ack_integration.client.PlicanteSoapClient;
import su.petrosoft.apk_ack_integration.exception.DictionaryException;
import su.petrosoft.apk_ack_integration.exception.EntityNotFoundException;
import su.petrosoft.apk_ack_integration.exception.JsonParsingException;
import su.petrosoft.apk_ack_integration.exception.MachineryParkReportException;
import su.petrosoft.apk_ack_integration.mapper.AgriculturalMachineryParkMapper;
import su.petrosoft.apk_ack_integration.model.AgriculturalMachineryPark;
import su.petrosoft.apk_ack_integration.model.AgriculturalMachineryReport;
import su.petrosoft.apk_ack_integration.model.SubsidyRecipient;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.AttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;
import su.petrosoft.apk_ack_integration.util.AgriculturalMachineryReportUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgriculturalMachineryService {

    private static final String VALUES_NODE_NAME = "data";
    private static final String VALUE_FIELD_PREFIX = "value_1_";
    private final PlicanteRestClient plicanteRestClient;
    private final ApkPlicanteService apkPlicanteService;
    private final AgriculturalMachineryParkMapper ampMapper;
    private final ObjectMapper objectMapper;
    private final PlicanteSoapClient plicanteSoapClient;

    public void processReport(Long id) {
        AgriculturalMachineryReport report = getAgriculturalMachineryReport(id);

        SubsidyRecipient recipient = findSubsidyRecipient(report.getRecipientId());

        List<AgriculturalMachineryPark> parksFromReport = createMachineryParks(report.getJsonReport(), recipient);
        Collection<Long> existedParkIds = recipient.getMachineParkIds();

        List<Long> savedParkIds = saveMachineryParks(parksFromReport);

        updateRecipient(recipient, savedParkIds);
        updateReport(report, savedParkIds);

        removeFormerRecipientParks(existedParkIds);

    }

    private void updateReport(AgriculturalMachineryReport report, List<Long> savedParkIds) {
        UpdateInstanceResponseDto updated = plicanteRestClient.updateInstance(requestDtoForUpdateReportByParks(report, savedParkIds));
        log.debug("Report [{}] updated", updated.id());
    }

    private void updateRecipient(SubsidyRecipient recipient, List<Long> savedIds) {
        UpdateInstanceResponseDto updated = plicanteRestClient.updateInstance(requestDtoForUpdateRecipientByParks(recipient, savedIds));
        log.debug("Recipient [{}] updated", updated.id());
    }

    private List<Long> saveMachineryParks(List<AgriculturalMachineryPark> parksFromReport) {
        List<Long> savedIds = new ArrayList<>();
        for (AgriculturalMachineryPark park : parksFromReport) {
            CreateInstanceRequestDto creationDto = ampMapper.toCreationDto(park);
            InstanceDto instance = plicanteRestClient.createInstance(creationDto);
            log.info("Park [{}] created", instance.id());
            savedIds.add(instance.id());
        }
        return savedIds;
    }

    private List<AgriculturalMachineryPark> createMachineryParks(String jsonReport, SubsidyRecipient recipient) {
        Map<Integer, List<String>> groupedValues = parseJsonReport(jsonReport);
        log.debug("Extracted value-fields from JSON: \n{}", groupedValues);

        Map<Dictionary, Map<String, Long>> codesMap = apkPlicanteService.getDictionariesCodesMap(
                Set.of(DISTRICT, TR_V_M, KOM_ZER, KOM_KOR, MAS_SH, MAS_ZH, MAS_ZH_PT_KOR, DIS_BEN_GEN,
                        MAS_KART, IZD_AVT_PR, TECH_FISHING, OTHER_TECH, PROD_COUNTRY, TECH_STATE));

        List<AgriculturalMachineryPark> result = new ArrayList<>();

        for (Map.Entry<Integer, List<String>> entry : groupedValues.entrySet()) {
            List<String> fields = entry.getValue();
            if (fields == null || fields.isEmpty()) {
                continue;
            }
            try {
                AgriculturalMachineryPark machineryPark = createParkFromFieldList(fields, codesMap);
                machineryPark.setRecipientId(recipient.getId());
                machineryPark.setDistrictId(recipient.getDistrictId());
                result.add(machineryPark);
            } catch (RuntimeException e) {
                throw new MachineryParkReportException(REPORT_READING_PROBLEM.formatted(entry.getKey(), e.getMessage()));
            }
        }
        return result;
    }

    private SubsidyRecipient findSubsidyRecipient(Long recipientId) {
        List<InstanceDto> instanceDtoList =
                plicanteRestClient.getTableAttributesList(requestDtoToFindRecipientById(recipientId));
        if (instanceDtoList == null || instanceDtoList.isEmpty()) {
            throw new EntityNotFoundException(
                    INSTANCE_NOT_FOUND_BY_ID.formatted(recipientId, TEMPLATE_ID));
        }
        InstanceDto instanceDto = instanceDtoList.get(0);
        Long districtId = extractData(instanceDto.attributeDtos(), DISTRICT_ATTR);
        Collection<Long> parkIds = extractAllData(instanceDto.attributeDtos(), MACHINE_PARK_ATTR);
        return SubsidyRecipient.builder()
                .id(instanceDto.id())
                .version(instanceDto.version())
                .districtId(districtId)
                .machineParkIds(parkIds)
                .build();
    }

    private AgriculturalMachineryReport getAgriculturalMachineryReport(Long id) {

        List<InstanceDto> dtoList = plicanteRestClient.getTableAttributesList(
                requestDtoForGetReportById(id));
        if (dtoList == null || dtoList.isEmpty()) {
            throw new EntityNotFoundException(INSTANCE_NOT_FOUND_BY_ID.formatted(id,
                    AgriculturalMachineryReportUtil.TEMPLATE_ID));
        }
        InstanceDto foundInstance = dtoList.get(0);
        Long reportVersion = foundInstance.version();
        List<AttributeDto<?>> attributeDtos = foundInstance.attributeDtos();
        Long recipientId = extractData(attributeDtos, RECIPIENT_ID);
        log.debug("Recipient id [{}]", recipientId);

        String codedJsonFile = extractData(attributeDtos, JSON_FILE_ATTR);
        String jsonReport = new String(Base64.getDecoder().decode(codedJsonFile),
                StandardCharsets.UTF_8);

        return AgriculturalMachineryReport.builder()
                .id(id)
                .version(reportVersion)
                .recipientId(recipientId)
                .jsonReport(jsonReport)
                .build();
    }

    private void removeFormerRecipientParks(Collection<Long> parkIds) {

        plicanteSoapClient.deleteInstancesList(parkIds);

        log.info("Machinery Park Instances [{}] deleted", parkIds);
    }

    private Map<Integer, List<String>> parseJsonReport(String jsonReport) {
        try {
            JsonNode root = objectMapper.readTree(jsonReport);
            JsonNode dataNode = root.path("data");

            if (dataNode.isMissingNode()) {
                throw new JsonParsingException(JSON_NODE_ABSENT.formatted(VALUES_NODE_NAME));
            }
            Map<Integer, List<String>> result = new TreeMap<>();

            Set<Map.Entry<String, JsonNode>> fields = dataNode.properties();
            for (Map.Entry<String, JsonNode> field : fields) {
                fillResultMap(field, result);
            }
            if (result.isEmpty()) {
                throw new JsonParsingException(JSON_FIELDS_ABSENT.formatted(VALUE_FIELD_PREFIX));
            }
            return result;
        } catch (Exception e) {
            throw new MachineryParkReportException(
                    FAILED_TO_PARSE_JSON_FILE.formatted(e.getMessage()));
        }
    }

    private static void fillResultMap(Entry<String, JsonNode> field,
                                      Map<Integer, List<String>> result) {
        String fieldName = field.getKey();

        if (fieldName.startsWith(VALUE_FIELD_PREFIX)) {
            String[] parts = fieldName.split("_");
            if (parts.length == 4) {
                try {
                    int objectNumber = Integer.parseInt(parts[2]);
                    int fieldIndex = Integer.parseInt(parts[3]);
                    String fieldValue = field.getValue().asText("");

                    List<String> objectFields = result.computeIfAbsent(
                            objectNumber,
                            k -> new ArrayList<>(Collections.nCopies(12, null))
                    );
                    objectFields.set(fieldIndex - 1, fieldValue);
                } catch (NumberFormatException ignore) {
                    throw new JsonParsingException(INVALID_FIELD_NAME.formatted(fieldName));
                }
            } else {
                throw new JsonParsingException(INVALID_FIELD_NAME.formatted(fieldName));
            }
        }
    }

    private AgriculturalMachineryPark createParkFromFieldList(List<String> fields, Map<Dictionary, Map<String, Long>> codesMap) {
        Map<String, Long> indicateMap = Map.of(
                "Тракторы всех марок", 537L,
                "Комбайны зерноуборочные", 538L,
                "Комбайны кормоуборочные", 539L,
                "Машины сельскохозяйственные", 540L,
                "Машины для животноводства, птицеводства и кормопроизводства", 541L,
                "Дизельные и бензиновые генераторы для резервного питания", 542L,
                "Машины для производства картофеля, овощей, плодов и ягод", 543L,
                "Изделия автомобильной промышленности", 544L,
                "Техника и оборудование для рыбоводства и рыболовства", 545L,
                "Прочая техника и оборудование", 546L
        );
        return AgriculturalMachineryPark.builder()
                .indicator(indicateMap.get(getField(fields, 0)))
                .machineryAndEquip(dictionaryIdByCode(codesMap, getType(fields.get(0)), getField(fields, 1)))
                .brandModel(getField(fields, 2))
                .serialNumber(getField(fields, 3))
                .count(parseLong(getField(fields, 4)))
                .power(parseBigDecimal(getField(fields, 5)))
                .cost(parseBigDecimal(getField(fields, 6)))
                .productionCountry(dictionaryIdByCode(codesMap, PROD_COUNTRY, getField(fields, 7)))
                .productionYear(parseLong(getField(fields, 8)))
                .stateSupport(parseBoolean(getField(fields, 9)))
                .techState(dictionaryIdByCode(codesMap, TECH_STATE, getField(fields, 10)))
                .build();
    }


    /**
     * Вспомогательный метод для безопасного получения поля из списка
     */
    private String getField(List<String> fields, int index) {
        if (fields == null || index < 0 || index >= fields.size()) {
            return null;
        }
        return fields.get(index);
    }

    /**
     * Парсинг Long с обработкой ошибок
     */
    private Long parseLong(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Парсинг BigDecimal с обработкой ошибок
     */
    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Парсинг Boolean для поля stateSupport
     */
    private Boolean parseBoolean(String value) {
        if (value == null) {
            return true;
        }
        String trimmed = value.trim();
        return "Да".equalsIgnoreCase(trimmed) ||
                "true".equalsIgnoreCase(trimmed) ||
                "1".equals(trimmed);
    }

    private static Dictionary getType(String indicator) {
        return Arrays.stream(values())
                .filter(type -> type.getName().equalsIgnoreCase(indicator))
                .findFirst()
                .orElseThrow(() -> new DictionaryException(MANAGED_DICTIONARY_NOT_FOUND.formatted(indicator)));
    }

}
