package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.List;

public record DateAttribute(
        Long id,
        String code,
        String type,
        List<Value> value,
        String name,
        String description,
        String attributeType
) implements Attribute {

    public DateAttribute(long id, LocalDateTime value) {
        this(id, null, "DATE", List.of(new Value(value, null)), null, null, null);
    }

    @JsonIgnore
    public LocalDateTime getData(){
        return (LocalDateTime) Attribute.super.getData();
    }
}
