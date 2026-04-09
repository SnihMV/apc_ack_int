package su.petrosoft.apk_ack_integration.model.data.excel;

import lombok.Builder;
import su.petrosoft.apk_ack_integration.model.data.BudgetItemData;
import su.petrosoft.apk_ack_integration.model.data.DescriptedBudgetItemData;

@Builder
public record UniBudget20262801ItemExcelRow(
    String kvsr,
    String kfsr,
    String kcsr,
    String kcsrTitle,
    String dopKr,
    String dopKrTitle,
    String kvr,
    String dopFk,
    String dopEk,
    String purpose,
    String kvfo,
    String kosgu,

    Double assignTotal,
    Double assignFederal,
    Double assignRegional,

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

) {

}
