package su.petrosoft.apk_ack_integration.util;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import su.petrosoft.apk_ack_integration.model.dto.request.instance.Filter;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.FilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateInstancesFromFileResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.enums.ValueType;

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

    public static Filter makeSimpleLongAttributeFilter(Map<Long, Long> filters) {
        if (filters == null || filters.isEmpty()) {
            return null;
        }
        return new Filter(filters.entrySet().stream()
                .map(entry -> new FilterAttribute(ValueType.LONG, entry.getKey(), entry.getValue()))
                .toList());
    }
    public static Filter makeFilter()

}
