package su.petrosoft.apk_ack_integration.model.dto.plicante.value;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DoubleValue(
        BigDecimal data
) implements Value<BigDecimal> {
}
