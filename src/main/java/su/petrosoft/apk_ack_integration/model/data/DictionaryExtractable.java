package su.petrosoft.apk_ack_integration.model.data;

import java.util.Map.Entry;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

import java.util.Map;

public interface DictionaryExtractable {

    Map<Dictionary, Entry<String, String>> dictionaryData();

}
