package su.petrosoft.apk_ack_integration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CashPlanLimit extends PlicanteInstance {

    private Long year;
    private String kfsr;
    private String kcsr;
    private String kvr;
    private String kosgu;
    private String kvsr;
    private String dopFk;
    private String dopEk;
    private String dopKr;
    private String purpose;
    private String recipientName;
    private String recipientInn;
    private String recipientKpp;
    private BigDecimal totalLimit;
    private BigDecimal totalExpense;
    private BigDecimal totalBalance;
    private BigDecimal federalBudget;
    private BigDecimal regionalBudget;
    private BigDecimal janLimit;
    private BigDecimal febLimit;
    private BigDecimal marLimit;
    private BigDecimal aprLimit;
    private BigDecimal mayLimit;
    private BigDecimal junLimit;
    private BigDecimal julLimit;
    private BigDecimal augLimit;
    private BigDecimal sepLimit;
    private BigDecimal octLimit;
    private BigDecimal novLimit;
    private BigDecimal decLimit;
    private BigDecimal janExpense;
    private BigDecimal febExpense;
    private BigDecimal marExpense;
    private BigDecimal aprExpense;
    private BigDecimal mayExpense;
    private BigDecimal junExpense;
    private BigDecimal julExpense;
    private BigDecimal augExpense;
    private BigDecimal sepExpense;
    private BigDecimal octExpense;
    private BigDecimal novExpense;
    private BigDecimal decExpense;
    private BigDecimal fstQuarterExpense;
    private BigDecimal scdQuarterExpense;
    private BigDecimal trdQuarterExpense;
    private BigDecimal frtQuarterExpense;
    private BigDecimal janBalance;
    private BigDecimal febBalance;
    private BigDecimal marBalance;
    private BigDecimal aprBalance;
    private BigDecimal mayBalance;
    private BigDecimal junBalance;
    private BigDecimal julBalance;
    private BigDecimal augBalance;
    private BigDecimal sepBalance;
    private BigDecimal octBalance;
    private BigDecimal novBalance;
    private BigDecimal decBalance;
    private BigDecimal fstQuarterBalance;
    private BigDecimal scdQuarterBalance;
    private BigDecimal trdQuarterBalance;
    private BigDecimal frtQuarterBalance;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CashPlanLimit that = (CashPlanLimit) o;
        return Objects.equals(year, that.year) && Objects.equals(kfsr, that.kfsr)
               && Objects.equals(kcsr, that.kcsr) && Objects.equals(kvr, that.kvr)
               && Objects.equals(kosgu, that.kosgu) && Objects.equals(kvsr, that.kvsr)
               && Objects.equals(dopFk, that.dopFk) && Objects.equals(dopEk, that.dopEk)
               && Objects.equals(dopKr, that.dopKr) && Objects.equals(purpose, that.purpose)
               && Objects.equals(recipientInn, that.recipientInn) && Objects.equals(recipientKpp, that.recipientKpp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, kfsr, kcsr, kvr, kosgu, kvsr, dopFk, dopEk, dopKr, purpose, recipientInn, recipientKpp);
    }
}
