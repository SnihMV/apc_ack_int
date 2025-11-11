package su.petrosoft.apk_ack_integration.util;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Stream;
import su.petrosoft.apk_ack_integration.model.xml.PlDirectionLine;

public class CashPlanLimitUtil {

    public static final long CASH_PLAN_LIMIT_TEMPLATE_ID = 9460;

    public static BigDecimal getTotalLimit(PlDirectionLine line) {
        return sumOf(line.limitAmt1(), line.limitAmt2(), line.limitAmt3());
    }

    public static BigDecimal getTotalFederal(PlDirectionLine line) {
        return sumOf(line.limitFederalAmt1(), line.limitFederalAmt2(), line.limitFederalAmt3());
    }

    public static BigDecimal getTotalRegional(PlDirectionLine line) {
        return sumOf(line.limitRegionalAmt1(), line.limitRegionalAmt2(), line.limitRegionalAmt3());
    }

    public static BigDecimal sumOf(BigDecimal... items) {
        return Stream.of(items)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
