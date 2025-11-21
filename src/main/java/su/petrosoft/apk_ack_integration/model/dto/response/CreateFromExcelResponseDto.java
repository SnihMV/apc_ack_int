package su.petrosoft.apk_ack_integration.model.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CreateFromExcelResponseDto(
        int parsedCount,
        int persistedCount,
        List<Long> persistedIds
) {
}
