package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonInclude;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.BooleanValue;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.LongValue;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;

import java.util.Collection;
import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.BOOLEAN;
import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.LONG;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BooleanAttributeDto(
        Long id,
        String code,
        AttributeType type,
        List<BooleanValue> value
) implements AttributeDto<Boolean> {

    public BooleanAttributeDto(long id, Object data) {
        this(id, null, BOOLEAN, List.of(new BooleanValue(BOOLEAN.cast(data))));
    }

    private static List<BooleanValue> toValueList(Object data) {
        if (data == null) {
            return null;
        }
        if (data instanceof Collection<?> coll) {
            return coll.stream()
                    .map(val -> new BooleanValue(BOOLEAN.cast(val)))
                    .toList();
        }
        return List.of(new BooleanValue(BOOLEAN.cast(data)));
    }
}