package su.petrosoft.apk_ack_integration.util;

import static su.petrosoft.apk_ack_integration.model.enums.SqlOperation.IN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.data.xml.rpl.PlDirectionLine;
import su.petrosoft.apk_ack_integration.model.dto.plicante.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.RequestedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DoubleAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.Filter;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.LongFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.StringFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
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

    public static GetAttributesListRequestDto requestDtoToGetCplById(long id) {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(ViewType.DETAILED_FORM_VIEW)
                .attributes(identAttributes())
                .filter(new Filter(List.of(
                        new LongFilterAttribute(ID_ATTR, id)
                )))
                .build();
    }

    public static GetAttributesListRequestDto requestDtoToGetCplIdentAttrsByCurrentYear() {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(ViewType.DETAILED_FORM_VIEW)
                .attributes(identAttributes())
                .filter(new Filter(List.of(
                        new LongFilterAttribute(YEAR_ATTR, LocalDate.now().getYear())
                )))
                .build();
    }

    public static GetAttributesListRequestDto requestDtoToGetCplIdentAttrsByYearAndInn(long year, String recipientInn) {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(ViewType.DETAILED_FORM_VIEW)
                .attributes(identAttributes())
                .filter(new Filter(List.of(
                        new LongFilterAttribute(YEAR_ATTR, year),
                        new StringFilterAttribute(RECIPIENT_INN, recipientInn)
                )))
                .build();
    }

    public static GetAttributesListRequestDto requestDtoToGettingCplEqualsFieldsByIds(Collection<Long> ids) {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(ViewType.DETAILED_FORM_VIEW)
                .attributes(identAttributes())
                .filter(new Filter(List.of(
                        new LongFilterAttribute(ID_ATTR, List.of(IN), ids)
                )))
                .build();
    }

    public static GetAttributesListRequestDto requestDtoToGetMonetaryFieldsById(long id) {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(ViewType.DETAILED_FORM_VIEW)
                .attributes(monetaryAttributes())
                .filter(new Filter(List.of(
                        new LongFilterAttribute(ID_ATTR, id)
                )))
                .build();
    }

    public static UpdateInstanceRequestDto requestDtoForUpdate(CashPlanLimit cpl, List<Attribute<?>> attributes) {
        return new UpdateInstanceRequestDto(
                InstanceDto.builder()
                        .id(cpl.getId())
                        .version(cpl.getVersion())
                        .attributes(attributes)
                        .build()
        );
    }

    public static boolean hasDifference(CashPlanLimit oldCpl, CashPlanLimit newCpl) {

        return oldCpl.getTotalLimit().compareTo(newCpl.getTotalLimit()) != 0
                || oldCpl.getFederalBudget().compareTo(newCpl.getFederalBudget()) != 0
                || oldCpl.getRegionalBudget().compareTo(newCpl.getRegionalBudget()) != 0
                || oldCpl.getJanLimit().compareTo(newCpl.getJanLimit()) != 0
                || oldCpl.getFebLimit().compareTo(newCpl.getFebLimit()) != 0
                || oldCpl.getMarLimit().compareTo(newCpl.getMarLimit()) != 0
                || oldCpl.getAprLimit().compareTo(newCpl.getAprLimit()) != 0
                || oldCpl.getMayLimit().compareTo(newCpl.getMayLimit()) != 0
                || oldCpl.getJunLimit().compareTo(newCpl.getJunLimit()) != 0
                || oldCpl.getJulLimit().compareTo(newCpl.getJulLimit()) != 0
                || oldCpl.getAugLimit().compareTo(newCpl.getAugLimit()) != 0
                || oldCpl.getSepLimit().compareTo(newCpl.getSepLimit()) != 0
                || oldCpl.getOctLimit().compareTo(newCpl.getOctLimit()) != 0
                || oldCpl.getNovLimit().compareTo(newCpl.getNovLimit()) != 0
                || oldCpl.getDecLimit().compareTo(newCpl.getDecLimit()) != 0;
    }

    public static List<Attribute<?>> getAttributesToUpdate(CashPlanLimit existed, CashPlanLimit updater) {
        List<Attribute<?>> attributes = new ArrayList<>();
        if (existed.getTotalLimit().compareTo(updater.getTotalLimit()) != 0) {
            attributes.add(new DoubleAttribute(TOTAL_LIMIT_ATTR, updater.getTotalLimit()));
        }
        if (existed.getFederalBudget().compareTo(updater.getFederalBudget()) != 0) {
            attributes.add(new DoubleAttribute(FEDERAL_BUDGET_ATTR, updater.getFederalBudget()));
        }
        if (existed.getRegionalBudget().compareTo(updater.getRegionalBudget()) != 0) {
            attributes.add(new DoubleAttribute(REGIONAL_BUDGET_ATTR, updater.getRegionalBudget()));
        }
        BigDecimal totalBalance = BigDecimal.ZERO.add(
                processFstQuarter(existed, updater, attributes)).add(
                processScdQuarter(existed, updater, attributes)).add(
                processTrdQuarter(existed, updater, attributes)).add(
                processFrtQuarter(existed, updater, attributes));
        if (existed.getTotalBalance().compareTo(totalBalance) != 0) {
            attributes.add(new DoubleAttribute(TOTAL_BALANCE_ATTR, totalBalance));
        }
        return attributes;
    }

    private static BigDecimal processFstQuarter(CashPlanLimit existed, CashPlanLimit updater, List<Attribute<?>> attributes) {
        boolean quarterUpdated = false;
        BigDecimal quarterBalance = BigDecimal.ZERO;
        if (existed.getJanLimit().compareTo(updater.getJanLimit()) != 0) {
            attributes.add(new DoubleAttribute(JAN_LIMIT_ATTR, updater.getJanLimit()));
            BigDecimal janBalance = updater.getJanLimit().subtract(existed.getJanExpense());
            attributes.add(new DoubleAttribute(JAN_BALANCE_ATTR, janBalance));
            quarterBalance = quarterBalance.add(janBalance);
            quarterUpdated = true;
        } else {
            quarterBalance = quarterBalance.add(existed.getJanBalance());
        }
        if (existed.getFebLimit().compareTo(updater.getFebLimit()) != 0) {
            attributes.add(new DoubleAttribute(FEB_LIMIT_ATTR, updater.getFebLimit()));
            BigDecimal febBalance = updater.getFebLimit().subtract(existed.getFebExpense());
            attributes.add(new DoubleAttribute(FEB_BALANCE_ATTR, febBalance));
            quarterBalance = quarterBalance.add(febBalance);
            quarterUpdated = true;
        } else {
            quarterBalance = quarterBalance.add(existed.getFebBalance());
        }
        if (existed.getMarLimit().compareTo(updater.getMarLimit()) != 0) {
            attributes.add(new DoubleAttribute(MAR_LIMIT_ATTR, updater.getMarLimit()));
            BigDecimal marBalance = updater.getMarLimit().subtract(existed.getMarExpense());
            attributes.add(new DoubleAttribute(MAR_BALANCE_ATTR, marBalance));
            quarterBalance = quarterBalance.add(marBalance);
            quarterUpdated = true;
        } else {
            quarterBalance = quarterBalance.add(existed.getMarBalance());
        }
        if (quarterUpdated) {
            attributes.add(new DoubleAttribute(Q_1_BALANCE_ATTR, quarterBalance));
        }
        return quarterBalance;
    }

    private static BigDecimal processScdQuarter(CashPlanLimit existed, CashPlanLimit updater, List<Attribute<?>> attributes) {
        boolean quarterUpdated = false;
        BigDecimal quarterBalance = BigDecimal.ZERO;
        if (existed.getAprLimit().compareTo(updater.getAprLimit()) != 0) {
            attributes.add(new DoubleAttribute(APR_LIMIT_ATTR, updater.getAprLimit()));
            BigDecimal aprBalance = updater.getAprLimit().subtract(existed.getAprExpense());
            attributes.add(new DoubleAttribute(APR_BALANCE_ATTR, aprBalance));
            quarterBalance = quarterBalance.add(aprBalance);
            quarterUpdated = true;
        } else {
            quarterBalance = quarterBalance.add(existed.getAprBalance());
        }
        if (existed.getMayLimit().compareTo(updater.getMayLimit()) != 0) {
            attributes.add(new DoubleAttribute(MAY_LIMIT_ATTR, updater.getMayLimit()));
            BigDecimal mayBalance = updater.getMayLimit().subtract(existed.getMayExpense());
            attributes.add(new DoubleAttribute(MAY_BALANCE_ATTR, mayBalance));
            quarterBalance = quarterBalance.add(mayBalance);
            quarterUpdated = true;
        } else {
            quarterBalance = quarterBalance.add(existed.getMayBalance());
        }
        if (existed.getJunLimit().compareTo(updater.getJunLimit()) != 0) {
            attributes.add(new DoubleAttribute(JUN_LIMIT_ATTR, updater.getJunLimit()));
            BigDecimal junBalance = updater.getJunLimit().subtract(existed.getJunExpense());
            attributes.add(new DoubleAttribute(JUN_BALANCE_ATTR, junBalance));
            quarterBalance = quarterBalance.add(junBalance);
            quarterUpdated = true;
        } else {
            quarterBalance = quarterBalance.add(existed.getJunBalance());
        }
        if (quarterUpdated) {
            attributes.add(new DoubleAttribute(Q_2_BALANCE_ATTR, quarterBalance));
        }
        return quarterBalance;
    }

    private static BigDecimal processTrdQuarter(CashPlanLimit existed, CashPlanLimit updater, List<Attribute<?>> attributes) {
        boolean quarterUpdated = false;
        BigDecimal quarterBalance = BigDecimal.ZERO;
        if (existed.getJulLimit().compareTo(updater.getJulLimit()) != 0) {
            attributes.add(new DoubleAttribute(JUL_LIMIT_ATTR, updater.getJulLimit()));
            BigDecimal julBalance = updater.getJulLimit().subtract(existed.getJulExpense());
            attributes.add(new DoubleAttribute(JUL_BALANCE_ATTR, julBalance));
            quarterBalance = quarterBalance.add(julBalance);
            quarterUpdated = true;
        } else {
            quarterBalance = quarterBalance.add(existed.getJulBalance());
        }
        if (existed.getAugLimit().compareTo(updater.getAugLimit()) != 0) {
            attributes.add(new DoubleAttribute(AUG_LIMIT_ATTR, updater.getAugLimit()));
            BigDecimal augBalance = updater.getAugLimit().subtract(existed.getAugExpense());
            attributes.add(new DoubleAttribute(AUG_BALANCE_ATTR, augBalance));
            quarterBalance = quarterBalance.add(augBalance);
            quarterUpdated = true;
        } else {
            quarterBalance = quarterBalance.add(existed.getAugBalance());
        }
        if (existed.getSepLimit().compareTo(updater.getSepLimit()) != 0) {
            attributes.add(new DoubleAttribute(SEP_LIMIT_ATTR, updater.getSepLimit()));
            BigDecimal sepBalance = updater.getSepLimit().subtract(existed.getSepExpense());
            attributes.add(new DoubleAttribute(SEP_BALANCE_ATTR, sepBalance));
            quarterBalance = quarterBalance.add(sepBalance);
            quarterUpdated = true;
        } else {
            quarterBalance = quarterBalance.add(existed.getSepBalance());
        }
        if (quarterUpdated) {
            attributes.add(new DoubleAttribute(Q_3_BALANCE_ATTR, quarterBalance));
        }
        return quarterBalance;
    }

    private static BigDecimal processFrtQuarter(CashPlanLimit existed, CashPlanLimit updater, List<Attribute<?>> attributes) {
        boolean quarterUpdated = false;
        BigDecimal quarterBalance = BigDecimal.ZERO;
        if (existed.getOctLimit().compareTo(updater.getOctLimit()) != 0) {
            attributes.add(new DoubleAttribute(OCT_LIMIT_ATTR, updater.getOctLimit()));
            BigDecimal octBalance = updater.getOctLimit().subtract(existed.getOctExpense());
            attributes.add(new DoubleAttribute(OCT_BALANCE_ATTR, octBalance));
            quarterBalance = quarterBalance.add(octBalance);
            quarterUpdated = true;
        } else {
            quarterBalance = quarterBalance.add(existed.getOctBalance());
        }
        if (existed.getNovLimit().compareTo(updater.getNovLimit()) != 0) {
            attributes.add(new DoubleAttribute(NOV_LIMIT_ATTR, updater.getNovLimit()));
            BigDecimal novBalance = updater.getNovLimit().subtract(existed.getNovExpense());
            attributes.add(new DoubleAttribute(NOV_BALANCE_ATTR, novBalance));
            quarterBalance = quarterBalance.add(novBalance);
            quarterUpdated = true;
        } else {
            quarterBalance = quarterBalance.add(existed.getNovBalance());
        }
        if (existed.getDecLimit().compareTo(updater.getDecLimit()) != 0) {
            attributes.add(new DoubleAttribute(DEC_LIMIT_ATTR, updater.getDecLimit()));
            BigDecimal decBalance = updater.getDecLimit().subtract(existed.getDecExpense());
            attributes.add(new DoubleAttribute(DEC_BALANCE_ATTR, updater.getDecBalance()));
            quarterBalance = quarterBalance.add(decBalance);
            quarterUpdated = true;
        } else {
            quarterBalance = quarterBalance.add(existed.getDecBalance());
        }
        if (quarterUpdated) {
            attributes.add(new DoubleAttribute(Q_4_BALANCE_ATTR, quarterBalance));
        }
        return quarterBalance;
    }

    public static CashPlanLimit recalculateValues(CashPlanLimit oldCpl, CashPlanLimit newCpl) {

        BigDecimal janBal = newCpl.getJanLimit().subtract(oldCpl.getJanExpense());
        BigDecimal febBal = newCpl.getFebLimit().subtract(oldCpl.getFebExpense());
        BigDecimal marBal = newCpl.getMarLimit().subtract(oldCpl.getMarExpense());
        BigDecimal aprBal = newCpl.getAprLimit().subtract(oldCpl.getAprExpense());
        BigDecimal mayBal = newCpl.getMayLimit().subtract(oldCpl.getMayExpense());
        BigDecimal junBal = newCpl.getJunLimit().subtract(oldCpl.getJunExpense());
        BigDecimal julBal = newCpl.getJulLimit().subtract(oldCpl.getJulExpense());
        BigDecimal augBal = newCpl.getAugLimit().subtract(oldCpl.getAugExpense());
        BigDecimal sepBal = newCpl.getSepLimit().subtract(oldCpl.getSepExpense());
        BigDecimal octBal = newCpl.getOctLimit().subtract(oldCpl.getOctExpense());
        BigDecimal novBal = newCpl.getNovLimit().subtract(oldCpl.getNovExpense());
        BigDecimal decBal = newCpl.getDecLimit().subtract(oldCpl.getDecExpense());
        BigDecimal fstQrtExpense = oldCpl.getJanExpense().add(oldCpl.getFebExpense()).add(oldCpl.getMarExpense());
        BigDecimal scdQrtExpense = oldCpl.getAprExpense().add(oldCpl.getMayExpense()).add(oldCpl.getJunExpense());
        BigDecimal trdQrtExpense = oldCpl.getJulExpense().add(oldCpl.getAugExpense()).add(oldCpl.getSepExpense());
        BigDecimal frtQrtExpense = oldCpl.getOctExpense().add(oldCpl.getNovExpense()).add(oldCpl.getDecExpense());
        BigDecimal fstQrtBalance = janBal.add(febBal).add(marBal);
        BigDecimal scdQrtBalance = aprBal.add(mayBal).add(junBal);
        BigDecimal trdQrtBalance = julBal.add(augBal).add(sepBal);
        BigDecimal frtQrtBalance = octBal.add(novBal).add(decBal);
        BigDecimal totalBalance = fstQrtBalance.add(scdQrtBalance).add(trdQrtBalance).add(frtQrtBalance);

        return CashPlanLimit.builder()
                .id(oldCpl.getId())
                .version(oldCpl.getVersion())
                .totalLimit(newCpl.getTotalLimit())
                .totalBalance(totalBalance)
                .federalBudget(newCpl.getFederalBudget())
                .regionalBudget(newCpl.getRegionalBudget())
                .janLimit(newCpl.getJanLimit())
                .febLimit(newCpl.getFebLimit())
                .marLimit(newCpl.getMarLimit())
                .aprLimit(newCpl.getAprLimit())
                .mayLimit(newCpl.getMayLimit())
                .junLimit(newCpl.getJunLimit())
                .julLimit(newCpl.getJulLimit())
                .augLimit(newCpl.getAugLimit())
                .sepLimit(newCpl.getSepLimit())
                .octLimit(newCpl.getOctLimit())
                .novLimit(newCpl.getNovLimit())
                .decLimit(newCpl.getDecLimit())
                .janExpense(oldCpl.getJanExpense())
                .febExpense(oldCpl.getFebExpense())
                .marExpense(oldCpl.getMarExpense())
                .aprExpense(oldCpl.getAprExpense())
                .mayExpense(oldCpl.getMayExpense())
                .junExpense(oldCpl.getJunExpense())
                .julExpense(oldCpl.getJulExpense())
                .augExpense(oldCpl.getAugExpense())
                .sepExpense(oldCpl.getSepExpense())
                .octExpense(oldCpl.getOctExpense())
                .novExpense(oldCpl.getNovExpense())
                .decExpense(oldCpl.getDecExpense())
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

    private static List<RequestedAttribute> identAttributes() {
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

    private static List<RequestedAttribute> monetaryAttributes() {
        return List.of(
                new RequestedAttribute(TOTAL_LIMIT_ATTR),
                new RequestedAttribute(TOTAL_BALANCE_ATTR),
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
        );
    }
}
