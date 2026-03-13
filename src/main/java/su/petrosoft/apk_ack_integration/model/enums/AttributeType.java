package su.petrosoft.apk_ack_integration.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.BlobFileAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.BooleanAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DateAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DoubleAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttribute;

import java.math.BigDecimal;

import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.CLASS_MISMATCH;

@RequiredArgsConstructor
@Getter
public enum AttributeType {
    BOOLEAN(Boolean.class, BooleanAttribute::new),
    BLOB(String.class, BlobFileAttribute::new),
    DATE(Long.class, DateAttribute::new),
    DOUBLE(BigDecimal.class, DoubleAttribute::new),
    LINKED(Long.class, LinkedAttribute::new),
    LONG(Long.class, LongAttribute::new),
    STRING(String.class, StringAttribute::new);

    private final Class<?> javaType;
    private final AttributeFactory factory;

    @SuppressWarnings("unchecked")
    public <T> T cast(Object value) {
        if (value == null) {
            return null;
        }
        if (javaType.isInstance(value)) {
            return (T) value;
        }
        return (T) convertToJavaType(value);
    }

    private Object convertToJavaType(Object value) {
        switch (this) {
            case DOUBLE -> {
                if (value instanceof Number num) {
                    return BigDecimal.valueOf(num.doubleValue());
                }
            }
            case LONG -> {
                if (value instanceof Number num) {
                    return num.longValue();
                }
            }
        }
        throw new ClassCastException(CLASS_MISMATCH.formatted(value, javaType, value.getClass()));
    }

    @FunctionalInterface
    public interface AttributeFactory {
        Attribute<?> create(long id, Object value);
    }
}
