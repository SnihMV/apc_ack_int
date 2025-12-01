package su.petrosoft.apk_ack_integration.model.dto.plicante;

import java.util.Set;

public record ChangeGroupStatusRequestDto(
        Long newStatusId,
        Set<Long> instanceIds
) {
}
