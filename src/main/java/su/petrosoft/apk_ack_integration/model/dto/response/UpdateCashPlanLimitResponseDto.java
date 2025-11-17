package su.petrosoft.apk_ack_integration.model.dto.response;

import java.util.List;

public record UpdateCashPlanLimitResponseDto(
        List<Long> updatedInstances
) {
}
