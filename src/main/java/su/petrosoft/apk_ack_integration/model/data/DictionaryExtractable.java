package su.petrosoft.apk_ack_integration.model.data;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

public interface DictionaryExtractable {

    Map<Dictionary, SimpleEntry<String, String>> getDictionaries();

}
