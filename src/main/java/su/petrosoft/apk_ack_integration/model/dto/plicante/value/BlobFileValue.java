package su.petrosoft.apk_ack_integration.model.dto.plicante.value;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BlobFileValue(
        Long id,
        String name,
        String bytes
) implements Value<String> {
    public BlobFileValue(long id) {
        this(id, null, null);
    }

    public BlobFileValue(String name) {
        this(null, name, null);
    }
    @Override
    public String data() {
        return bytes;
    }
}
