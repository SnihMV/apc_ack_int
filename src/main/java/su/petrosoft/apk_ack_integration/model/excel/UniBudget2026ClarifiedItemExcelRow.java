package su.petrosoft.apk_ack_integration.model.excel;

import lombok.Builder;

@Builder
public record UniBudget2026ClarifiedItemExcelRow(
    String kvsr,
    String kvsrTitle,
    String kfsr,
    String kfsrTitle,
    String kcsr,
    String kcsrTitle,
    String dopKr,
    String dopKrTitle,
    String kvr,
    String kvrTitle,
    String dopFk,
    String dopFkTitle,
    String dopEk,
    String dopEkTitle,
    String purpose,
    String purposeTitle,
    String kvfo,
    String kvfoTitle,
    String kosgu,
    String kosguTitle,

    String recipientName,
    String recipientInn,
    String recipientKpp,

    Double assignTotal,
    Double assignFederal,
    Double assignRegional,

    Double financeTotal,
    Double requested,

    Double financeFederal,
    Double financeRegional,

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
    Double m12Amt

) implements BudgetItemExcelRow {
}
