package su.petrosoft.apk_ack_integration.model.dto.plicante.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Arrays;
import java.util.List;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.StringValue;
import su.petrosoft.apk_ack_integration.model.enums.SqlOperation;
import su.petrosoft.apk_ack_integration.model.enums.ValueType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StringFilterAttribute(
        Long id,
        ValueType type,
        Condition<StringValue> condition
) implements FilterAttribute<StringValue> {
    public StringFilterAttribute(long id, String data) {
        this(id, ValueType.STRING, new Condition<>(List.of(SqlOperation.EQUALS), List.of(new StringValue(data))));
    }

    public StringFilterAttribute(long id, List<SqlOperation> operations, String... data) {
        this(id, ValueType.STRING, new Condition<>(operations, Arrays.stream(data).map(StringValue::new).toList()));
    }
}
