package su.petrosoft.apk_ack_integration.model.dto.plicante.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.BlobFileValue;
import su.petrosoft.apk_ack_integration.model.enums.SqlOperation;
import su.petrosoft.apk_ack_integration.model.enums.ValueType;

import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.ValueType.BLOB;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BlobFileFilterAttribute(
        Long id,
        ValueType type,
        Condition<BlobFileValue> condition
) implements FilterAttribute<BlobFileValue> {
    public BlobFileFilterAttribute(long id, long data) {
        this(id, BLOB, new Condition<>(List.of(SqlOperation.EQUALS), List.of(new BlobFileValue(data))));
    }

    public BlobFileFilterAttribute(long id, String data) {
        this(id, BLOB, new Condition<>(List.of(SqlOperation.EQUALS), List.of(new BlobFileValue(data))));
    }
}
