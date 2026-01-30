package su.petrosoft.apk_ack_integration.model.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CreatingInstancesFromFileResponseDto(
        int incomingCount,
        int disjointCount,
        List<Long> persistedIds
) {
}
