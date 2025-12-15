package su.petrosoft.apk_ack_integration.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AttributeRepresentationDto(
        Long id,
        String code,
        String name,
        String description,
        List<ValueRepresentation> value
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ValueRepresentation(
            String bytes,
            String data,
            String shortForm
    ) {
    }
}
