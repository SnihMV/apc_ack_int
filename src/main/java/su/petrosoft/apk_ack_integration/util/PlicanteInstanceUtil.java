package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.Pair;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateInstancesFromFileResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

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

    public static Long getCodeId(Map<CodeType, Map<Long, String>> allCodes, CodeType type, String code) {
        return allCodes.get(type).entrySet().stream()
                .filter(entry -> entry.getValue().equalsIgnoreCase(code))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(type.getDefaultValue());
    }

    public static Long toEpochMilli(LocalDate day) {
        if (day == null) {
            return null;
        }
        return day.atStartOfDay(MOSCOW_ZONE)
                .toInstant()
                .toEpochMilli();
    }

    public static LocalDate toLocalDate(long epochMilli) {
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
