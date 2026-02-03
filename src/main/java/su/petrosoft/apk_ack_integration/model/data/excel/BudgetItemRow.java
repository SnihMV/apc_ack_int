package su.petrosoft.apk_ack_integration.model.data.excel;

import lombok.Data;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.*;

@Data
public class BudgetItemRow extends ExcelPojo {

    private static final String DESCRIPTION_PREFIX = "Наименование ";
    private static final Set<Dictionary> REQUIRED_DICTIONARIES = Set.of(
            KVSR, KFSR, KCSR, DOPKR, DOPEK, DOPFK, KVR, KOSGU, PURPOSE
    );

    private static final Set<String> REQUIRED_NON_DICTIONARIES = Set.of(
            "Ассигнования {year} год", "Ассигнования Фед {year} год", "Ассигнования Рег {year} год",
            "КП - расходы Январь", "КП - расходы Февраль", "КП - расходы Март", "КП - расходы Апрель",
            "КП - расходы Май", "КП - расходы Июнь", "КП - расходы Июль", "КП - расходы Август",
            "КП - расходы Сентябрь", "КП - расходы Октябрь", "КП - расходы Ноябрь", "КП - расходы Декабрь"
    );

    private final Set<String> requiredColumns = new HashSet<>();
    private final Set<String> optionalColumns = new HashSet<>();

    public BudgetItemRow() {
        fillRequiredSet();
        fillOptionalSet();
    }

    private void fillRequiredSet() {
        Set<String> dictionaryCodeColumns = REQUIRED_DICTIONARIES.stream()
                .map(Dictionary::getName)
                .collect(Collectors.toSet());
        requiredColumns.addAll(dictionaryCodeColumns);

        Set<String> nonDictionaryColumns = REQUIRED_NON_DICTIONARIES.stream()
                .map(str -> str.replace("{year}", String.valueOf(LocalDate.now().getYear())))
                .collect(Collectors.toSet());
        requiredColumns.addAll(nonDictionaryColumns);
    }

    private void fillOptionalSet() {
        Set<String> dictionaryDescriptionColumns = REQUIRED_DICTIONARIES.stream()
                .map(dict -> DESCRIPTION_PREFIX + dict.getName())
                .collect(Collectors.toSet());
        optionalColumns.addAll(dictionaryDescriptionColumns);
    }


}