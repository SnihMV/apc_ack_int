package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonInclude;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.DoubleValue;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;

import java.math.BigDecimal;
import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.DOUBLE;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DoubleAttribute(
        Long id,
        String code,
        AttributeType type,
        List<DoubleValue> value
) implements Attribute<BigDecimal> {

    public DoubleAttribute(long id, Object data) {
        this(id, null, DOUBLE, List.of(new DoubleValue(DOUBLE.cast(data))));
    }
}