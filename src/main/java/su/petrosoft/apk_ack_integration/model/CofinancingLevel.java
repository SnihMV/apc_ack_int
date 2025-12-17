package su.petrosoft.apk_ack_integration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import su.petrosoft.apk_ack_integration.model.enums.FinancingForm;
import su.petrosoft.apk_ack_integration.model.enums.OwnershipForm;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CofinancingLevel {
    private Long id;
    private Long version;
    private Long year;
    private LocalDate startDate;
    private BigDecimal obCoeff;
    private BigDecimal fbCoeff;
    private String financingForm;
    private String ownershipForm;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CofinancingLevel that = (CofinancingLevel) o;
        return Objects.equals(year, that.year)
                && Objects.equals(obCoeff, that.obCoeff)
                && Objects.equals(fbCoeff, that.fbCoeff)
                && Objects.equals(ownershipForm, that.ownershipForm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, obCoeff, fbCoeff, ownershipForm);
    }
}



