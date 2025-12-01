package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.BlobFileValue;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BlobFileAttribute(
        Long id,
        String code,
        String type,
        List<BlobFileValue> value
) implements Attribute<BlobFileValue> {

    @Override
    @JsonIgnore
    public BlobFileValue getFirstValue() {
        return Attribute.super.getFirstValue();
    }

    @Override
    @JsonIgnore
    public String getData() {
        BlobFileValue firstValue = getFirstValue();
        return firstValue != null ? firstValue.data() : null;
    }
}
