package su.petrosoft.apk_ack_integration.model.dto.response;

import java.util.List;
import java.util.Map;

public record CreateBudgetItemsResponseDto(
        Map<String, List<Long>> createdInstances
) {
}
