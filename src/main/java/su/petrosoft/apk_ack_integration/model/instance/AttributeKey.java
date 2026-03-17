package su.petrosoft.apk_ack_integration.model.instance;

import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;

public interface AttributeKey {
    Long id();
    String code();
    String title();
    AttributeType type();

    default Attribute<?> createAttribute(Object value) {
        if (value == null) {
            return null;
        }
        Object castedValue = type().cast(value);
        return type().getFactory().create(id(), value);
    }
}
