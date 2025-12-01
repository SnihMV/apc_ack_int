package su.petrosoft.apk_ack_integration.model.dto.plicante.filter;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Filter(
        List<FilterAttribute<?>> attributes
) {
}
