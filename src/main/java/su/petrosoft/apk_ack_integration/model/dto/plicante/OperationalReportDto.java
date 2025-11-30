package su.petrosoft.apk_ack_integration.model.dto.plicante;

import java.math.BigDecimal;

public record OperationalReportDto(
        Long id,
        String field,
        BigDecimal value
) {
}
