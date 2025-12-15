package su.petrosoft.apk_ack_integration.model.dto.plicante.attribute;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import su.petrosoft.apk_ack_integration.model.Pair;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.LinkedValue;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.Value;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    @JsonIgnore
    default boolean hasValue() {
        return value() != null && !value().isEmpty() && value().get(0) != null;
    }

    @JsonIgnore
    default T getFirstValue() {
        return hasValue() ? value().get(0) : null;
    }

    @JsonIgnore
    default Object getData() {
        T firstValue = getFirstValue();
        return firstValue instanceof Value<?> value ? value.data() : null;
    }

    @JsonIgnore
    default String getShortForm() {
        T firstValue = getFirstValue();
        return firstValue instanceof LinkedValue value ? value.shortForm() : null;
    }

    @JsonIgnore
    default Pair getPair() {
        T firstValue = getFirstValue();
        if (firstValue instanceof LinkedValue linkedValue) {
            return new Pair(linkedValue.data(), linkedValue.shortForm());
        }
        return null;
    }

    @JsonIgnore
    default List<?> getAllData() {
        if (value() == null) return Collections.emptyList();
        return value().stream()
                .map(Value.class::cast)
                .map(Value::data)
                .filter(Objects::nonNull)
                .toList();
    }

    @JsonIgnore
    default List<String> getAllShortForms() {
        if (value() == null) return Collections.emptyList();
        return value().stream()
                .filter(LinkedValue.class::isInstance)
                .map(LinkedValue.class::cast)
                .map(LinkedValue::shortForm)
                .filter(Objects::nonNull)
                .toList();
    }

    @JsonIgnore
    default List<Pair> getAllPairs() {
        if (value() == null) return Collections.emptyList();
        return value().stream()
                .filter(LinkedValue.class::isInstance)
                .map(LinkedValue.class::cast)
                .map(lv -> new Pair(lv.data(), lv.shortForm()))
                .filter(pair -> pair.data() != null || pair.shortForm() != null)
                .toList();
    }
}
