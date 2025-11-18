package su.petrosoft.apk_ack_integration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancingSource {
    private Long id;
    private Long version;
    private Long year;
    private String kvsr;
    private String kfsr;
    private String kcsr;
    private String kvr;
    private String kosgu;
    private String dopFk;
    private String dopEk;
    private String dopKr;
    private String purpose;
    private Long subsidyProgramId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FinancingSource that = (FinancingSource) o;
        return Objects.equals(kvsr, that.kvsr) && Objects.equals(kfsr, that.kfsr) && Objects.equals(kcsr, that.kcsr) && Objects.equals(kvr, that.kvr) && Objects.equals(kosgu, that.kosgu) && Objects.equals(dopFk, that.dopFk) && Objects.equals(dopEk, that.dopEk) && Objects.equals(dopKr, that.dopKr) && Objects.equals(purpose, that.purpose);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kvsr, kfsr, kcsr, kvr, kosgu, dopFk, dopEk, dopKr, purpose);
    }
}
