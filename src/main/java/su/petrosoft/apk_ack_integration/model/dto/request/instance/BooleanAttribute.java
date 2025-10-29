package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import java.util.List;

public record BooleanAttribute(
        Integer id,
        String code,
        String type,
        List<BooleanValue> value
) implements Attribute {

    public BooleanAttribute(int id, boolean data) {
        this(id, null, "BOOLEAN", List.of(new BooleanValue(data)));
    }

    public BooleanAttribute(String code, boolean data) {
        this(null, code, "BOOLEAN", List.of(new BooleanValue(data)));
    }

    public record BooleanValue(
            Boolean data
    ) {
    }
}