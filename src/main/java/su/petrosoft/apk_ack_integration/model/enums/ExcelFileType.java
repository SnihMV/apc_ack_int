package su.petrosoft.apk_ack_integration.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import su.petrosoft.apk_ack_integration.model.excel.CofinancingLevelExcelRow;
import su.petrosoft.apk_ack_integration.model.excel.RosterKbkItemExcelRow;
import su.petrosoft.apk_ack_integration.model.excel.UniBudget2026ClarifiedItemExcelRow;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetCodedItemExcelRow;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetItemExcelRow;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ExcelFileType {
    UNI_BUDGET_2026_CLARIFIED(List.of("КВСР", "Наименование КВСР", "КФСР", "Наименование КФСР", "КЦСР", "Наименование КЦСР", "Доп. КР"),
            "Итого",
            UniBudget2026ClarifiedItemExcelRow.class),
    UNI_BUDGET_CODED(
            List.of("Код", "Раздел", "Подраздел", "Наименование КФСР", "КЦСР", "Наименование КЦСР", "Доп. КР"),
            "Итого",
            UniBudgetCodedItemExcelRow.class),
    UNI_BUDGET(
            List.of("Раздел", "Подраздел", "КЦСР", "Наименование КЦСР", "Доп. КР", "Наименование Доп. КР", "КВР"),
            "Итого",
            UniBudgetItemExcelRow.class),
    ROSTER_KBK(
            List.of("Раздел", "Подраздел", "КЦСР", "Доп. КР", "КВР", "КОСГУ", "КВСР"),
            "Итого",
            RosterKbkItemExcelRow.class),
    COFINANCING_LEVEL(
            List.of("Код", "Раздел", "Подраздел", "Наименование КФСР", "КЦСР", "Наименование КЦСР", "Доп. КР"),
            "Итого",
            CofinancingLevelExcelRow.class
    );

    private final List<String> columnNames;
    private final String footerSearchKey;
    private final Class<?> klass;
}
