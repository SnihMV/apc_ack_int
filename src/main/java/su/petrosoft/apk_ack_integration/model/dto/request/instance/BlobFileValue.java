package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BlobFileValue(
        Long id,
        String name,
        String bytes
) implements Value<String> {
    @Override
    public String data() {
        return bytes;
    }
}
