package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.DoubleValue;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DoubleAttribute(
        Long id,
        String code,
        String type,
        List<DoubleValue> value
) implements Attribute<DoubleValue> {

    public DoubleAttribute(long id, BigDecimal data) {
        this(id, null, "DOUBLE", List.of(new DoubleValue(data)));
    }

    @Override
    @JsonIgnore
    public BigDecimal getData() {
        DoubleValue firstValue = getFirstValue();
        return firstValue != null ? firstValue.data() : BigDecimal.ZERO;
    }
}