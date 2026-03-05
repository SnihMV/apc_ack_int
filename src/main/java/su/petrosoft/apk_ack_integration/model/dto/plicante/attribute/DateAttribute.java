package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonInclude;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.LongValue;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;

import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.DATE;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DateAttribute(
        Long id,
        String code,
        AttributeType type,
        List<LongValue> value
) implements Attribute<Long> {

    public DateAttribute(long id, Long value) {
        this(id, null, DATE, List.of(new LongValue(value)));
    }
}
