package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import java.util.List;

public record StringAttribute(
        Integer id,
        String code,
        String type,
        List<StringValue> value
) implements Attribute {

    public StringAttribute(int id, String data) {
        this(id, null, "STRING", List.of(new StringValue(data)));
    }

    public StringAttribute(String code, String data) {
        this(null, code, "STRING", List.of(new StringValue(data)));
    }

    public record StringValue(
            String data
    ) {
    }
}
