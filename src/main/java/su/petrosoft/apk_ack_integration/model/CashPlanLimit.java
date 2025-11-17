package su.petrosoft.apk_ack_integration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashPlanLimit {
    private Long id;
    private Long version;
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
    private BigDecimal limitTotalAmt;
    private BigDecimal limitFederalAmt;
    private BigDecimal limitRegionalAmt;
    private BigDecimal spendTotal;
    private BigDecimal remainTotal;
    private BigDecimal m01Amt;
    private BigDecimal m02Amt;
    private BigDecimal m03Amt;
    private BigDecimal m04Amt;
    private BigDecimal m05Amt;
    private BigDecimal m06Amt;
    private BigDecimal m07Amt;
    private BigDecimal m08Amt;
    private BigDecimal m09Amt;
    private BigDecimal m10Amt;
    private BigDecimal m11Amt;
    private BigDecimal m12Amt;
    private BigDecimal s01Amt;
    private BigDecimal s02Amt;
    private BigDecimal s03Amt;
    private BigDecimal sKv1Amt;
    private BigDecimal s04Amt;
    private BigDecimal s05Amt;
    private BigDecimal s06Amt;
    private BigDecimal sKv2Amt;
    private BigDecimal s07Amt;
    private BigDecimal s08Amt;
    private BigDecimal s09Amt;
    private BigDecimal sKv3Amt;
    private BigDecimal s10Amt;
    private BigDecimal s11Amt;
    private BigDecimal s12Amt;
    private BigDecimal sKv4Amt;
    private BigDecimal r01Amt;
    private BigDecimal r02Amt;
    private BigDecimal r03Amt;
    private BigDecimal rKv1Amt;
    private BigDecimal r04Amt;
    private BigDecimal r05Amt;
    private BigDecimal r06Amt;
    private BigDecimal rKv2Amt;
    private BigDecimal r07Amt;
    private BigDecimal r08Amt;
    private BigDecimal r09Amt;
    private BigDecimal rKv3Amt;
    private BigDecimal r10Amt;
    private BigDecimal r11Amt;
    private BigDecimal r12Amt;
    private BigDecimal rKv4Amt;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CashPlanLimit that = (CashPlanLimit) o;
        return Objects.equals(year, that.year) && Objects.equals(kfsr, that.kfsr)
                && Objects.equals(kcsr, that.kcsr) && Objects.equals(kvr, that.kvr)
                && Objects.equals(kosgu, that.kosgu) && Objects.equals(kvsr, that.kvsr)
                && Objects.equals(dopFk, that.dopFk) && Objects.equals(dopEk, that.dopEk)
                && Objects.equals(dopKr, that.dopKr) && Objects.equals(purpose, that.purpose);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                year, kfsr, kcsr, kvr, kosgu, kvsr, dopFk, dopEk, dopKr, purpose);
    }
}
