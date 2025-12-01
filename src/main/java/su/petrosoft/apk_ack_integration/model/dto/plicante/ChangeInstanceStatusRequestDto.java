package su.petrosoft.apk_ack_integration.model.dto.plicante;

public record ChangeInstanceStatusRequestDto(
        Long objectId,
        Long oldStatusId,
        Long newStatusId
) {
}
