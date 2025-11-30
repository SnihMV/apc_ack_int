package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateInstancesFromFileResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;

import java.util.List;
import java.util.Map;
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

    public static Object getAttrData(List<Attribute<?>> attributes, Long attributeId) {
        return attributes.stream()
            .filter(a -> a.id().equals(attributeId))
            .findFirst()
            .map(Attribute::getData)
            .orElse(null);
    }

    public static String getAttrShortForm(List<Attribute<?>> attributes, Long attributeId) {
        return attributes.stream()
            .filter(a -> a.id().equals(attributeId))
            .findFirst()
            .map(Attribute::getShortForm)
            .orElse(null);
    }

}
