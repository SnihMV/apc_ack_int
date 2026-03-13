package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.StringValue;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;

import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.STRING;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record StringAttribute(
        Long id,
        String code,
        AttributeType type,
        List<StringValue> value
) implements Attribute<String> {

    public StringAttribute(long id, Object data) {
        this(id, null, STRING, List.of(new StringValue(STRING.cast(data))));
    }
}
