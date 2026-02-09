package su.petrosoft.apk_ack_integration.util;

import java.util.Collection;
import java.util.Map.Entry;

import lombok.extern.slf4j.Slf4j;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Pair;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.response.CreatingInstancesFromFileResponseDto;
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

    public static <T> CreatingInstancesFromFileResponseDto creatingInstancesFromFileResponseDto(
            List<?> dtoList,
            List<T> createdInstances,
            Function<T, Long> function
    ) {
        return new CreatingInstancesFromFileResponseDto(
                dtoList.size(),
                createdInstances.size(),
                createdInstances.stream()
                        .map(function)
                        .toList());
    }

    public static CreateInstanceRequestDto creatingDictionaryInstanceRequestDto(
            Dictionary type, String code, String description) {
        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(type.getTemplateId())
                        .attributes(List.of(
                                new StringAttribute(type.getCodeAttrId(), code),
                                new StringAttribute(type.getDescriptionAttrId(), description)))
                        .build());
    }

//    public static UpdateInstanceRequestDto requestDtoForUpdatingDictionaryDescription(Dictionary dictionary, long id, String description) {
//        return new UpdateInstanceRequestDto(
//                InstanceDto.builder()
//                        .id(id)
//                        .templateId(dictionary.getTemplateId())
//                        .version(updatedSP.getVersion())
//                        .attributes(List.of(
//                                new LinkedAttribute(COFIN_LVL_ATTR, updatedSP.getCofinancingLevelIds())))
//                        .build());
//    }

    public static Long dictionaryIdByCode(Map<Dictionary, Map<Long, Entry<String, String>>> allCodes, Dictionary dictionary, String code) {
        return allCodes.get(dictionary).entrySet().stream()
                .filter(entry -> entry.getValue().getKey().equalsIgnoreCase(code))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new RuntimeException("Not found code [%s] for dictionary [%s]".formatted(code, dictionary)));
    }

    public static String dictionaryCodeById(Map<Dictionary, Map<Long, Entry<String, String>>> allCodes, Dictionary dictionary, long id) {
        return allCodes.get(dictionary).entrySet().stream()
                .filter(entry -> entry.getKey().equals(id))
                .findFirst()
                .map(entry -> entry.getValue().getKey())
                .orElseThrow(() -> new RuntimeException("Not found dictionary [%s] instance with id [%d]".formatted(dictionary, id)));
    }

    public static String dictionaryCodeDescription(Map<Dictionary, Map<Long, Entry<String, String>>> allCodes, Dictionary dictionary, String code) {
        return allCodes.get(dictionary).entrySet().stream()
                .filter(entry -> entry.getValue().getKey().equalsIgnoreCase(code))
                .findFirst()
                .map(entry -> entry.getValue().getValue())
                .orElseThrow(() -> new RuntimeException("Not found dictionary [%s] instance with code [%s]".formatted(dictionary, code)));
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
    public static <T> Collection<T> extractAllData(List<Attribute<?>> attributes, long attributeId) {
        return findAttribute(attributes, attributeId)
                .map(attr -> (Collection<T>) attr.getAllData())
                .orElse(new ArrayList<>());
    }

    public static List<String> extractAllShortForms(List<Attribute<?>> attributes,
                                                    long attributeId) {
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

    private static Optional<Attribute<?>> findAttribute(List<Attribute<?>> attributes,
                                                        long attributeId) {
        return attributes.stream()
                .filter(a -> a.id().equals(attributeId))
                .findFirst();
    }

}
