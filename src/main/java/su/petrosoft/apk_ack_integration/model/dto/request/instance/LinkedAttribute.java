package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record LinkedAttribute(
        Long id,
        String code,
        String type,
        List<Value> value,
        String name,
        String description,
        String attributeType
) implements Attribute {

    public LinkedAttribute(long id) {
        this(id, null);
    }

    public LinkedAttribute(long id, Long data) {
        this(id, null, "LINKED", List.of(new Value(data, null)), null, null, null);
    }

    @JsonIgnore
    public Long getData(){
        Object data = Attribute.super.getData();
        if (data instanceof Long l) return l;
        if (data instanceof Integer i) return i.longValue();
        return null;
    }
}
