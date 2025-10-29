package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import java.util.List;

public record DateAttribute(
        Integer id,
        String code,
        String type,
        List<DateValue> value
) implements Attribute {

    public DateAttribute(int id, String value) {
        this(id, null, "DATE", List.of(new DateValue(value)));
    }

    public DateAttribute(String code, String data) {
        this(null, code, "DATE", List.of(new DateValue(data)));
    }

    public record DateValue(
            String data
    ) {
    }
}
