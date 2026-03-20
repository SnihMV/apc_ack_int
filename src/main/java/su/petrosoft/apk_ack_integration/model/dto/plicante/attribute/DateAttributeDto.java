package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonInclude;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.BooleanValue;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.LongValue;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;

import java.util.Collection;
import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.BOOLEAN;
import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.DATE;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DateAttributeDto(
        Long id,
        String code,
        AttributeType type,
        List<LongValue> value
) implements AttributeDto<Long> {

    public DateAttributeDto(long id, Object value) {
        this(id, null, DATE, toValueList(value));
    }

    private static List<LongValue> toValueList(Object data) {
        if (data == null) {
            return null;
        }
        if (data instanceof Collection<?> coll) {
            return coll.stream()
                    .map(val -> new LongValue(DATE.cast(val)))
                    .toList();
        }
        return List.of(new LongValue(DATE.cast(data)));
    }
}
