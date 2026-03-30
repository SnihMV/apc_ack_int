package su.petrosoft.apk_ack_integration.util;

import static su.petrosoft.apk_ack_integration.model.enums.SqlOperation.IN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.data.xml.rpl.PlDirectionLine;
import su.petrosoft.apk_ack_integration.model.dto.plicante.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.RequestedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.Filter;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.LinkedFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.LongFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.StringFilterAttribute;
import su.petrosoft.apk_ack_integration.model.enums.ViewType;

public class CashPlanLimitUtil {

    public static final long TEMPLATE_ID = 9460;
    public static final String TEMPLATE_TITLE = "Лимиты кассового плана";

    public static final long ID_ATTR = 1586;
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
    public static final long RECIPIENT_NAME = 4426;
    public static final long RECIPIENT_INN = 4427;
    public static final long RECIPIENT_KPP = 4428;
    public static final long TOTAL_LIMIT_ATTR = 1609;
    public static final long TOTAL_EXPENSE_ATTR = 1610;
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
    public static final long Q_1_BALANCE_ATTR = 1616;
    public static final long Q_2_BALANCE_ATTR = 1621;
    public static final long Q_3_BALANCE_ATTR = 1626;
    public static final long Q_4_BALANCE_ATTR = 1631;
    public static final long JAN_EXPENSE_ATTR = 3275;
    public static final long FEB_EXPENSE_ATTR = 3277;
    public static final long MAR_EXPENSE_ATTR = 3279;
    public static final long APR_EXPENSE_ATTR = 3281;
    public static final long MAY_EXPENSE_ATTR = 3283;
    public static final long JUN_EXPENSE_ATTR = 3285;
    public static final long JUL_EXPENSE_ATTR = 3287;
    public static final long AUG_EXPENSE_ATTR = 3289;
    public static final long SEP_EXPENSE_ATTR = 3291;
    public static final long OCT_EXPENSE_ATTR = 3293;
    public static final long NOV_EXPENSE_ATTR = 3295;
    public static final long DEC_EXPENSE_ATTR = 3297;
    public static final long Q_1_EXPENSE_ATTR = 1615;
    public static final long Q_2_EXPENSE_ATTR = 1620;
    public static final long Q_3_EXPENSE_ATTR = 1625;
    public static final long Q_4_EXPENSE_ATTR = 1630;

    public static GetAttributesListRequestDto requestDtoToGettingCplEqualsFieldsByCurrentYear() {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(ViewType.DETAILED_FORM_VIEW)
                .attributes(getEqualsFieldsAttributes())
                .filter(new Filter(List.of(
                        new LongFilterAttribute(YEAR_ATTR, LocalDate.now().getYear())
                )))
                .build();
    }

    public static GetAttributesListRequestDto requestDtoToGetCplByIdents(CashPlanLimit cpl) {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(ViewType.DETAILED_FORM_VIEW)
                .filter(new Filter(List.of(
                        new LongFilterAttribute(YEAR_ATTR, LocalDate.now().getYear()),
                        new LinkedFilterAttribute(KVSR_ATTR, cpl.getKvsr()),
                        new LinkedFilterAttribute(KFSR_ATTR, cpl.getKfsr()),
                        new LinkedFilterAttribute(KCSR_ATTR, cpl.getKcsr()),
                        new LinkedFilterAttribute(KVR_ATTR, cpl.getKvr()),
                        new LinkedFilterAttribute(KOSGU_ATTR, cpl.getKosgu()),
                        new LinkedFilterAttribute(DOPFK_ATTR, cpl.getDopFk()),
                        new LinkedFilterAttribute(DOPEK_ATTR, cpl.getDopEk()),
                        new LinkedFilterAttribute(DOPKR_ATTR, cpl.getDopKr()),
                        new LinkedFilterAttribute(PURPOSE_ATTR, cpl.getPurpose()),
                        new StringFilterAttribute(RECIPIENT_KPP, cpl.getRecipientKpp()),
                        new StringFilterAttribute(RECIPIENT_INN, cpl.getRecipientInn())
                )))
                .build();
    }

    public static GetAttributesListRequestDto requestDtoToGettingCplEqualsFieldsByIds(Collection<Long> ids) {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(ViewType.DETAILED_FORM_VIEW)
                .attributes(getEqualsFieldsAttributes())
                .filter(new Filter(List.of(
                        new LongFilterAttribute(ID_ATTR, List.of(IN), ids)
                )))
                .build();
    }

