package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import java.util.List;

public record LongAttribute(
        Long id,
        String code,
        String type,
        List<LongValue> value
) implements Attribute {

    public LongAttribute(long id, Long data) {
        this(id, null, "LONG", List.of(new LongValue(data)));
    }

    public LongAttribute(String code, Long data) {
        this(null, code, "LONG", List.of(new LongValue(data)));
    }

    public record LongValue(
            Long data
    ) {
    }
}