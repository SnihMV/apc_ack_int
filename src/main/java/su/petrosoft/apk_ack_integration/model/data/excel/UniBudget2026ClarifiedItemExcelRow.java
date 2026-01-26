package su.petrosoft.apk_ack_integration.model.data.excel;

import lombok.Builder;
import su.petrosoft.apk_ack_integration.model.data.DescriptedBudgetItemData;

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

    Double janLimit,
    Double febLimit,
    Double marLimit,
    Double aprLimit,
    Double mayLimit,
    Double junLimit,
    Double julLimit,
    Double augLimit,
    Double sepLimit,
    Double octLimit,
    Double novLimit,
    Double decLimit

) implements DescriptedBudgetItemData {
//    @Override
//    public String code() {
//        return "16";
//    }
}
