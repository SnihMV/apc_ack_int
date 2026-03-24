package su.petrosoft.apk_ack_integration.model.instance.cashplanlimit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;
import su.petrosoft.apk_ack_integration.model.instance.InstanceAttributeInfo;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.DOUBLE;

@Getter
@RequiredArgsConstructor
public enum CashPlanLimitOptionAttribute implements InstanceAttributeInfo {
    TOTAL_LIMIT(1609, DOUBLE),
    TOTAL_EXPENSE(1610, DOUBLE),
    TOTAL_BALANCE(1611, DOUBLE),
    REGIONAL_BUDGET(1828, DOUBLE),
    JAN_LIMIT(1829, DOUBLE),
    FEB_LIMIT(1612, DOUBLE),
    MAR_LIMIT(1613, DOUBLE),
    APR_LIMIT(1614, DOUBLE),
    MAY_LIMIT(1617, DOUBLE),
    JUN_LIMIT(1618, DOUBLE),
    JUL_LIMIT(1619, DOUBLE),
    AUG_LIMIT(1622, DOUBLE),
    SEP_LIMIT(1623, DOUBLE),
    OCT_LIMIT(1624, DOUBLE),
    NOV_LIMIT(1627, DOUBLE),
    DEC_LIMIT(1628, DOUBLE),
    JAN_BALANCE(1629, DOUBLE),
    FEB_BALANCE(3276, DOUBLE),
    MAR_BALANCE(3278, DOUBLE),
    APR_BALANCE(3280, DOUBLE),
    MAY_BALANCE(3282, DOUBLE),
    JUN_BALANCE(3284, DOUBLE),
    JUL_BALANCE(3286, DOUBLE),
    AUG_BALANCE(3288, DOUBLE),
    SEP_BALANCE(3290, DOUBLE),
    OCT_BALANCE(3292, DOUBLE),
    NOV_BALANCE(3294, DOUBLE),
    DEC_BALANCE(3296, DOUBLE),
    Q_1_BALANCE(3298, DOUBLE),
    Q_2_BALANCE(1616, DOUBLE),
    Q_3_BALANCE(1621, DOUBLE),
    Q_4_BALANCE(1626, DOUBLE),
    JAN_EXPENSE(1631, DOUBLE),
    FEB_EXPENSE(3275, DOUBLE),
    MAR_EXPENSE(3277, DOUBLE),
    APR_EXPENSE(3279, DOUBLE),
    MAY_EXPENSE(3281, DOUBLE),
    JUN_EXPENSE(3283, DOUBLE),
    JUL_EXPENSE(3285, DOUBLE),
    AUG_EXPENSE(3287, DOUBLE),
    SEP_EXPENSE(3289, DOUBLE),
    OCT_EXPENSE(3291, DOUBLE),
    NOV_EXPENSE(3293, DOUBLE),
    DEC_EXPENSE(3295, DOUBLE),
    Q_1_EXPENSE(3297, DOUBLE),
    Q_2_EXPENSE(1615, DOUBLE),
    Q_3_EXPENSE(1620, DOUBLE),
    Q_4_EXPENSE(1625, DOUBLE);

    private final long id;
    private final AttributeType type;

    public static final Map<Long, CashPlanLimitOptionAttribute> BY_ID = Arrays.stream(values())
            .collect(Collectors.toMap(
                    InstanceAttributeInfo::getId,
                    Function.identity()
            ));

    @Override
    public Map<Long, ? extends InstanceAttributeInfo> getIdMap() {
        return BY_ID;
    }
}
