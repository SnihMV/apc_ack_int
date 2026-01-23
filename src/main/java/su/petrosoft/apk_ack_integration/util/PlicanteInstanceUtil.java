package su.petrosoft.apk_ack_integration.util;

import lombok.extern.slf4j.Slf4j;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;
import su.petrosoft.apk_ack_integration.model.data.DictionaryExtractable;
import su.petrosoft.apk_ack_integration.model.Pair;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateInstancesFromFileResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class PlicanteInstanceUtil {

    public static final ZoneId MOSCOW_ZONE = ZoneId.of("Europe/Moscow");

    public static <T> CreateInstancesFromFileResponseDto getCreationInstancesFromFileResponse(
            List<?> dtoList,
            List<T> createdLimits,
            Function<T, Long> function
    ) {
        return new CreateInstancesFromFileResponseDto(
                dtoList.size(),
                createdLimits.size(),
                createdLimits.stream()
                        .map(function)
                        .toList());
    }

    public static CreateInstanceRequestDto requestDtoToSaveDictionaryInstance(Dictionary type, String code, String description) {
        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(type.getTemplateId())
                        .attributes(List.of(
                                new StringAttribute(type.getCodeAttrId(), code),
                                new StringAttribute(type.getDescriptionAttrId(), description)))
                .build());
    }

    public static void updateCodesMap(
            Map<Dictionary, Map<String, Long>> dictionaryCodesMap,
            List<? extends DictionaryExtractable> dictionaryExtractables,
            PlicanteRestClient restClient
    ) {
        for (Dictionary dictionary : dictionaryCodesMap.keySet()) {
            Map<String, Long> codesMap = dictionaryCodesMap.get(dictionary);
            for (DictionaryExtractable extractable : dictionaryExtractables) {
                String code = extractable.dictionaryCodes().get(dictionary);
                String description = extractable.dictionaryDescriptions().get(dictionary);
                codesMap.computeIfAbsent(code,
                        c -> {
                            Long createdId = restClient.createInstance(
                                            requestDtoToSaveDictionaryInstance(dictionary, code, description))
                                    .id();
                            log.info("Added new {} instance. Code: [{}] id: [{}]", dictionary, code, description);
                            return createdId;
                        });
            }
        }
    }

    public static Long getCodeId(Map<Dictionary, Map<String, Long>> allCodes, Dictionary type, String code) {
        if (code == null || code.isEmpty()) {
            return type.getDefaultValue();
        }
        return allCodes.get(type).entrySet().stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(code))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseThrow();
    }

    public static Long toEpochMilli(LocalDate day) {
        if (day == null) {
            return null;
        }
        return day.atStartOfDay(MOSCOW_ZONE)
                .toInstant()
                .toEpochMilli();
    }

    public static LocalDate toLocalDate(Long epochMilli) {
        if (epochMilli == null) {
            return null;
        }
        return Instant.ofEpochMilli(epochMilli)
                .atZone(MOSCOW_ZONE)
                .toLocalDate();
    }

    @SuppressWarnings("unchecked")
    public static <T> T extractData(List<Attribute<?>> attributes, long attributeId) {
        return findAttribute(attributes, attributeId)
                .map(attr -> (T) attr.getData())
                .orElse(null);
    }

    public static String extractShortForm(List<Attribute<?>> attributes, Long attributeId) {
        return findAttribute(attributes, attributeId)
                .map(Attribute::getShortForm)
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> extractAllData(List<Attribute<?>> attributes, long attributeId) {
        return findAttribute(attributes, attributeId)
                .map(attr -> (List<T>) attr.getAllData())
                .orElse(new ArrayList<>());
    }

    public static List<String> extractAllShortForms(List<Attribute<?>> attributes, long attributeId) {
        return findAttribute(attributes, attributeId)
                .map(Attribute::getAllShortForms)
                .orElse(Collections.emptyList());
    }

    public static Pair extractPair(List<Attribute<?>> attributes, long attributeId) {
        return findAttribute(attributes, attributeId)
                .map(Attribute::getPair)
                .orElse(null);
    }

    public static List<Pair> extractAllPairs(List<Attribute<?>> attributes, long attributeId) {
        return findAttribute(attributes, attributeId)
                .map(Attribute::getAllPairs)
                .orElse(Collections.emptyList());
    }

    private static Optional<Attribute<?>> findAttribute(List<Attribute<?>> attributes, long attributeId) {
        return attributes.stream()
                .filter(a -> a.id().equals(attributeId))
                .findFirst();
    }

}
