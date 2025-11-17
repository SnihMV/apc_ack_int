package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.List;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = StringAttribute.class, name = "STRING"),
    @JsonSubTypes.Type(value = DoubleAttribute.class, name = "DOUBLE"),
    @JsonSubTypes.Type(value = LongAttribute.class, name = "LONG"),
    @JsonSubTypes.Type(value = BooleanAttribute.class, name = "BOOLEAN"),
    @JsonSubTypes.Type(value = DateAttribute.class, name = "DATE"),
    @JsonSubTypes.Type(value = LinkedAttribute.class, name = "LINKED")
})
public sealed interface Attribute
    permits StringAttribute, DoubleAttribute, LongAttribute,
    BooleanAttribute, DateAttribute, LinkedAttribute {

    Long id();

    String code();

    String type();

    List<Value> value();

    default boolean hasValue() {
        return value() != null && !value().isEmpty() && value().get(0) != null;
    }

    default Object getData() {
        return hasValue() ? value().get(0).data() : null;
    }

    default String getShortForm() {
        return hasValue() ? value().get(0).shortForm() : null;
    }
}
