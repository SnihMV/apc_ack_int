package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.LongValue;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;

import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.LONG;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record LongAttribute(
        Long id,
        String code,
        AttributeType type,
        List<LongValue> value
) implements Attribute<Long> {

    public LongAttribute(long id, Object data) {
        this(id, null, LONG, List.of(new LongValue(LONG.cast(data))));
    }
}