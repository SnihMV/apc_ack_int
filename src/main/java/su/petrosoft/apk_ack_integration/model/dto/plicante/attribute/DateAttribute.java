package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.LongValue;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DateAttribute(
        Long id,
        String code,
        String type,
        List<LongValue> value
) implements Attribute<LongValue> {

    public DateAttribute(long id, Long value) {
        this(id, null, "DATE", List.of(new LongValue(value)));
    }

    @Override
    @JsonIgnore
    public LongValue getFirstValue() {
        return Attribute.super.getFirstValue();
    }

    @Override
    @JsonIgnore
    public Long getData(){
        LongValue firstValue = getFirstValue();
        return firstValue != null ? firstValue.data() : null;
    }
}
