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
    private Long subsidyProgramId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FinancingSource that = (FinancingSource) o;
        return Objects.equals(year, that.year) && Objects.equals(kvsr, that.kvsr)
                && Objects.equals(kfsr, that.kfsr) && Objects.equals(kcsr, that.kcsr)
                && Objects.equals(kvr, that.kvr) && Objects.equals(kosgu, that.kosgu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, kvsr, kfsr, kcsr, kvr, kosgu);
    }
}
