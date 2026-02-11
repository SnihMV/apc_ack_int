package su.petrosoft.apk_ack_integration.model.data;

import java.util.Map.Entry;
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

public interface DescriptedBudgetItemData extends BudgetItemData, DictionaryContaining {

    String kfsrTitle();

    String kvsrTitle();

    String kcsrTitle();

    String kvrTitle();

    String kosguTitle();

    String dopEkTitle();

    String dopKrTitle();

    String purposeTitle();

    String dopFkTitle();

    @Override
    default Map<Dictionary, Entry<String, String>> dictionariesData() {
        return Map.of(
            KFSR, Map.entry(kfsr(), kfsrTitle()),
            KVSR, Map.entry(kvsr(), kvsrTitle()),
            KCSR, Map.entry(kcsr(), kcsrTitle()),
            KVR, Map.entry(kvr(), kvrTitle()),
            KOSGU, Map.entry(kosgu(), kosguTitle()),
            DOPEK, Map.entry(dopEk(), dopEkTitle()),
            DOPKR, Map.entry(dopKr(), dopKrTitle()),
            DOPFK, Map.entry(dopFk(), dopFkTitle()),
            PURPOSE, Map.entry(purpose(), purposeTitle())
        );
    }
}
