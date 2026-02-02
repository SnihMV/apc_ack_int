package su.petrosoft.apk_ack_integration.model.data;

import java.util.AbstractMap.SimpleEntry;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

import java.util.Map;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPEK;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPFK;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPKR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KCSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KFSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KOSGU;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KVR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KVSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.PURPOSE;

public interface DescriptedBudgetItemData extends BudgetItemData, DictionaryExtractable {

    String kfsrTitle();

    String kvsrTitle();

    String kcsrTitle();

    String kvrTitle();

    String kosguTitle();

    String dopEkTitle();

    String dopKrTitle();

    String dopFkTitle();

    String purposeTitle();

    @Override
    default Map<Dictionary, SimpleEntry<String, String>> getDictionaries() {
        return Map.of(
            KVR, new SimpleEntry<>(kvr(), kvrTitle()),
            KFSR, new SimpleEntry<>(kfsr(), kfsrTitle()),
            KVSR, new SimpleEntry<>(kvsr(), kvsrTitle()),
            KCSR, new SimpleEntry<>(kcsr(), kcsrTitle()),
            KOSGU, new SimpleEntry<>(kosgu(), kosguTitle()),
            DOPEK, new SimpleEntry<>(dopEk(), dopEkTitle()),
            DOPKR, new SimpleEntry<>(dopKr(), dopKrTitle()),
            DOPFK, new SimpleEntry<>(dopFk(), dopFkTitle()),
            PURPOSE, new SimpleEntry<>(purpose(), purposeTitle())
        );
    }
}
