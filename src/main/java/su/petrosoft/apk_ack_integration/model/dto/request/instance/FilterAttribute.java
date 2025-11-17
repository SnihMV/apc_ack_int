package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;
import su.petrosoft.apk_ack_integration.model.enums.SqlOperation;
import su.petrosoft.apk_ack_integration.model.enums.ValueType;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FilterAttribute(
        ValueType type,
        AttributeType attributeType,
        Long id,
        Condition condition
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    record Condition(
            List<SqlOperation> sqlOps,
            List<Value> values
    ) {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        record Value(
                Object data,
                String shortForm
        ){}
    }

    public FilterAttribute(ValueType type, Long id, Object data) {
        this(type, null, id, new Condition(
                List.of(SqlOperation.EQUALS),
                List.of(new Condition.Value(data, null))));
    }
}
