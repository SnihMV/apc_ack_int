package su.petrosoft.apk_ack_integration.model.excel;

import lombok.Builder;

@Builder
public record CashPlanLimitExcelRow(
        String section,
        String subsection,
        String kcsr,
        String dopKr,
        String kvr,
        String kosgu,
        String kvsr,
        String dopFk,
        String dopEk,
        String purposeCode,

        Double assignTotal,
        Double assignFederal,
        Double assignRegional,

        Double financeTotal,
        Double requested,

        Double m01Amt,
        Double m02Amt,
        Double m03Amt,
        Double m04Amt,
        Double m05Amt,
        Double m06Amt,
        Double m07Amt,
        Double m08Amt,
        Double m09Amt,
        Double m10Amt,
        Double m11Amt,
        Double m12Amt,

        Double financeFederal,
        Double financeRegional

) {
}
