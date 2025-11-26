package su.petrosoft.apk_ack_integration.model.dto.response;

import java.util.Map;
import java.util.Set;

public record CreateBudgetItemsResponseDto(
        Map<String, Set<Long>> createdInstances
) {
}
