package su.petrosoft.apk_ack_integration.util;

import java.util.Map;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;

public class DictionaryUtil {

    public static Long getCodeId(Map<CodeType, Map<Long, String>> allCodes, CodeType type, String code) {
        return allCodes.get(type).entrySet().stream()
            .filter(entry -> entry.getValue().equals(code))
            .findFirst()
            .map(Map.Entry::getKey)
            .orElseThrow(() -> new RuntimeException(
                "There is no code %s in %s dictionary".formatted(code, type.name())));
    }

}
