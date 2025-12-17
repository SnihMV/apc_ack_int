package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.LinkedValue;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record LinkedAttribute(
        Long id,
        String code,
        String type,
        List<LinkedValue> value
) implements Attribute<LinkedValue> {

    public LinkedAttribute(long id) {
        this(id, null, "LINKED", null);
    }

    public LinkedAttribute(long id, Long data) {
        this(id, null, "LINKED", List.of(new LinkedValue(data)));
    }

    public LinkedAttribute(long id, List<Long> values) {
        this(id, null, "LINKED", values.stream().map(LinkedValue::new).toList());
    }

    @Override
    @JsonIgnore
    public LinkedValue getFirstValue() {
        return Attribute.super.getFirstValue();
    }

    @Override
    @JsonIgnore
    public Long getData() {
        LinkedValue firstValue = getFirstValue();
        return firstValue != null ? firstValue.data() : null;
    }
}
