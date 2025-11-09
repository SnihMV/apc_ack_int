package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import java.util.List;

public record BooleanAttribute(
        Long id,
        String code,
        String type,
        List<Value> value
) implements Attribute {

    public BooleanAttribute(long id, boolean data) {
        this(id, null, "BOOLEAN", List.of(new Value(data, null)));
    }

    public BooleanAttribute(String code, boolean data) {
        this(null, code, "BOOLEAN", List.of(new Value(data, null)));
    }

    public Boolean getData() {
        return (Boolean) Attribute.super.getData();
    }
}