package su.petrosoft.apk_ack_integration.model.excel;

public sealed interface BudgetItemExcelRow
    permits RosterKbkItemExcelRow, UniBudget2026ClarifiedItemExcelRow, UniBudget2026ItemExcelRow,
        UniBudgetCodedItemExcelRow, UniBudgetItemExcelRow {

    default String code() {
        return "1337";
    }

    String kfsr();

    default String kfsrTitle() {
        return "";
    }

    String kvsr();

    default String kvsrTitle() {
        return "";
    }

    String kcsr();

    default String kcsrTitle() {
        return "";
    }

    String kvr();

    default String kvrTitle() {
        return "";
    }

    String kosgu();

    default String kosguTitle() {
        return "";
    }

    String dopEk();

    default String dopEkTitle() {
        return "";
    }

    String dopKr();

    default String dopKrTitle() {
        return "";
    }

    String purpose();

    default String purposeTitle() {
        return "";
    }

    String dopFk();

    default String dopFkTitle() {
        return "";
    }

    Double assignTotal();

    Double assignFederal();

    Double assignRegional();

    Double m01Amt();

    Double m02Amt();

    Double m03Amt();

    Double m04Amt();

    Double m05Amt();

    Double m06Amt();

    Double m07Amt();

    Double m08Amt();

    Double m09Amt();

    Double m10Amt();

    Double m11Amt();

    Double m12Amt();
}
