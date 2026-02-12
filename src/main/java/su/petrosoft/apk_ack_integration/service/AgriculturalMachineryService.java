package su.petrosoft.apk_ack_integration.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;
import su.petrosoft.apk_ack_integration.client.PlicanteSoapClient;
import su.petrosoft.apk_ack_integration.exception.EntityNotFoundException;
import su.petrosoft.apk_ack_integration.exception.JsonFileException;
import su.petrosoft.apk_ack_integration.mapper.AgriculturalMachineryParkMapper;
import su.petrosoft.apk_ack_integration.model.AgriculturalMachineryPark;
import su.petrosoft.apk_ack_integration.model.AgriculturalMachineryReport;
import su.petrosoft.apk_ack_integration.model.SubsidyRecipient;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryParkUtil.buildRequestDtoToFindMachineryParkByRecipientId;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryReportUtil.JSON_FILE_ATTR;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryReportUtil.RECIPIENT_ID;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryReportUtil.requestDtoForReportProcessing;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessage.FAILED_TO_READ_JSON_FILE;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessage.RECIPIENT_BY_ID_NOT_FOUND;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.*;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgriculturalMachineryService {

    private final PlicanteRestClient plicanteRestClient;
    private final ApkPlicanteService apkPlicanteService;
    private final AgriculturalMachineryParkMapper ampMapper;
    private final ObjectMapper objectMapper;
    private final PlicanteSoapClient plicanteSoapClient;

    public void processReport(Long id) {
        AgriculturalMachineryReport report = getAgriculturalMachineryReport(id);
        Long recipientId = report.getRecipientId();
        String jsonReport = report.getJsonReport();

        SubsidyRecipient recipient = findSubsidyRecipient(recipientId);
        Collection<Long> existedParkIds = recipient.getMachineParkIds();
        List<AgriculturalMachineryPark> parksFromReport = createMachineryParks(jsonReport, recipient);

        List<Long> savedIds = saveMachineryParks(parksFromReport);

        updateRecipient(recipient, savedIds);

        removeRecipientParks(existedParkIds);

    }

    private void updateRecipient(SubsidyRecipient recipient, List<Long> savedIds) {
        UpdateInstanceRequestDto updatingDto = new UpdateInstanceRequestDto(
            InstanceDto.builder()
                .id(recipient.getId())
                .templateId(TEMPLATE_ID)
                .version(recipient.getVersion())
                .attributes(List.of(
                    new LinkedAttribute(MACHINE_PARK_ATTR, savedIds)))
                .build());
        UpdateInstanceResponseDto updated = plicanteRestClient.updateInstance(updatingDto);
        log.debug("Recipient [{}] updated", updated.id());
    }

    private List<Long> saveMachineryParks(List<AgriculturalMachineryPark> parksFromReport) {

        Map<Dictionary, Map<Long, Entry<String, String>>> codesMap = apkPlicanteService.getDictionariesCodesMap(
            Set.of(DISTRICT, TR_V_M, KOM_ZER, KOM_KOR, MAS_SH, MAS_ZH, MAS_ZH_PT_KOR, DIS_BEN_GEN,
                MAS_KART, IZD_AVT_PR, TECH_FISHING, OTHER_TECH, PROD_COUNTRY, TECH_STATE));

        List<Long> savedIds = new ArrayList<>();

        for (AgriculturalMachineryPark park : parksFromReport) {
            CreateInstanceRequestDto creationDto = ampMapper.toCreationDto(park, codesMap);
            InstanceDto instance = plicanteRestClient.createInstance(creationDto);
            log.debug("Park [{}] created", instance.id());
            savedIds.add(instance.id());
        }
        return savedIds;
    }

    private List<AgriculturalMachineryPark> createMachineryParks(String jsonReport, SubsidyRecipient recipient) {
        Map<Integer, List<String>> groupedValues = parseJsonReport(jsonReport);
        log.debug("Extracted value-fields from JSON: \n{}", groupedValues);

        List<AgriculturalMachineryPark> result = new ArrayList<>();

        for (Map.Entry<Integer, List<String>> entry : groupedValues.entrySet()) {
            List<String> fields = entry.getValue();
            if (fields == null || fields.isEmpty()) {
                continue;
            }
            AgriculturalMachineryPark machineryPark = createParkFromFieldList(fields);
            machineryPark.setRecipientId(recipient.getId());
            machineryPark.setDistrictId(recipient.getDistrictId());
            result.add(machineryPark);
        }
        return result;
    }

    private SubsidyRecipient findSubsidyRecipient(Long recipientId) {
        List<InstanceDto> instanceDtoList =
            plicanteRestClient.getTableAttributesList(requestDtoToFindRecipientById(recipientId));
        if (instanceDtoList == null || instanceDtoList.isEmpty()) {
            throw new EntityNotFoundException(RECIPIENT_BY_ID_NOT_FOUND.getMessage().formatted(
                recipientId));
        }
        InstanceDto instanceDto = instanceDtoList.get(0);
        Long districtId = extractData(instanceDto.attributes(), DISTRICT_ATTR);
        Collection<Long> parkIds = extractAllData(instanceDto.attributes(), MACHINE_PARK_ATTR);
        return SubsidyRecipient.builder()
            .id(instanceDto.id())
            .version(instanceDto.version())
            .districtId(districtId)
            .machineParkIds(parkIds)
            .build();
    }

    private AgriculturalMachineryReport getAgriculturalMachineryReport(Long id) {
        List<Attribute<?>> attributes = plicanteRestClient.getInstanceRepresentation(
            requestDtoForReportProcessing(id));

        Long recipientId = extractData(attributes, RECIPIENT_ID);
        log.debug("Recipient id [{}]", recipientId);

        String codedJsonFile = extractData(attributes, JSON_FILE_ATTR);
        String jsonReport = new String(Base64.getDecoder().decode(codedJsonFile), StandardCharsets.UTF_8);

            return AgriculturalMachineryReport.builder()
                .id(id)
                .recipientId(recipientId)
                .jsonReport(jsonReport)
                .build();
    }

    private void removeRecipientParks(Collection<Long> parkIds) {

        plicanteSoapClient.deleteInstancesList(parkIds);

        log.info("Machinery Park Instances [{}] deleted", parkIds);
    }

    private List<Long> findExistingParks(long recipientId) {
        List<InstanceDto> instanceDtoList = plicanteRestClient.getTableAttributesList(
            buildRequestDtoToFindMachineryParkByRecipientId(recipientId));

        List<Long> parkIds = instanceDtoList.stream()
            .map(InstanceDto::id)
            .toList();
        log.debug("For Recipient [{}] found [{}] Machinery Park Instances: {}", recipientId,
            parkIds.size(), parkIds);
        return parkIds;
    }

    private Map<Integer, List<String>> parseJsonReport(String jsonReport) {
        try {
            JsonNode root = objectMapper.readTree(jsonReport);
            JsonNode dataNode = root.path("data");

            if (dataNode.isMissingNode()) {
                throw new IllegalArgumentException("Field \"data\" is absent");
            }
            Map<Integer, List<String>> result = new TreeMap<>();

            Set<Map.Entry<String, JsonNode>> fields = dataNode.properties();
            for (Map.Entry<String, JsonNode> entry : fields) {
                fillResultMap(entry, result);
            }
            if (result.isEmpty()) {
                throw new IllegalArgumentException("\"value_\" fields not found");
            }
            return result;
        } catch (Exception e) {
            throw new JsonFileException(FAILED_TO_READ_JSON_FILE.getMessage().formatted(e.getMessage()));
        }
    }

    private static void fillResultMap(Entry<String, JsonNode> entry,
        Map<Integer, List<String>> result) {
        String fieldName = entry.getKey();

        if (fieldName.startsWith("value_1_")) {
            String[] parts = fieldName.split("_");
            if (parts.length == 4) {
                try {
                    int objectNumber = Integer.parseInt(parts[2]);
                    int fieldIndex = Integer.parseInt(parts[3]);
                    String fieldValue = entry.getValue().asText("");

                    List<String> objectFields = result.computeIfAbsent(
                        objectNumber,
                        k -> new ArrayList<>(Collections.nCopies(12, null))
                    );
                    objectFields.set(fieldIndex - 1, fieldValue);
                } catch (NumberFormatException ignore) {
                    throw new JsonFileException(
                        "Cannot parse JSON file. Some kind of problem with [%s] field"
                            .formatted(fieldName));
                }
            }
        }
    }

    private AgriculturalMachineryPark createParkFromFieldList(List<String> fields) {
        return AgriculturalMachineryPark.builder()
            .indicator(getField(fields, 0))
            .machineryAndEquip(getField(fields, 1))
            .brandModel(getField(fields, 2))
            .serialNumber(getField(fields, 3))
            .count(parseLong(getField(fields, 4)))
            .power(parseBigDecimal(getField(fields, 5)))
            .cost(parseBigDecimal(getField(fields, 6)))
            .productionCountry(getField(fields, 7))
            .productionYear(parseLong(getField(fields, 8)))
            .stateSupport(parseBoolean(getField(fields, 9)))
            .techState(getField(fields, 10))
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

}
