package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonInclude;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.DoubleValue;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.StringValue;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.DOUBLE;
import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.STRING;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DoubleAttributeDto(
        Long id,
        String code,
        AttributeType type,
        List<DoubleValue> value
) implements AttributeDto<BigDecimal> {

    public DoubleAttributeDto(long id, Object data) {
        this(id, null, DOUBLE, toValueList(data));
    }

    private static List<DoubleValue> toValueList(Object data) {
        if (data == null) {
            return null;
        }
        if (data instanceof Collection<?> coll) {
            return coll.stream()
                    .map(val -> new DoubleValue(DOUBLE.cast(val)))
                    .toList();
        }
        return List.of(new DoubleValue(DOUBLE.cast(data)));
    }
}