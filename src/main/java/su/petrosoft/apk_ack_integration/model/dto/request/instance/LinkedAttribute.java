package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import java.util.List;

public record LinkedAttribute(
        Long id,
        String code,
        String type,
        List<Value> value
) implements Attribute {

    public LinkedAttribute(long id, Long data) {
        this(id, null, "LINKED", List.of(new Value(data, null)));
    }
    public LinkedAttribute(String code, Long data) {
        this(null, code, "LINKED", List.of(new Value(data, null)));
    }

    public Long getData(){
        Object data = Attribute.super.getData();
        if (data instanceof Long l) return l;
        if (data instanceof Integer i) return i.longValue();
        return null;
    }
}
