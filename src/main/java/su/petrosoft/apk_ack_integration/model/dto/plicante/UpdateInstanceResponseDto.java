package su.petrosoft.apk_ack_integration.model.dto.plicante;

import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.Status;

public record UpdateInstanceResponseDto(
    Long id,
    Long templateId,
    Long version,
    Status status
) {

}
