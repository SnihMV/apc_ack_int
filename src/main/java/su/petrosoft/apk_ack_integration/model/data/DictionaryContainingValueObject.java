package su.petrosoft.apk_ack_integration.model.data;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPEK;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPFK;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPKR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KCSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KFSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KOSGU;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KVR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KVSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.PURPOSE;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import su.petrosoft.apk_ack_integration.exception.ExcelFileException;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

public abstract class DictionaryContainingValueObject implements DescriptedBudgetItemData {

    protected Map<Dictionary, Map.Entry<String, String>> dictionaries = new EnumMap<>(
        Dictionary.class);

    @Override
    public String dopKr() {
        return getCode(DOPKR);
    }


    @Override
    public String kcsr() {
        return getCode(KCSR);
    }

    @Override
    public String purpose() {
        return getCode(PURPOSE);
    }

    @Override
    public String dopEk() {
        return getCode(DOPEK);
    }

    @Override
    public String dopFk() {
        return getCode(DOPFK);
    }

    @Override
    public String kvsr() {
        return getCode(KVSR);
    }

    @Override
    public String kosgu() {
        return getCode(KOSGU);
    }

    @Override
    public String kvr() {
        return getCode(KVR);
    }

    @Override
    public String kfsr() {
        return getCode(KFSR);
    }

    @Override
    public String dopFkTitle() {
        return getDescription(DOPFK);
    }

    @Override
    public String purposeTitle() {
        return getDescription(PURPOSE);
    }

    @Override
    public String dopKrTitle() {
        return getDescription(DOPKR);
    }

    @Override
    public String dopEkTitle() {
        return getDescription(DOPEK);
    }

    @Override
    public String kosguTitle() {
        return getDescription(KOSGU);
    }

    @Override
    public String kvrTitle() {
        return getDescription(KVR);
    }

    @Override
    public String kcsrTitle() {
        return getDescription(KCSR);
    }

    @Override
    public String kvsrTitle() {
        return getDescription(KVSR);
    }

    @Override
    public String kfsrTitle() {
        return getDescription(KFSR);
    }


    private String getCode(Dictionary dictionary) {
        Entry<String, String> entry = dictionaries.get(dictionary);
        if (entry == null) {
            throw new ExcelFileException(
                "Could not find required code [%s] value".formatted(dictionary.getName()));
        }
        return entry.getKey();
    }

    private String getDescription(Dictionary dictionary) {
        Entry<String, String> entry = dictionaries.get(dictionary);
        if (entry == null) {
            throw new ExcelFileException(
                "Could not find required column [%s]".formatted(dictionary.getName()));
        }
        return entry.getValue();
    }
}
