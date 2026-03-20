package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonInclude;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.BlobFileValue;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.LinkedValue;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;

import java.util.Collection;
import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.BLOB;
import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.LINKED;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BlobFileAttributeDto(
        Long id,
        String code,
        AttributeType type,
        List<BlobFileValue> value
) implements AttributeDto<String> {

    public BlobFileAttributeDto(long id, Object data) {
        this(id, null, BLOB, List.of(new BlobFileValue(BLOB.cast(data))));
    }

    private static List<BlobFileValue> toValueList(Object data) {
        if (data == null) {
            return null;
        }
        if (data instanceof Collection<?> coll) {
            return coll.stream()
                    .map(val -> new BlobFileValue(BLOB.cast(val)))
                    .toList();
        }
        return List.of(new BlobFileValue(BLOB.cast(data)));
    }
}
