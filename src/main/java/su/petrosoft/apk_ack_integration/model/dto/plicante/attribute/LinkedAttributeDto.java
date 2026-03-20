package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.LinkedValue;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.LongValue;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;

import java.util.Collection;
import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.DATE;
import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.LINKED;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record LinkedAttributeDto(
        Long id,
        String code,
        AttributeType type,
        List<LinkedValue> value
) implements AttributeDto<Long> {

    public LinkedAttributeDto(long id, Object data) {
        this(id, null, LINKED, toValueList(data));
    }

    private static List<LinkedValue> toValueList(Object data) {
        if (data == null) {
            return null;
        }
        if (data instanceof Collection<?> coll) {
            return coll.stream()
                    .map(val -> new LinkedValue(LINKED.cast(val)))
                    .toList();
        }
        return List.of(new LinkedValue(LINKED.cast(data)));
    }
}
