package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.LinkedValue;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;

import java.util.Collection;
import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.LINKED;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record LinkedAttribute(
        Long id,
        String code,
        AttributeType type,
        List<LinkedValue> value
) implements Attribute<Long> {

    public LinkedAttribute(long id, Long data) {
        this(id, null, LINKED, List.of(new LinkedValue(data)));
    }

    public LinkedAttribute(long id, Collection<Long> values) {
        this(id, null, LINKED, values.stream().map(LinkedValue::new).toList());
    }
}
