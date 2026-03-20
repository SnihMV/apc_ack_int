package su.petrosoft.apk_ack_integration.model.instance.cashplanlimit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;
import su.petrosoft.apk_ack_integration.model.instance.InstanceAttributeEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.*;

@RequiredArgsConstructor
@Getter
public enum CashPlanLimitAttributeEnum implements InstanceAttributeEnum {
    YEAR(3303, LONG, true),
    KVSR(1733, LINKED, true),
    KFSR(1734, LINKED, true),
    KCSR(1735, LINKED, true),
    KVR(1736, LINKED, true),
    KOSGU(1737, LINKED, true),
    DOPEK(1739, LINKED, true),
    DOPKR(1740, LINKED, true),
    PURPOSE(1751, LINKED, true),
    DOPFK(3448, LINKED, true),
    RECIPIENTINN(4427, STRING, true),
    RECIPIENTKPP(4428, STRING, true);

    private final long id;
    private final AttributeType type;
    private final boolean isIdentifying;
    private static final Map<Long, CashPlanLimitAttributeEnum> BY_ID =
            Arrays.stream(values()).collect(Collectors.toMap(InstanceAttributeEnum::getId, Function.identity()));

}
