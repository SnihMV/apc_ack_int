package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonInclude;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.BooleanValue;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;

import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.BOOLEAN;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BooleanAttribute(
        Long id,
        String code,
        AttributeType type,
        List<BooleanValue> value
) implements Attribute<Boolean> {

    public BooleanAttribute(long id, boolean data) {
        this(id, null, BOOLEAN, List.of(new BooleanValue(data)));
    }
}