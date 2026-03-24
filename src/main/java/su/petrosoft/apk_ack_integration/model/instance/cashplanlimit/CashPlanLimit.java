package su.petrosoft.apk_ack_integration.model.instance.cashplanlimit;

import lombok.Builder;
import su.petrosoft.apk_ack_integration.model.instance.BasePlicanteInstance;
import su.petrosoft.apk_ack_integration.model.instance.InstanceAttributeInfo;

import java.util.Map;

@Builder
public class CashPlanLimit extends BasePlicanteInstance<CashPlanLimitIdentAttribute, CashPlanLimitOptionAttribute> {

    @Override
    protected Class<CashPlanLimitIdentAttribute> getIdentifyAttributeInfoClass() {
        return CashPlanLimitIdentAttribute.class;
    }

    @Override
    protected Class getOptionalAttributeInfoClass() {
        return CashPlanLimitOptionAttribute.class;
    }

    @Override
    protected Map updateData(Map updatingData) {
        return Map.of();
    }
}
