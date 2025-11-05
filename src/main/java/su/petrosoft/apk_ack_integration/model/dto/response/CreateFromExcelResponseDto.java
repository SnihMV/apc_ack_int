package su.petrosoft.apk_ack_integration.model.dto.response;

import java.util.List;

public record CreateFromExcelResponseDto(
        int parsedCount,
        int persistedCount,
        List<Long> persistedIds
) {
}
