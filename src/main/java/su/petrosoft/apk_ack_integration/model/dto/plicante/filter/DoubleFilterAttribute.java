package su.petrosoft.apk_ack_integration.model.dto.plicante.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.DoubleValue;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.LinkedValue;
import su.petrosoft.apk_ack_integration.model.enums.SqlOperation;
import su.petrosoft.apk_ack_integration.model.enums.ValueType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.ValueType.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DoubleFilterAttribute(
        Long id,
        ValueType type,
        Condition<DoubleValue> condition
) implements FilterAttribute<DoubleValue> {
    public DoubleFilterAttribute(long id, BigDecimal data) {
        this(id, DOUBLE, new Condition<>(List.of(SqlOperation.EQUALS), List.of(new DoubleValue(data))));
    }

    public DoubleFilterAttribute(long id, List<SqlOperation> operations, BigDecimal... data) {
        this(id, DOUBLE, new Condition<>(operations, Arrays.stream(data).map(DoubleValue::new).toList()));
    }
}
