package su.petrosoft.apk_ack_integration.model.dto.plicante.instance;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import lombok.Builder;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.Filter;
import su.petrosoft.apk_ack_integration.model.enums.ViewType;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record InstanceDto(
        Long id,
        Long templateId,
        Long version,
        Status status,
        ViewType viewType,
        String shortForm,
        List<Attribute<?>> attributes,
        Filter filter
) {
}
