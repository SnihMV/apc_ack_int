package su.petrosoft.apk_ack_integration.model.data;

import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

import java.util.Map;

public interface DictionaryExtractable {

    Map<Dictionary, String> dictionaryCodes();

    Map<Dictionary, String> dictionaryDescriptions();
}
