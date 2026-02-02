package su.petrosoft.apk_ack_integration.model.data.excel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BudgetItemRequiredFields {
    ASSIGN_TOT("Ассигнования {year} год"),
    ASSIGN_FED("Ассигнования Фед {year} год"),
    ASSIGN_REG("Ассигнования Рег {year} год"),
    JAN_LIMIT("КП - расходы Январь"),
    FEB_LIMIT("КП - расходы Февраль"),
    MAR_LIMIT("КП - расходы Март"),
    APR_LIMIT("КП - расходы Апрель"),
    MAY_LIMIT("КП - расходы Май"),
    JUN_LIMIT("КП - расходы Июнь"),
    JUL_LIMIT("КП - расходы Июль"),
    AUG_LIMIT("КП - расходы Август"),
    SEP_LIMIT("КП - расходы Сентябрь"),
    OCT_LIMIT("КП - расходы Октябрь"),
    NOV_LIMIT("КП - расходы Ноябрь"),
    DEC_LIMIT("КП - расходы Декабрь");

    private final String pattern;

    public String getColumnName(int year) {
        return pattern.replace("{year}", String.valueOf(year));
    }
}
