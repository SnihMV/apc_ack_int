package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import java.util.List;

public record LinkedAttribute(
        Long id,
        String code,
        String type,
        List<LinkedValue> value
) implements Attribute {

    public LinkedAttribute(long id, Long data) {
        this(id, null, "LINKED", List.of(new LinkedValue(data, null)));
    }
    public LinkedAttribute(String code, Long data) {
        this(null, code, "LINKED", List.of(new LinkedValue(data, null)));
    }

    public record LinkedValue(
            Long data,
            String shortForm
    ) {
    }
}
