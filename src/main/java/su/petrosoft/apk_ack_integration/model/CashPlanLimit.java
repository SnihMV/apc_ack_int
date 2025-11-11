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
    private Long kfsrCode;
    private Long kcsrCode;
    private Long kvrCode;
    private Long kesrCode;
    private Long kadmrCode;
    private Long kdfCode;
    private Long kdeCode;
    private Long kdrCode;
    private Long purposeCode;
    private BigDecimal limitTotalAmt;
    private BigDecimal limitFederalAmt;
    private BigDecimal limitRegionalAmt;
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
        return Objects.equals(year, that.year) && Objects.equals(kfsrCode, that.kfsrCode)
                && Objects.equals(kcsrCode, that.kcsrCode) && Objects.equals(kvrCode, that.kvrCode)
                && Objects.equals(kesrCode, that.kesrCode) && Objects.equals(kadmrCode, that.kadmrCode)
                && Objects.equals(kdfCode, that.kdfCode) && Objects.equals(kdeCode, that.kdeCode)
                && Objects.equals(kdrCode, that.kdrCode) && Objects.equals(purposeCode, that.purposeCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                year, kfsrCode, kcsrCode, kvrCode, kesrCode, kadmrCode, kdfCode, kdeCode, kdrCode, purposeCode);
    }
}
