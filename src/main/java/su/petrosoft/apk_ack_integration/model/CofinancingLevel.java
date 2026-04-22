package su.petrosoft.apk_ack_integration.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.TEMPLATE_ID;

@Getter
@Setter
@SuperBuilder
public class CofinancingLevel extends PlicanteInstance {
    private Long year;
    private LocalDate startDate;
    private BigDecimal obCoeff;
    private BigDecimal fbCoeff;
    private Long financingForm;
    private Long ownershipForm;

    @Override
    public long getTemplateId() {
        return TEMPLATE_ID;
    }

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



