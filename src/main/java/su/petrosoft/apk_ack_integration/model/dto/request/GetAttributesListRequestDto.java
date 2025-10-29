package su.petrosoft.apk_ack_integration.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GetAttributesListRequestDto(
        Long templateId,
        Long statusId
) {
}
