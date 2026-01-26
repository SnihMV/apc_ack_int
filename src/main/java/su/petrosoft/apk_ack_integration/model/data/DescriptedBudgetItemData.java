package su.petrosoft.apk_ack_integration.model.data;

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

    String purposeTitle();

    String dopFkTitle();

    @Override
    default Map<Dictionary, String> dictionaryCodes() {
        return Map.of(
                KCSR, kcsr(),
                KFSR, kfsr(),
                KVSR, kvsr(),
                KVR, kvr(),
                KOSGU, kosgu(),
                DOPEK, dopEk(),
                DOPKR, dopKr(),
                DOPFK, dopFk(),
                PURPOSE, purpose()
        );
    }

    @Override
    default Map<Dictionary, String> dictionaryDescriptions() {
        return Map.of(
                KCSR, kcsrTitle(),
                KFSR, kfsrTitle(),
                KVSR, kvsrTitle(),
                KVR, kvrTitle(),
                KOSGU, kosguTitle(),
                DOPEK, dopEkTitle(),
                DOPKR, dopKrTitle(),
                DOPFK, dopFkTitle(),
                PURPOSE, purposeTitle()
        );
    }
}
