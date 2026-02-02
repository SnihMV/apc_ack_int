package su.petrosoft.apk_ack_integration.model.data.excel;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

public class UniBudgetExcelRow {

    private static final Set<Dictionary> DICTIONARIES = Set.of(
        KVSR, KFSR, KCSR, DOPKR, DOPEK, DOPFK, KVR, KOSGU, PURPOSE
    );

    private static final Set<String> CASH_PLAN_MONTHS = Set.of(
        "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь",
        "Октябрь", "Ноябрь", "Декабрь"
    );

    private static final Set<String> ALLOCATION_PREFIXES = Set.of(
        "Ассигнования ", "Ассигнования Фед ", "Ассигнования Рег "
    );

    private static final String ALLOCATION_SUFFIX = "год";

    private final static Set<String> REQUIRED_COLUMN_NAMES = new HashSet<>();


    static {
        REQUIRED_COLUMN_NAMES.addAll(
            DICTIONARIES.stream()
                .map(Dictionary::getName)
                .collect(Collectors.toSet()));
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