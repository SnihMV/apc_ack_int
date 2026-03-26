package su.petrosoft.apk_ack_integration.model.dto.plicante.filter;

import static su.petrosoft.apk_ack_integration.model.enums.ValueType.STATUS;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.StatusValue;
import su.petrosoft.apk_ack_integration.model.enums.SqlOperation;
import su.petrosoft.apk_ack_integration.model.enums.ValueType;

@JsonInclude(Include.NON_NULL)
public record StatusFilterAttribute(
    Long id,
    ValueType type,
    Condition<StatusValue> condition
) implements FilterAttribute<StatusValue> {

    public StatusFilterAttribute(long attrId, long statusId) {
        this(attrId, STATUS,
            new Condition<>(List.of(SqlOperation.EQUALS), List.of(new StatusValue(statusId))));
    }

    public StatusFilterAttribute(long attrId, List<SqlOperation> operations, Collection<Long> data) {
        this(attrId, STATUS, new Condition<>(operations, data.stream().map(StatusValue::new).toList()));
    }
}
