package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.Filter;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.LongFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.ViewType;
import su.petrosoft.apk_ack_integration.model.xml.PlDirectionLine;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class CashPlanLimitUtil {

    public static final long TEMPLATE_ID = 9460;
    public static final String CPL_TITLE = "Лимиты кассового плана";

    public static final long YEAR_ATTR = 3303;
    public static final long KVSR_ATTR = 1733;
    public static final long KFSR_ATTR = 1734;
    public static final long KCSR_ATTR = 1735;
    public static final long KVR_ATTR = 1736;
    public static final long KOSGU_ATTR = 1737;
    public static final long DOPFK_ATTR = 3448;
    public static final long DOPEK_ATTR = 1739;
    public static final long DOPKR_ATTR = 1740;
    public static final long PURPOSE_ATTR = 1751;
    public static final long TOTAL_LIMIT_ATTR = 1609;
    public static final long TOTAL_BALANCE_ATTR = 1611;
    public static final long FEDERAL_BUDGET_ATTR = 1828;
    public static final long REGIONAL_BUDGET_ATTR = 1829;
    public static final long JAN_LIMIT_ATTR = 1612;
    public static final long FEB_LIMIT_ATTR = 1613;
    public static final long MAR_LIMIT_ATTR = 1614;
    public static final long APR_LIMIT_ATTR = 1617;
    public static final long MAY_LIMIT_ATTR = 1618;
    public static final long JUN_LIMIT_ATTR = 1619;
    public static final long JUL_LIMIT_ATTR = 1622;
    public static final long AUG_LIMIT_ATTR = 1623;
    public static final long SEP_LIMIT_ATTR = 1624;
    public static final long OCT_LIMIT_ATTR = 1627;
    public static final long NOV_LIMIT_ATTR = 1628;
    public static final long DEC_LIMIT_ATTR = 1629;
    public static final long JAN_BALANCE_ATTR = 3276;
    public static final long FEB_BALANCE_ATTR = 3278;
    public static final long MAR_BALANCE_ATTR = 3280;
    public static final long APR_BALANCE_ATTR = 3282;
    public static final long MAY_BALANCE_ATTR = 3284;
    public static final long JUN_BALANCE_ATTR = 3286;
    public static final long JUL_BALANCE_ATTR = 3288;
    public static final long AUG_BALANCE_ATTR = 3290;
    public static final long SEP_BALANCE_ATTR = 3292;
    public static final long OCT_BALANCE_ATTR = 3294;
    public static final long NOV_BALANCE_ATTR = 3296;
    public static final long DEC_BALANCE_ATTR = 3298;
    public static final long QUARTER_1_BAL_ATTR = 1616;
    public static final long QUARTER_2_BAL_ATTR = 1621;
    public static final long QUARTER_3_BAL_ATTR = 1626;
    public static final long QUARTER_4_BAL_ATTR = 1631;

    public static InstanceDto getCplCodesOnlyByCurrentYearRequestDto() {
        return InstanceDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(ViewType.DETAILED_FORM_VIEW)
                .attributes(List.of(
                        new StringAttribute(YEAR_ATTR),
                        new LongAttribute(KVSR_ATTR),
                        new LongAttribute(KFSR_ATTR),
                        new LinkedAttribute(KCSR_ATTR),
                        new LinkedAttribute(KVR_ATTR),
                        new LinkedAttribute(KOSGU_ATTR),
                        new LinkedAttribute(DOPFK_ATTR),
                        new LinkedAttribute(DOPEK_ATTR),
                        new LinkedAttribute(DOPKR_ATTR),
                        new LinkedAttribute(PURPOSE_ATTR)
                ))
                .filter(new Filter(List.of(
                        new LongFilterAttribute(YEAR_ATTR, (long) LocalDateTime.now().getYear()))))
                .build();
    }

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
