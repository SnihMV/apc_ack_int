package su.petrosoft.apk_ack_integration.model.dto.plicante.filter;

import static su.petrosoft.apk_ack_integration.model.enums.ValueType.LINKED;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Collection;
import java.util.List;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.LinkedValue;
import su.petrosoft.apk_ack_integration.model.enums.SqlOperation;
import su.petrosoft.apk_ack_integration.model.enums.ValueType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LinkedFilterAttribute(
        Long id,
        ValueType type,
        Condition<LinkedValue> condition
) implements FilterAttribute<LinkedValue> {

    public LinkedFilterAttribute(long id, long data) {
        this(id, LINKED, new Condition<>(List.of(SqlOperation.EQUALS), List.of(new LinkedValue(data))));
    }

    public LinkedFilterAttribute(long id, List<SqlOperation> operations, Collection<Long> data) {
        this(id, LINKED, new Condition<>(operations, data.stream().map(LinkedValue::new).toList()));
    }
}
