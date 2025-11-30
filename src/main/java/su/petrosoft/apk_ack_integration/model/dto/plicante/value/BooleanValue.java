package su.petrosoft.apk_ack_integration.model.dto.plicante.value;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BooleanValue(
        Boolean data
) implements Value<Boolean> {
}
