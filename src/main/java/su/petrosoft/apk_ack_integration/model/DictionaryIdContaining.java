package su.petrosoft.apk_ack_integration.model;

import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

import java.util.Map;

public interface DictionaryIdContaining {
    Map<Dictionary, Long> dictionaryIds();
}
