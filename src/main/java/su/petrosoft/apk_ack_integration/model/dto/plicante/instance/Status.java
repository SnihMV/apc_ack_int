package su.petrosoft.apk_ack_integration.model.dto.plicante.instance;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Status(
        Long id,
        String code,
        String name,
        List<AvailableStatus> availableStatuses
) {
}
