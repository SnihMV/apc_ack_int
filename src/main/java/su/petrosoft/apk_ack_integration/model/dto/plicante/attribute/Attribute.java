package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.Value;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        visible = true,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StringAttribute.class, name = "STRING"),
        @JsonSubTypes.Type(value = DoubleAttribute.class, name = "DOUBLE"),
        @JsonSubTypes.Type(value = LongAttribute.class, name = "LONG"),
        @JsonSubTypes.Type(value = BooleanAttribute.class, name = "BOOLEAN"),
        @JsonSubTypes.Type(value = DateAttribute.class, name = "DATE"),
        @JsonSubTypes.Type(value = LinkedAttribute.class, name = "LINKED"),
        @JsonSubTypes.Type(value = BlobFileAttribute.class, name = "BLOB")
})
public sealed interface Attribute<T>
        permits StringAttribute, DoubleAttribute, LongAttribute,
        BooleanAttribute, DateAttribute, LinkedAttribute, BlobFileAttribute {

    Long id();

    String code();

    AttributeType type();

    List<? extends Value<T>> value();

    @JsonIgnore
    default boolean hasValue() {
        return value() != null && !value().isEmpty() && value().get(0) != null;
    }

    @JsonIgnore
    default Value<T> getFirstValue() {
        return hasValue() ? value().get(0) : null;
    }

    @JsonIgnore
    default T getData() {
        Value<T> firstValue = getFirstValue();
        return firstValue != null ? firstValue.data() : null;
    }

    @JsonIgnore
    default List<T> getAllData() {
        if (value() == null) return new ArrayList<>();
        return value().stream()
                .map(v -> (Value<T>) v)
                .map(Value::data)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
