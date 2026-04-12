package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.data.xml.rpl.PlDirectionLine;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.RequestedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.*;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.Filter;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.LongFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.StringFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.ViewType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static su.petrosoft.apk_ack_integration.model.enums.SqlOperation.IN;

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

    public static GetAttributesListRequestDto requestDtoToGetLimitsByYear(long year) {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(ViewType.DETAILED_FORM_VIEW)
                .filter(new Filter(List.of(
                        new LongFilterAttribute(YEAR_ATTR, year)
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

    public static CreateInstanceRequestDto requestDtoToCreateCpl(CashPlanLimit limit) {
        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(TEMPLATE_ID)
                        .attributes(getAttributesToCreate(limit))
                        .build()
        );
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

    public static UpdateInstanceRequestDto requestDtoForUpdate(long id, long version, List<Attribute<?>> attributes) {
        return new UpdateInstanceRequestDto(
                InstanceDto.builder()
                        .id(id)
                        .version(version)
                        .attributes(attributes)
                        .build()
        );
    }

    public static List<Attribute<?>> getAttributesToCreate(CashPlanLimit creator) {
        if (creator.getYear() == null || creator.getKfsr() == null || creator.getKcsr() == null ||
                creator.getKvr() == null || creator.getKosgu() == null || creator.getKvsr() == null ||
                creator.getDopFk() == null || creator.getDopEk() == null || creator.getDopKr() == null ||
                creator.getPurpose() == null || creator.getRecipientInn() == null || creator.getRecipientKpp() == null
        ) {
            throw new IllegalStateException("Обязательный атрибут объекта не инициализирован");
        }
        List<Attribute<?>> attrs = new ArrayList<>(getIdentAttributes(creator));
        processField(attrs, creator.getFederalBudget(), FEDERAL_BUDGET_ATTR);
        processField(attrs, creator.getRegionalBudget(), REGIONAL_BUDGET_ATTR);
        processField(attrs, creator.getTotalLimit(), TOTAL_LIMIT_ATTR, TOTAL_BALANCE_ATTR);
        processField(attrs, creator.getJanLimit(), JAN_LIMIT_ATTR, JAN_BALANCE_ATTR);
        processField(attrs, creator.getFebLimit(), FEB_LIMIT_ATTR, FEB_BALANCE_ATTR);
        processField(attrs, creator.getMarLimit(), MAR_LIMIT_ATTR, MAR_BALANCE_ATTR);
        processField(attrs, creator.getFstQuarterBalance(), Q_1_BALANCE_ATTR);
        processField(attrs, creator.getAprLimit(), APR_LIMIT_ATTR, APR_BALANCE_ATTR);
        processField(attrs, creator.getMayLimit(), MAY_LIMIT_ATTR, MAY_BALANCE_ATTR);
        processField(attrs, creator.getJunLimit(), JUN_LIMIT_ATTR, JUN_BALANCE_ATTR);
        processField(attrs, creator.getScdQuarterBalance(), Q_2_BALANCE_ATTR);
        processField(attrs, creator.getJulLimit(), JUL_LIMIT_ATTR, JUL_BALANCE_ATTR);
        processField(attrs, creator.getAugLimit(), AUG_LIMIT_ATTR, AUG_BALANCE_ATTR);
        processField(attrs, creator.getSepLimit(), SEP_LIMIT_ATTR, SEP_BALANCE_ATTR);
        processField(attrs, creator.getTrdQuarterBalance(), Q_3_BALANCE_ATTR);
        processField(attrs, creator.getOctLimit(), OCT_LIMIT_ATTR, OCT_BALANCE_ATTR);
        processField(attrs, creator.getNovLimit(), NOV_LIMIT_ATTR, NOV_BALANCE_ATTR);
        processField(attrs, creator.getDecLimit(), DEC_LIMIT_ATTR, DEC_BALANCE_ATTR);
        processField(attrs, creator.getFrtQuarterBalance(), Q_4_BALANCE_ATTR);
        return attrs;
    }

    private static List<Attribute<?>> getIdentAttributes(CashPlanLimit creator) {
        return List.of(
                new LongAttribute(YEAR_ATTR, creator.getYear()),
                new LinkedAttribute(KVSR_ATTR, creator.getKvsr()),
                new LinkedAttribute(KFSR_ATTR, creator.getKfsr()),
                new LinkedAttribute(KCSR_ATTR, creator.getKcsr()),
                new LinkedAttribute(KVR_ATTR, creator.getKvr()),
                new LinkedAttribute(KOSGU_ATTR, creator.getKosgu()),
                new LinkedAttribute(DOPFK_ATTR, creator.getDopFk()),
                new LinkedAttribute(DOPEK_ATTR, creator.getDopEk()),
                new LinkedAttribute(DOPKR_ATTR, creator.getDopKr()),
                new LinkedAttribute(PURPOSE_ATTR, creator.getPurpose()),
                new StringAttribute(RECIPIENT_NAME, creator.getRecipientName()),
                new StringAttribute(RECIPIENT_INN, creator.getRecipientInn()),
                new StringAttribute(RECIPIENT_KPP, creator.getRecipientKpp())
        );
    }

    private static void processField(List<Attribute<?>> result, BigDecimal value, long... attrs) {
        if (value != null && value.compareTo(BigDecimal.ZERO) != 0) {
            for (long attr : attrs) {
                result.add(new DoubleAttribute(attr, value));
            }
        }
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
