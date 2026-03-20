package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.DoubleValue;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.LongValue;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;

import java.util.Collection;
import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.DOUBLE;
import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.LONG;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record LongAttributeDto(
        Long id,
        String code,
        AttributeType type,
        List<LongValue> value
) implements AttributeDto<Long> {

    public LongAttributeDto(long id, Object data) {
        this(id, null, LONG, List.of(new LongValue(LONG.cast(data))));
    }

    private static List<LongValue> toValueList(Object data) {
        if (data == null) {
            return null;
        }
        if (data instanceof Collection<?> coll) {
            return coll.stream()
                    .map(val -> new LongValue(LONG.cast(val)))
                    .toList();
        }
        return List.of(new LongValue(LONG.cast(data)));
    }
}