package su.petrosoft.apk_ack_integration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

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
}
