package su.petrosoft.apk_ack_integration.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.Filter;
import su.petrosoft.apk_ack_integration.model.enums.ViewType;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GetAttributesListRequestDto(
        Long templateId,
        Long statusId,
        ViewType viewType,
        Boolean getBinaries,
        List<RequestedAttribute> attributes,
        Filter filter
) {
}
