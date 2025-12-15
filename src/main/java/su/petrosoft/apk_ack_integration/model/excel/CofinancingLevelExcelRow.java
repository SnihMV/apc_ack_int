package su.petrosoft.apk_ack_integration.model.excel;

import lombok.Builder;

@Builder
public record CofinancingLevelExcelRow(
        String code,
        String section,
        String subsection,
        String kfsrTitle,
        String kcsr,
        String kcsrTitle,
        String dopKr,
        String dopKrTitle,
        String kvr,
        String kvrTitle,
        String kosgu,
        String kosguTitle,
        String kvsr,
        String kvsrTitle,
        String dopFk,
        String dopFrTitle,
        String dopEk,
        String dopEkTitle,
        String purpose,
        String purposeTitle,
        String kvfo,
        String kvfoTitle,

        Double assignTotal,
        Double assignFederal,
        Double assignRegional,

        Double obCoeff,
        Double fbCoeff,

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
