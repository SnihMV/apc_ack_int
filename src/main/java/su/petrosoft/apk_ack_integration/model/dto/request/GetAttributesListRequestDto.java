package su.petrosoft.apk_ack_integration.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import su.petrosoft.apk_ack_integration.model.enums.ViewType;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record GetAttributesListRequestDto(
        Long templateId,
        Long statusId
) {
}
