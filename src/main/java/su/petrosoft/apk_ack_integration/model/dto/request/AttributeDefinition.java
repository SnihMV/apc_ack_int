package su.petrosoft.apk_ack_integration.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AttributeDefinition(
        Long id,
        String code
) {
    public AttributeDefinition(Long id) {
        this(id, null);
    }

    public AttributeDefinition(String code) {
        this(null, code);
    }
}
