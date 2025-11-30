package su.petrosoft.apk_ack_integration.model.dto.plicante.filter;

import su.petrosoft.apk_ack_integration.model.enums.SqlOperation;

import java.util.List;

public record Condition<T>(
        List<SqlOperation> sqlOps,
        List<T> values
) {
}
