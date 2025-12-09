package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.Pair;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateInstancesFromFileResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class PlicanteInstanceUtil {

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
                .filter(entry -> entry.getValue().equals(code))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new RuntimeException(
                        "There is no code %s in %s dictionary".formatted(code, type.name())));
    }

    @SuppressWarnings("unchecked")
    public static <T> T extractAttributeData(List<Attribute<?>> attributes, long attributeId) {
        return findAttribute(attributes, attributeId)
                .map(attr -> (T) attr.getData())
                .orElse(null);
    }

    public static String getAttrShortForm(List<Attribute<?>> attributes, Long attributeId) {
        return findAttribute(attributes, attributeId)
                .map(Attribute::getShortForm)
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> extractAllData(List<Attribute<?>> attributes, long attributeId) {
        return findAttribute(attributes, attributeId)
                .map(attr -> (List<T>) attr.getAllData())
                .orElse(Collections.emptyList());
    }

    public static List<String> extractAllShortForms(List<Attribute<?>> attributes, long attributeId) {
        return findAttribute(attributes, attributeId)
                .map(Attribute::getAllShortForms)
                .orElse(Collections.emptyList());
    }

    public static Pair extractAttributePair(List<Attribute<?>> attributes, long attributeId) {
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
