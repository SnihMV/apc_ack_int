package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonInclude;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.BlobFileValue;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BlobFileAttribute(
        Long id,
        String code,
        AttributeType type,
        List<BlobFileValue> value
) implements Attribute<String> {
}
