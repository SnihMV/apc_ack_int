package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.StringValue;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;

import java.util.Collection;
import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.AttributeType.STRING;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record StringAttributeDto(
        Long id,
        String code,
        AttributeType type,
        List<StringValue> value
) implements AttributeDto<String> {

    public StringAttributeDto(long id, Object data) {
        this(id, null, STRING, toValueList(data));
    }

    private static List<StringValue> toValueList(Object data) {
        if (data == null) {
            return null;
        }
        if (data instanceof Collection<?> coll) {
            return coll.stream()
                    .map(val -> new StringValue(STRING.cast(val)))
                    .toList();
        }
        return List.of(new StringValue(STRING.cast(data)));
    }
}
