package su.petrosoft.apk_ack_integration.model.data.excel;

import lombok.Builder;
import su.petrosoft.apk_ack_integration.model.data.DescriptedBudgetItemData;

@Builder
public record UniBudget2026ItemExcelRow(
        String kfsr,
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
        String dopFkTitle,
        String dopEk,
        String dopEkTitle,
        String purpose,
        String purposeTitle,
        String kvfo,
        String kvfoTitle,

        String recipientName,

        Double assignTotal,
        Double assignFederal,
        Double assignRegional,

        Double financeTotal,
        Double requested,

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
        Double decLimit,

        Double financeFederal,
        Double financeRegional
) implements DescriptedBudgetItemData {

    @Override
    public String recipientInn() {
        return "";
    }

    @Override
    public String recipientKpp() {
        return "";
    }
}
