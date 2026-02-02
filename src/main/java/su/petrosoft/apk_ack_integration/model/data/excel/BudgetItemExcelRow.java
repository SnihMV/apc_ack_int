package su.petrosoft.apk_ack_integration.model.data.excel;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import su.petrosoft.apk_ack_integration.model.data.DictionaryContainingValueObject;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

public class BudgetItemExcelRow extends DictionaryContainingValueObject {

    private static final Set<Dictionary> REQUIRED_DICTIONARIES = Set.of(
        KVSR, KFSR, KCSR, DOPKR, DOPEK, DOPFK, KVR, KOSGU, PURPOSE
    );

    private final static Set<String> REQUIRED_COLUMN_NAMES = new HashSet<>();

    static {
        REQUIRED_COLUMN_NAMES.addAll(
            REQUIRED_DICTIONARIES.stream()
                .map(Dictionary::getName)
                .collect(Collectors.toSet()));
        REQUIRED_COLUMN_NAMES.addAll(
            Arrays.stream(BudgetItemRequiredFields.values())
                .map(field -> field.getColumnName(LocalDate.now().getYear()))
                .collect(Collectors.toSet())
        );
    }


    @Override
    public Double assignTotal() {
        return 0.0;
    }

    @Override
    public Double assignFederal() {
        return 0.0;
    }

    @Override
    public Double assignRegional() {
        return 0.0;
    }

    @Override
    public Double janLimit() {
        return 0.0;
    }

    @Override
    public Double febLimit() {
        return 0.0;
    }

    @Override
    public Double marLimit() {
        return 0.0;
    }

    @Override
    public Double aprLimit() {
        return 0.0;
    }

    @Override
    public Double mayLimit() {
        return 0.0;
    }

    @Override
    public Double junLimit() {
        return 0.0;
    }

    @Override
    public Double julLimit() {
        return 0.0;
    }

    @Override
    public Double augLimit() {
        return 0.0;
    }

    @Override
    public Double sepLimit() {
        return 0.0;
    }

    @Override
    public Double octLimit() {
        return 0.0;
    }

    @Override
    public Double novLimit() {
        return 0.0;
    }

    @Override
    public Double decLimit() {
        return 0.0;
    }
}


/*
        "КВСР", "КФСР", "КЦСР", "Доп. КР", "КВР", "Доп. ФК", "Доп. ЭК", "Код цели", "КВФО", "КОСГУ",
        "Ассигнования 2026 год", "Ассигнования Фед 2026 год", "Ассигнования Рег 2026 год",
        "КП - расходы Январь", "КП - расходы Февраль", "КП - расходы Март", "КП - расходы Апрель",
        "КП - расходы Май", "КП - расходы Июнь", "КП - расходы Июль", "КП - расходы Август",
        "КП - расходы Сентябрь", "КП - расходы Октябрь", "КП - расходы Ноябрь",
        "КП - расходы Декабрь"
*/