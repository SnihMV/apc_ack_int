package su.petrosoft.apk_ack_integration.model.data;

import su.petrosoft.apk_ack_integration.model.DictionaryData;
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

public interface DescriptedBudgetItemData extends BudgetItemData, DictionaryDataContaining {

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
    default Map<Dictionary, DictionaryData> dictionariesData() {
        return Map.of(
                KFSR, new DictionaryData(kfsr(), kfsrTitle()),
                KVSR, new DictionaryData(kvsr(), kvsrTitle()),
                KCSR, new DictionaryData(kcsr(), kcsrTitle()),
                KVR, new DictionaryData(kvr(), kvrTitle()),
                KOSGU, new DictionaryData(kosgu(), kosguTitle()),
                DOPEK, new DictionaryData(dopEk(), dopEkTitle()),
                DOPKR, new DictionaryData(dopKr(), dopKrTitle()),
                DOPFK, new DictionaryData(dopFk(), dopFkTitle()),
                PURPOSE, new DictionaryData(purpose(), purposeTitle())
        );
    }
}
