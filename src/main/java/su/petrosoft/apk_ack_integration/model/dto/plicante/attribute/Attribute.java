package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.LinkedValue;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.Value;

import java.util.List;

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

    String type();

    List<T> value();

    default boolean hasValue() {
        return value() != null && !value().isEmpty() && value().get(0) != null;
    }

    default T getFirstValue() {
        return hasValue() ? value().get(0) : null;
    }

    default Object getData() {
        T firstValue = getFirstValue();
        return firstValue instanceof Value<?> value ? value.data() : firstValue;
    }

    default String getShortForm() {
        T firstValue = getFirstValue();
        return firstValue instanceof LinkedValue value ? value.shortForm() : null;
    }
}
