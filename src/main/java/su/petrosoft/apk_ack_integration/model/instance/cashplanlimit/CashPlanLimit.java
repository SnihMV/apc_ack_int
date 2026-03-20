package su.petrosoft.apk_ack_integration.model.instance.cashplanlimit;

import lombok.Builder;
import su.petrosoft.apk_ack_integration.model.instance.BasePlicanteInstance;

import java.util.Map;

@Builder
public class CashPlanLimit extends BasePlicanteInstance<CashPlanLimitAttributeEnum> {

    public CashPlanLimit() {
    }

    public CashPlanLimit(Map<CashPlanLimitAttributeEnum, Object> values) {
        super(values);

    }

    @Override
    protected Class<CashPlanLimitAttributeEnum> getAttributeInfoClass() {
        return CashPlanLimitAttributeEnum.class;
    }

    @Override
    protected Map<CashPlanLimitAttributeEnum, Object> updateData(Map<CashPlanLimitAttributeEnum, Object> updatingData) {
        return Map.of();
    }
}
