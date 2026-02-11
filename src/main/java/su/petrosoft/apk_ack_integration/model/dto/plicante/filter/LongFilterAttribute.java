package su.petrosoft.apk_ack_integration.model.dto.plicante.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.LongValue;
import su.petrosoft.apk_ack_integration.model.enums.SqlOperation;
import su.petrosoft.apk_ack_integration.model.enums.ValueType;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.ValueType.LONG;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LongFilterAttribute(
        Long id,
        ValueType type,
        Condition<LongValue> condition
) implements FilterAttribute<LongValue> {
    public LongFilterAttribute(long id, long data) {
        this(id, LONG, new Condition<>(List.of(SqlOperation.EQUALS), List.of(new LongValue(data))));
    }
    public LongFilterAttribute(long id, List<SqlOperation> operations, Collection<Long> data) {
        this(id, LONG, new Condition<>(operations, data.stream().map(LongValue::new).toList()));
    }
}
