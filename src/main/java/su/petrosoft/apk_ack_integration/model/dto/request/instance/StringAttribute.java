package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import java.util.List;

public record StringAttribute(
        Long id,
        String code,
        String type,
        List<Value> value
) implements Attribute {

    public StringAttribute(long id, String data) {
        this(id, null, "STRING", List.of(new Value(data, null)));
    }

    public StringAttribute(String code, String data) {
        this(null, code, "STRING", List.of(new Value(data, null)));
    }

    public String getData() {
        return (String) Attribute.super.getData();
    }

    public String getStringData() {
        if (value == null || value.get(0) == null) {
            return null;
        }
        return value.get(0).data().toString();
    }
}
