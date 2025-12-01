package su.petrosoft.apk_ack_integration.model.dto.plicante.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.LinkedValue;
import su.petrosoft.apk_ack_integration.model.enums.SqlOperation;
import su.petrosoft.apk_ack_integration.model.enums.ValueType;

import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.ValueType.LINKED;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LinkedFilterAttribute(
        Long id,
        ValueType type,
        Condition<LinkedValue> condition
) implements FilterAttribute<LinkedValue> {
    public LinkedFilterAttribute(long id, String data) {
        this(id, LINKED, new Condition<>(List.of(SqlOperation.EQUALS), List.of(new LinkedValue(data))));
    }

    public LinkedFilterAttribute(long id, long data) {
        this(id, LINKED, new Condition<>(List.of(SqlOperation.EQUALS), List.of(new LinkedValue(data))));
    }
}
