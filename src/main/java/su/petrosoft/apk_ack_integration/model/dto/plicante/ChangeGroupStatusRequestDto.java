package su.petrosoft.apk_ack_integration.model.dto.plicante;

import java.util.List;

public record ChangeGroupStatusRequestDto(
        Long newStatusId,
        List<Long> instanceIds
) {
}
