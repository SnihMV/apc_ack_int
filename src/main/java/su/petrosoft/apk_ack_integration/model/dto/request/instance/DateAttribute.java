package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import java.time.LocalDateTime;
import java.util.List;

public record DateAttribute(
        Long id,
        String code,
        String type,
        List<Value> value
) implements Attribute {

    public DateAttribute(long id, LocalDateTime value) {
        this(id, null, "DATE", List.of(new Value(value, null)));
    }

    public DateAttribute(String code, LocalDateTime data) {
        this(null, code, "DATE", List.of(new Value(data, null)));
    }

    public LocalDateTime getData(){
        return (LocalDateTime) Attribute.super.getData();
    }
}
