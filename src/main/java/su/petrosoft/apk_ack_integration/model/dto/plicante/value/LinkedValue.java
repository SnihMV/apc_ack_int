package su.petrosoft.apk_ack_integration.model.dto.plicante.value;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LinkedValue(
        Long data,
        String shortForm
) implements Value<Long> {

    public LinkedValue(String shortForm) {
        this(null, shortForm);
    }
    public LinkedValue(Long id) {
        this(id, null);
    }
}