    public static GetAttributesListRequestDto requestDtoToGettingExpenseFieldsById(long id) {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(ViewType.DETAILED_FORM_VIEW)
                .attributes(List.of(
                        new RequestedAttribute(TOTAL_LIMIT_ATTR),
                        new RequestedAttribute(FEDERAL_BUDGET_ATTR),
                        new RequestedAttribute(REGIONAL_BUDGET_ATTR),
                        new RequestedAttribute(JAN_LIMIT_ATTR),
                        new RequestedAttribute(FEB_LIMIT_ATTR),
                        new RequestedAttribute(MAR_LIMIT_ATTR),
                        new RequestedAttribute(APR_LIMIT_ATTR),
                        new RequestedAttribute(MAY_LIMIT_ATTR),
                        new RequestedAttribute(JUN_LIMIT_ATTR),
                        new RequestedAttribute(JUL_LIMIT_ATTR),
                        new RequestedAttribute(AUG_LIMIT_ATTR),
                        new RequestedAttribute(SEP_LIMIT_ATTR),
                        new RequestedAttribute(OCT_LIMIT_ATTR),
                        new RequestedAttribute(NOV_LIMIT_ATTR),
                        new RequestedAttribute(DEC_LIMIT_ATTR),
                        new RequestedAttribute(JAN_BALANCE_ATTR),
                        new RequestedAttribute(FEB_BALANCE_ATTR),
                        new RequestedAttribute(MAR_BALANCE_ATTR),
                        new RequestedAttribute(APR_BALANCE_ATTR),
                        new RequestedAttribute(MAY_BALANCE_ATTR),
                        new RequestedAttribute(JUN_BALANCE_ATTR),
                        new RequestedAttribute(JUL_BALANCE_ATTR),
                        new RequestedAttribute(AUG_BALANCE_ATTR),
                        new RequestedAttribute(SEP_BALANCE_ATTR),
                        new RequestedAttribute(OCT_BALANCE_ATTR),
                        new RequestedAttribute(NOV_BALANCE_ATTR),
                        new RequestedAttribute(DEC_BALANCE_ATTR),
                        new RequestedAttribute(JAN_EXPENSE_ATTR),
                        new RequestedAttribute(FEB_EXPENSE_ATTR),
                        new RequestedAttribute(MAR_EXPENSE_ATTR),
                        new RequestedAttribute(APR_EXPENSE_ATTR),
                        new RequestedAttribute(MAY_EXPENSE_ATTR),
                        new RequestedAttribute(JUN_EXPENSE_ATTR),
                        new RequestedAttribute(JUL_EXPENSE_ATTR),
                        new RequestedAttribute(AUG_EXPENSE_ATTR),
                        new RequestedAttribute(SEP_EXPENSE_ATTR),
                        new RequestedAttribute(OCT_EXPENSE_ATTR),
                        new RequestedAttribute(NOV_EXPENSE_ATTR),
                        new RequestedAttribute(DEC_EXPENSE_ATTR)
                ))
                .filter(new Filter(List.of(
                        new LongFilterAttribute(ID_ATTR, id)
                )))
                .build();
    }

