package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Instance(
        Long id,
        Long templateId,
        Long version,
        Status status,
        List<Attribute> attributes
) {
}
