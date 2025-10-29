package su.petrosoft.apk_ack_integration.model.dto.response;

import java.util.List;

public record InstanceCreationResponseDto(
        Long id,
        Integer templateId,
        ObjectStatus status,
        List<SimpleAttribute> attributes
) {
    public record ObjectStatus(
            String code,
            String name
    ) {
    }

    public record SimpleAttribute(
            String code,
            String name,
            List<Value> value
    ) {
    }

    public record Value(
            Object data
    ) {
    }
}