    public static CashPlanLimit recalculateValues(CashPlanLimit dst, CashPlanLimit src) {

        BigDecimal janBal = src.getJanLimit().subtract(dst.getJanExpense());
        BigDecimal febBal = src.getFebLimit().subtract(dst.getFebExpense());
        BigDecimal marBal = src.getMarLimit().subtract(dst.getMarExpense());
        BigDecimal aprBal = src.getAprLimit().subtract(dst.getAprExpense());
        BigDecimal mayBal = src.getMayLimit().subtract(dst.getMayExpense());
        BigDecimal junBal = src.getJunLimit().subtract(dst.getJunExpense());
        BigDecimal julBal = src.getJulLimit().subtract(dst.getJulExpense());
        BigDecimal augBal = src.getAugLimit().subtract(dst.getAugExpense());
        BigDecimal sepBal = src.getSepLimit().subtract(dst.getSepExpense());
        BigDecimal octBal = src.getOctLimit().subtract(dst.getOctExpense());
        BigDecimal novBal = src.getNovLimit().subtract(dst.getNovExpense());
        BigDecimal decBal = src.getDecLimit().subtract(dst.getDecExpense());
        BigDecimal fstQrtExpense = dst.getJanExpense().add(dst.getFebExpense()).add(dst.getMarExpense());
        BigDecimal scdQrtExpense = dst.getAprExpense().add(dst.getMayExpense()).add(dst.getJunExpense());
        BigDecimal trdQrtExpense = dst.getJulExpense().add(dst.getAugExpense()).add(dst.getSepExpense());
        BigDecimal frtQrtExpense = dst.getOctExpense().add(dst.getNovExpense()).add(dst.getDecExpense());
        BigDecimal fstQrtBalance = janBal.add(febBal).add(marBal);
        BigDecimal scdQrtBalance = aprBal.add(mayBal).add(junBal);
        BigDecimal trdQrtBalance = julBal.add(augBal).add(sepBal);
        BigDecimal frtQrtBalance = octBal.add(novBal).add(decBal);
        BigDecimal totalBalance = fstQrtBalance.add(scdQrtBalance).add(trdQrtBalance).add(frtQrtBalance);

        return CashPlanLimit.builder()
                .id(dst.getId())
                .version(dst.getVersion())
                .totalLimit(src.getTotalLimit())
                .totalBalance(totalBalance)
                .federalBudget(src.getFederalBudget())
                .regionalBudget(src.getRegionalBudget())
                .janLimit(src.getJanLimit())
                .febLimit(src.getFebLimit())
                .marLimit(src.getMarLimit())
                .aprLimit(src.getAprLimit())
                .mayLimit(src.getMayLimit())
                .junLimit(src.getJunLimit())
                .julLimit(src.getJulLimit())
                .augLimit(src.getAugLimit())
                .sepLimit(src.getSepLimit())
                .octLimit(src.getOctLimit())
                .novLimit(src.getNovLimit())
                .decLimit(src.getDecLimit())
                .janExpense(dst.getJanExpense())
                .febExpense(dst.getFebExpense())
                .marExpense(dst.getMarExpense())
                .aprExpense(dst.getAprExpense())
                .mayExpense(dst.getMayExpense())
                .junExpense(dst.getJunExpense())
                .julExpense(dst.getJulExpense())
                .augExpense(dst.getAugExpense())
                .sepExpense(dst.getSepExpense())
                .octExpense(dst.getOctExpense())
                .novExpense(dst.getNovExpense())
                .decExpense(dst.getDecExpense())
                .fstQuarterExpense(fstQrtExpense)
                .scdQuarterExpense(scdQrtExpense)
                .trdQuarterExpense(trdQrtExpense)
                .frtQuarterExpense(frtQrtExpense)
                .janBalance(janBal)
                .febBalance(febBal)
                .marBalance(marBal)
                .aprBalance(aprBal)
                .mayBalance(mayBal)
                .junBalance(junBal)
                .julBalance(julBal)
                .augBalance(augBal)
                .sepBalance(sepBal)
                .octBalance(octBal)
                .novBalance(novBal)
                .decBalance(decBal)
                .fstQuarterBalance(fstQrtBalance)
                .scdQuarterBalance(scdQrtBalance)
                .trdQuarterBalance(trdQrtBalance)
                .frtQuarterBalance(frtQrtBalance)
                .build();
    }

    public static BigDecimal getTotalLimit(PlDirectionLine line) {
        return line.limitAmt1().add(line.limitAmt2()).add(line.limitAmt3());
    }

    public static BigDecimal getTotalFederal(PlDirectionLine line) {
        return line.limitFederalAmt1().add(line.limitFederalAmt2().add(line.limitFederalAmt3()));
    }

    public static BigDecimal getTotalRegional(PlDirectionLine line) {
        return line.limitRegionalAmt1().add(line.limitRegionalAmt2()).add(line.limitRegionalAmt3());
    }

    public static BigDecimal sumOf(Double... items) {
        return Stream.of(items)
                .filter(Objects::nonNull)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static List<RequestedAttribute> getEqualsFieldsAttributes() {
        return List.of(
                new RequestedAttribute(YEAR_ATTR),
                new RequestedAttribute(KVSR_ATTR),
                new RequestedAttribute(KFSR_ATTR),
                new RequestedAttribute(KCSR_ATTR),
                new RequestedAttribute(KVR_ATTR),
                new RequestedAttribute(KOSGU_ATTR),
                new RequestedAttribute(DOPFK_ATTR),
                new RequestedAttribute(DOPEK_ATTR),
                new RequestedAttribute(DOPKR_ATTR),
                new RequestedAttribute(PURPOSE_ATTR),
                new RequestedAttribute(RECIPIENT_INN),
                new RequestedAttribute(RECIPIENT_KPP)
        );
    }
}
