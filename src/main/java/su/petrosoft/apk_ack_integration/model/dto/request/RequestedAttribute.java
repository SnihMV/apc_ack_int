package su.petrosoft.apk_ack_integration.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RequestedAttribute(
        Long id,
        String code
) {
    public RequestedAttribute(Long id) {
        this(id, null);
    }

    public RequestedAttribute(String code) {
        this(null, code);
    }
}
