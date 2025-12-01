package su.petrosoft.apk_ack_integration.model.dto.plicante.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.LongValue;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.StringValue;
import su.petrosoft.apk_ack_integration.model.enums.SqlOperation;
import su.petrosoft.apk_ack_integration.model.enums.ValueType;

import java.util.Arrays;
import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.ValueType.DATE;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DateFilterAttribute(
        Long id,
        ValueType type,
        Condition<LongValue> condition
) implements FilterAttribute<LongValue> {
    public DateFilterAttribute(long id, long data) {
        this(id, DATE, new Condition<>(List.of(SqlOperation.EQUALS), List.of(new LongValue(data))));
    }

    public DateFilterAttribute(long id, List<SqlOperation> operations, long... data) {
        this(id, DATE, new Condition<>(operations, Arrays.stream(data).mapToObj(LongValue::new).toList()));
    }
}
