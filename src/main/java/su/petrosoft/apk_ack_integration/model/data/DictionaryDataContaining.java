package su.petrosoft.apk_ack_integration.model.data;

import su.petrosoft.apk_ack_integration.model.DictionaryData;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

import java.util.Map;

public interface DictionaryDataContaining {

    Map<Dictionary, DictionaryData> dictionariesData();
}
