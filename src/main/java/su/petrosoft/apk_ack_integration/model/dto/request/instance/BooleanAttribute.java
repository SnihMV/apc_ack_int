package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BooleanAttribute(
        Long id,
        String code,
        String type,
        List<Value> value,
        String name,
        String description,
        String attributeType
) implements Attribute {
    public BooleanAttribute(long id, boolean data) {
        this(id, null, "BOOLEAN", List.of(new Value(data, null)), null, null, null);
    }

    @JsonIgnore
    public Boolean getData() {
        return (Boolean) Attribute.super.getData();
    }
}