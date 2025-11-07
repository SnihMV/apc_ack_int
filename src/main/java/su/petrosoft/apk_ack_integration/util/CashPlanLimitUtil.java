package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.xml.PlDirectionLine;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

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

    public static Long getCodeId(Map<CodeType, Map<Long, String>> allCodes, CodeType type, String code) {
        return allCodes.get(type).entrySet().stream()
                .filter(entry -> entry.getValue().equals(code))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new RuntimeException(
                        "There is no code %s in %s dictionary".formatted(code, type.name())));
    }

    private static BigDecimal sumOf(BigDecimal... items) {
        return Stream.of(items)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
