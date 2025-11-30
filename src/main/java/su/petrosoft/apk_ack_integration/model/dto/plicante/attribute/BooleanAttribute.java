package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.BooleanValue;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BooleanAttribute(
        Long id,
        String code,
        String type,
        List<BooleanValue> value
) implements Attribute<BooleanValue> {

    public BooleanAttribute(long id, boolean data) {
        this(id, null, "BOOLEAN", List.of(new BooleanValue(data)));
    }

    @Override
    @JsonIgnore
    public BooleanValue getFirstValue() {
        return Attribute.super.getFirstValue();
    }

    @Override
    @JsonIgnore
    public Boolean getData() {
        BooleanValue firstValue = getFirstValue();
        return firstValue != null ? firstValue.data() : null;
    }
}