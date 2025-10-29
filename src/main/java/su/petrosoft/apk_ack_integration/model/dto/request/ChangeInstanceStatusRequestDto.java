package su.petrosoft.apk_ack_integration.model.dto.request;

public record ChangeInstanceStatusRequestDto(
        Long objectId,
        Long oldStatusId,
        Long newStatusId
) {
}
