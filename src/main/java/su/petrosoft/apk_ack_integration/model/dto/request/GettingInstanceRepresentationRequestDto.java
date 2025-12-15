package su.petrosoft.apk_ack_integration.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.ViewType;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GettingInstanceRepresentationRequestDto(
        InstanceDto instance,
        Boolean getBinaries,
        Boolean getLinked,
        Boolean getManagedDictionary,
        ViewType viewType,
        String statusCode
) {
}
