package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record StringAttribute(
        Long id,
        String code,
        String type,
        List<Value> value,
        String name,
        String description,
        String attributeType
) implements Attribute {

    public StringAttribute(long id) {
        this(id, null);
    }

    public StringAttribute(long id, String data) {
        this(id, null, "STRING", List.of(new Value(data, null)), null, null, null);
    }

    @JsonIgnore
    public String getData() {
        return (String) Attribute.super.getData();
    }
}
