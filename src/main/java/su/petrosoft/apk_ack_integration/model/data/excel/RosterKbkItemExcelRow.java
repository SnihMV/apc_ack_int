package su.petrosoft.apk_ack_integration.model.data.excel;

import lombok.Builder;
import su.petrosoft.apk_ack_integration.model.data.BudgetItemData;

@Builder
public record RosterKbkItemExcelRow(
        String section,
        String subsection,
        String kcsr,
        String dopKr,
        String kvr,
        String kosgu,
        String kvsr,
        String dopFk,
        String dopEk,
        String purpose,

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

) {
    public String kfsr() {
        return section + subsection;
    }
}
