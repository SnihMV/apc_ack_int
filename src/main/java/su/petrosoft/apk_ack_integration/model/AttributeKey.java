package su.petrosoft.apk_ack_integration.model;

import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;

public interface AttributeKey<T> {
    Long id();
    String code();
    String name();
    AttributeType type();

    default Attribute<?> createAttribute(Object value) {
        if (value == null) {
            return null;
        }
        Object castedValue = type().cast(value);
        return type().getFactory().create(id(), value);
    }
}
