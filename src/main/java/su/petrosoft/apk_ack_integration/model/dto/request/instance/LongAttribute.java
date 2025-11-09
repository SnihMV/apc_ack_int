package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import java.util.List;

public record LongAttribute(
        Long id,
        String code,
        String type,
        List<Value> value
) implements Attribute {

    public LongAttribute(long id, Long data) {
        this(id, null, "LONG", List.of(new Value(data, null)));
    }

    public LongAttribute(String code, Long data) {
        this(null, code, "LONG", List.of(new Value(data, null)));
    }

    public Long getData(){
        Object data = Attribute.super.getData();
        if (data instanceof Long l) return l;
        if (data instanceof Integer i) return i.longValue();
        return null;
    }

    public Long getLongData() {
        return (Long) value.get(0).data();
    }

}