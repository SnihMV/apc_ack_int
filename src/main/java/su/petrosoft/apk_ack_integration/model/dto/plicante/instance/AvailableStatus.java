package su.petrosoft.apk_ack_integration.model.dto.plicante.instance;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AvailableStatus(
        Long id,
        String code,
        String name
) {
}
