package su.petrosoft.apk_ack_integration.util;

import java.util.Map;

public class DictionaryUtil {

    public static final Map<String, Long> OWNERSHIP_FORM = Map.of(
        "244", 75727L,
        "245", 75728L,
        "246", 75729L
    );
    public static final long DEFAULT_OWNERSHIP_FORM = 75730;

    public static long ownershipForm(String kosgu) {
        return OWNERSHIP_FORM.getOrDefault(kosgu, DEFAULT_OWNERSHIP_FORM);
    }

}
