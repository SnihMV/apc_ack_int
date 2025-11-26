package su.petrosoft.apk_ack_integration.model.excel;

public sealed interface CashPlanLimitExcelRow
        permits UniBudgetExcelRow, UniBudgetCodedExcelRow, RosterKbkExcelRow {

    String section();

    String subsection();

    String kvsr();

    String kcsr();

    String kvr();

    String kosgu();

    String dopEk();

    String dopKr();

    String purpose();

    String dopFk();

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

    default String getKfsr() {
        return section() + subsection();
    }
}
