package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.CodeType;
import su.petrosoft.apk_ack_integration.model.xml.PlDirectionLine;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class CashPlanLimitUtil {

    public static final long CASH_PLAN_LIMIT_TEMPLATE_ID = 9460;
    public static final long STATUS_ACTUAL_ID = 9624;
    public static final long STATUS_DRAFT_ID = 9588;

    public static BigDecimal getTotalLimit(PlDirectionLine line) {
        return sumOf(line.limitAmt1(), line.limitAmt2(), line.limitAmt3());
    }

    public static BigDecimal getTotalFederal(PlDirectionLine line) {
        return sumOf(line.limitFederalAmt1(), line.limitFederalAmt2(), line.limitFederalAmt3());
    }

    public static BigDecimal getTotalRegional(PlDirectionLine line) {
        return sumOf(line.limitRegionalAmt1(), line.limitRegionalAmt2(), line.limitRegionalAmt3());
    }

    public static Long getCodeId(Map<CodeType, Map<Long, String>> codes, CodeType type, String code) {
        return codes.get(type).entrySet().stream()
                .filter(entry -> entry.getValue().equals(code))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Code [%s] not found in existed [%s] collection"
                        .formatted(code, type.getName())))
                .getKey();
    }

    private static BigDecimal sumOf(BigDecimal... items) {
        return Stream.of(items)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
