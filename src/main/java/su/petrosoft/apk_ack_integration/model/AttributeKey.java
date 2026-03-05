package su.petrosoft.apk_ack_integration.model;

import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;

public interface AttributeKey<T> {
    Long id();
    String code();
    String name();
    AttributeType type();
    Attribute<T> createAttribute(T value);
    boolean isEmpty(T value);


    default Attribute<Object> createAttribute(Object value) {
        return switch (type) {
            case LONG -> new LongAttribute(id, code, (Long) value);
            case STRING -> new StringAttribute(id, code, (String) value);
            default -> throw new IllegalArgumentException("Unsupported type for ident: " + type);
        };
    }
}
