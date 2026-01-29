package su.petrosoft.apk_ack_integration.model.data.excel;

import lombok.Builder;
import su.petrosoft.apk_ack_integration.model.data.CofinancingLevelData;
import su.petrosoft.apk_ack_integration.model.data.DescriptedBudgetItemData;

@Builder
public record CofinancingLevel2026ExcelRow(
        String programTitle,
        String kcsr,
        String dopKr,
        String kosgu,
        Double fbCoeff,
        Double obCoeff
) implements CofinancingLevelData {
}
