package su.petrosoft.apk_ack_integration.model.dto.response;

import java.util.List;

public record GetAttributesListResponseDto(
        Long id,
        Long version,
        Long versionDate,
        Long templateId,
        String name,
        String shortForm,
        Status status,
        List<Attribute> attributes
) {
    public record Status(
            Long id,
            String code,
            String name
    ) {
    }

    public record Attribute(
            String type,
            Long id,
            String code,
            List<Value> value
    ) {
    }
    public record Value(
            String data,
            String shortForm
    ) {
    }
}

