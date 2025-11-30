package su.petrosoft.apk_ack_integration.model.dto.plicante.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.BooleanValue;
import su.petrosoft.apk_ack_integration.model.enums.SqlOperation;
import su.petrosoft.apk_ack_integration.model.enums.ValueType;

import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.ValueType.BOOLEAN;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BooleanFilterAttribute(
        Long id,
        ValueType type,
        Condition<BooleanValue> condition
) implements FilterAttribute<BooleanValue> {
    public BooleanFilterAttribute(long id, boolean data) {
        this(id, BOOLEAN, new Condition<>(List.of(SqlOperation.EQUALS), List.of(new BooleanValue(data))));
    }
}
