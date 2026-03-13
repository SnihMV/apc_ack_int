package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonInclude;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.BlobFileValue;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.StringValue;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;

import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.BLOB;
import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.STRING;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BlobFileAttribute(
        Long id,
        String code,
        AttributeType type,
        List<BlobFileValue> value
) implements Attribute<String> {

    public BlobFileAttribute(long id, Object data) {
        this(id, null, BLOB, List.of(new BlobFileValue(BLOB.cast(data))));
    }
}
