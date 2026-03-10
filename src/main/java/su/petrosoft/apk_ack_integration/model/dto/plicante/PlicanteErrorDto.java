package su.petrosoft.apk_ack_integration.model.dto.plicante;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlicanteErrorDto(
        String errorMsg,
        String type,
        String code,
        String responseStatus
) {
}
