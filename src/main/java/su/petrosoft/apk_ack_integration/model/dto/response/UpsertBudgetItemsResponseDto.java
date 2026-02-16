package su.petrosoft.apk_ack_integration.model.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Map;
import java.util.Set;

@JsonInclude(Include.NON_NULL)
public record UpsertBudgetItemsResponseDto(
    Map<String, Set<Long>> created,
    Map<String, Set<Long>> updated
) {

}
