package su.petrosoft.apk_ack_integration.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.ApkPlicanteRestClient;
import su.petrosoft.apk_ack_integration.mapper.AgriculturalMachineryParkMapper;
import su.petrosoft.apk_ack_integration.model.AgriculturalMachineryPark;
import su.petrosoft.apk_ack_integration.model.AgriculturalMachineryReport;
import su.petrosoft.apk_ack_integration.model.SubsidyRecipient;
import su.petrosoft.apk_ack_integration.model.dto.plicante.ChangeGroupStatusRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.LinkedValue;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil;
import su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static su.petrosoft.apk_ack_integration.model.enums.CodeType.DIS_BEN_GEN;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.IZD_AVT_PR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KOM_KOR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KOM_ZER;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.MAS_KART;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.MAS_SH;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.MAS_ZH;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.MAS_ZH_PT_KOR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.OTHER_TECH;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.PROD_COUNTRY;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.TECH_FISHING;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.TECH_STATE;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.TR_V_M;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryParkUtil.STATUS_INACTIVE;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryParkUtil.buildRequestDtoToFindMachineryParkByRecipientId;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryReportUtil.JSON_FILE_ATTR;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryReportUtil.RECIPIENT_ID;
import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryReportUtil.buildRequestDtoForReportProcessing;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgriculturalMachineryService {
    private final ApkPlicanteRestClient plicanteRestClient;
    private final ApkPlicanteService apkPlicanteService;
    //    private final AgriculturalMachineryReportMapper amrMapper;
    private final AgriculturalMachineryParkMapper ampMapper;
    private final ObjectMapper objectMapper;


    public void processReport(Long id) {
        AgriculturalMachineryReport report = getAgriculturalMachineryReport(id);
        removeRecipientParks(report.getRecipientId());
        byte[] rawReport = Base64.getDecoder().decode(report.getCodedReport());
        try {
            List<AgriculturalMachineryPark> agriculturalMachineryParks =
                    parseJsonAndCreateObjects(id, report.getRecipientId(), rawReport);
            log.debug("===");
            agriculturalMachineryParks.stream()
                    .peek(park -> park.setRecipientId(report.getRecipientId()))
                    .forEach(park -> log.debug("Park [{}]", park));

            Map<CodeType, Map<Long, String>> codesMap = apkPlicanteService.getCodesMap(
                    TR_V_M, KOM_ZER, KOM_KOR, MAS_SH, MAS_ZH, MAS_ZH_PT_KOR, DIS_BEN_GEN, MAS_KART, IZD_AVT_PR, TECH_FISHING, OTHER_TECH, PROD_COUNTRY, TECH_STATE);
            List<Long> savedIds = new ArrayList<>();
            for (AgriculturalMachineryPark park : agriculturalMachineryParks) {
                CreateInstanceRequestDto creationDto = ampMapper.toCreationDto(park, codesMap);
                String s = objectMapper.writeValueAsString(creationDto);
                log.debug("=== JSON === {}", s);
                InstanceDto instance = plicanteRestClient.createInstance(creationDto);
                log.debug("Park [{}] been created", instance.id());
                savedIds.add(instance.id());
            }
            List<LinkedValue> linkedValues = savedIds.stream()
                    .map(LinkedValue::new)
                    .toList();

            GetAttributesListRequestDto dto = SubsidyRecipientUtil.buildRequestDtoToFindById(report.getRecipientId());
            String s = objectMapper.writeValueAsString(dto);
            log.debug("===Getting JSON [{}]", s);

            List<InstanceDto> instanceDtoList = plicanteRestClient.getTableAttributesList(dto);
            SubsidyRecipient recipient = SubsidyRecipient.builder()
                    .id(instanceDtoList.get(0).id())
                    .version(instanceDtoList.get(0).version())
                    .build();

            UpdateInstanceRequestDto updatingDto = new UpdateInstanceRequestDto(
                    InstanceDto.builder()
                            .id(recipient.getId())
                            .templateId(SubsidyRecipientUtil.TEMPLATE_ID)
                            .version(recipient.getVersion())
                            .attributes(List.of(
                                    new LinkedAttribute(SubsidyRecipientUtil.MACHINE_PARK_ATTR, linkedValues)
                            ))
                            .build());
            String s1 = objectMapper.writeValueAsString(updatingDto);
            log.debug("===Updating JSON {}", s1);
            InstanceDto updated = plicanteRestClient.updateInstance(updatingDto);
            log.debug("Updated Recipient: [{}]", updated);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<AgriculturalMachineryPark> parseJsonAndCreateObjects(Long id, Long recipientId, byte[] rawReport) throws Exception {
        Map<Integer, List<String>> groupedData = parseValueFields(rawReport);
        log.debug("=== {}", groupedData);

        // 2. Преобразуем Map в список объектов
        return createObjectsFromMap(groupedData);
    }

    /**
     * Шаг 1: Парсим поле data и группируем значения value_x_y_z по объектам (y)
     */
    private Map<Integer, List<String>> parseValueFields(byte[] rawReport) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(rawReport);
        JsonNode dataNode = root.path("data");

        if (dataNode.isMissingNode()) {
            throw new IllegalArgumentException("Поле 'data' не найдено в JSON");
        }

        // Создаем Map, где ключ - номер объекта (y), значение - список полей
        Map<Integer, List<String>> result = new TreeMap<>();

        // Инициализируем списки для каждого объекта (максимум 11 полей на объект)
        // Из JSON видно, что z идет от 1 до 11
        Iterator<Map.Entry<String, JsonNode>> fields = dataNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String fieldName = entry.getKey();

            if (fieldName.startsWith("value_1_")) {
                String[] parts = fieldName.split("_");
                if (parts.length == 4) {
                    try {
                        int objectNumber = Integer.parseInt(parts[2]); // y
                        int fieldIndex = Integer.parseInt(parts[3]);  // z
                        String fieldValue = entry.getValue().asText("");

                        // Инициализируем список для объекта, если его еще нет
                        List<String> objectFields = result.computeIfAbsent(
                                objectNumber,
                                k -> new ArrayList<>(Collections.nCopies(12, null)) // индексы 1-11
                        );

                        // Увеличиваем список при необходимости
                        while (objectFields.size() <= fieldIndex) {
                            objectFields.add(null);
                        }

                        // Сохраняем значение по индексу z
                        objectFields.set(fieldIndex, fieldValue);

                    } catch (NumberFormatException e) {
                        // Игнорируем некорректные поля
                    }
                }
            }
        }

        return result;
    }

    /**
     * Шаг 2: Создаем объекты из сгруппированных данных
     */
    private List<AgriculturalMachineryPark> createObjectsFromMap(Map<Integer, List<String>> groupedData) {
        List<AgriculturalMachineryPark> result = new ArrayList<>();

        for (Map.Entry<Integer, List<String>> entry : groupedData.entrySet()) {
            Integer objectNumber = entry.getKey();
            List<String> fields = entry.getValue();

            if (fields == null || fields.isEmpty()) {
                continue;
            }

            AgriculturalMachineryPark machine = createPark(fields);
            result.add(machine);
        }
        return result;
    }

    /**
     * Создаем один объект из списка полей
     */
    private AgriculturalMachineryPark createPark(List<String> fields) {
        return AgriculturalMachineryPark.builder()
                .indicator(getField(fields, 1))
                .machineryAndEquip(getField(fields, 2))
                .brandModel(getField(fields, 3))
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

//    private Map<String, Object> createNewParks(String codedReport) {
//        byte[] rawReport = Base64.getDecoder().decode(codedReport);
//        JsonNode root = null;
//        try {
//            root = objectMapper.readTree(rawReport);
//            JsonNode dataNode = root.path("data");
//            if (dataNode.isMissingNode() || !dataNode.isObject()) {
//                return Map.of();
//            }
//
//            dataNode.properties().forEach(
//                    entry-> {
//                        String key = entry.getKey();
//                        JsonNode valueNode = entry.getValue();
//                    }
//            );
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @SneakyThrows
    private AgriculturalMachineryReport getAgriculturalMachineryReport(Long id) {
        List<Attribute<?>> attributes = plicanteRestClient.getInstanceRepresentation(
                buildRequestDtoForReportProcessing(id));

        String json = objectMapper.writeValueAsString(attributes);
        log.debug("Get instance [{}] attributes response json [{}]", id, json);

        Long recipientId = PlicanteInstanceUtil.extractData(attributes, RECIPIENT_ID);
        log.debug("Recipient id [{}]", recipientId);

        String codedJsonFile = PlicanteInstanceUtil.extractData(attributes, JSON_FILE_ATTR);

        return AgriculturalMachineryReport.builder()
                .id(id)
                .recipientId(recipientId)
                .codedReport(codedJsonFile)
                .build();
    }

    private void removeRecipientParks(Long recipientId) {
        List<InstanceDto> instanceDtoList = plicanteRestClient.getTableAttributesList(
                buildRequestDtoToFindMachineryParkByRecipientId(recipientId));
        log.debug("Instances dto: [{}]", instanceDtoList);

        List<Long> parkIds = instanceDtoList.stream()
                .map(InstanceDto::id)
                .toList();

        plicanteRestClient.changeStatus(new ChangeGroupStatusRequestDto(STATUS_INACTIVE, parkIds));
        log.debug("Status changed for instances: [{}]", parkIds);
    }
}
