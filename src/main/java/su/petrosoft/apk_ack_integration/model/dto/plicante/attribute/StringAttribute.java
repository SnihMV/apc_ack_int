package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.StringValue;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record StringAttribute(
        Long id,
        String code,
        String type,
        List<StringValue> value
) implements Attribute<StringValue> {

    public StringAttribute(long id) {
        this(id, null);
    }

    public StringAttribute(long id, String data) {
        this(id, null, "STRING", List.of(new StringValue(data)));
    }

    @Override
    @JsonIgnore
    public StringValue getFirstValue() {
        return Attribute.super.getFirstValue();
    }

    @Override
    @JsonIgnore
    public String getData() {
        StringValue firstValue = getFirstValue();
        return firstValue != null ? firstValue.data() : null;
    }
}
