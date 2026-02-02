package su.petrosoft.apk_ack_integration.model.dto.response;

import java.util.Map;
import java.util.Set;

public record CreateCofinancingLevelsFromExcelResponseDto(
        Map<Long, Set<Long>> updatedByFile,
        Map<Long, Set<Long>> updatedByDefault
) {
}
