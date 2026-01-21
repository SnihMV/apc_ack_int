package su.petrosoft.apk_ack_integration.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import su.petrosoft.apk_ack_integration.model.excel.CofinancingLevelExcelRow;
import su.petrosoft.apk_ack_integration.model.excel.RosterKbkExcelRow;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetCodedExcelRow;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetExcelRow;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ExcelFileType {
    UNI_BUDGET_2026(List.of("КВСР", "КФСР", "Подраздел", "Наименование КФСР", "КЦСР", "Наименование КЦСР", "Доп. КР"),
            "Итого",
            UniBudgetCodedExcelRow.class),
    UNI_BUDGET_CODED(
            List.of("Код", "Раздел", "Подраздел", "Наименование КФСР", "КЦСР", "Наименование КЦСР", "Доп. КР"),
            "Итого",
            UniBudgetCodedExcelRow.class),
    UNI_BUDGET(
            List.of("Раздел", "Подраздел", "КЦСР", "Наименование КЦСР", "Доп. КР", "Наименование Доп. КР", "КВР"),
            "Итого",
            UniBudgetExcelRow.class),
    ROSTER_KBK(
            List.of("Раздел", "Подраздел", "КЦСР", "Доп. КР", "КВР", "КОСГУ", "КВСР"),
            "Итого",
            RosterKbkExcelRow.class),
    COFINANCING_LEVEL(
            List.of("Код", "Раздел", "Подраздел", "Наименование КФСР", "КЦСР", "Наименование КЦСР", "Доп. КР"),
            "Итого",
            CofinancingLevelExcelRow.class
    );

    private final List<String> columnNames;
    private final String footerSearchKey;
    private final Class<?> klass;
}
