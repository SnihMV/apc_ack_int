package su.petrosoft.apk_ack_integration.model.instance.cashplanlimit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;
import su.petrosoft.apk_ack_integration.model.instance.IdentAttributeInfo;
import su.petrosoft.apk_ack_integration.model.instance.InstanceAttributeInfo;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.*;

@RequiredArgsConstructor
@Getter
public enum CashPlanLimitIdentAttribute implements IdentAttributeInfo {
    YEAR(3303, LONG),
    KVSR(1733, LINKED),
    KFSR(1734, LINKED),
    KCSR(1735, LINKED),
    KVR(1736, LINKED),
    KOSGU(1737, LINKED),
    DOPEK(1739, LINKED),
    DOPKR(1740, LINKED),
    PURPOSE(1751, LINKED),
    DOPFK(3448, LINKED),
    RECIPIENTINN(4427, STRING),
    RECIPIENTKPP(4428, STRING);

    private final long id;
    private final AttributeType type;

    private static final Map<Long, CashPlanLimitIdentAttribute> BY_ID = Arrays.stream(values())
            .collect(Collectors.toMap(
                    InstanceAttributeInfo::getId,
                    Function.identity()));

    @Override
    public Map<Long, ? extends InstanceAttributeInfo> getIdMap() {
        return BY_ID;
    }
}
