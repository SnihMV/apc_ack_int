package su.petrosoft.apk_ack_integration.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import su.petrosoft.apk_ack_integration.exception.PlicanteInstanceException;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.AttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.BlobFileAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.BooleanAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DateAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DoubleAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttributeDto;

import java.math.BigDecimal;

import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.CLASS_MISMATCH;

@RequiredArgsConstructor
@Getter
public enum AttributeType {
    BOOLEAN(Boolean.class, BooleanAttributeDto::new, Boolean.FALSE),
    BLOB(String.class, BlobFileAttributeDto::new, null),
    DATE(Long.class, DateAttributeDto::new, null),
    DOUBLE(BigDecimal.class, DoubleAttributeDto::new, BigDecimal.ZERO),
    LINKED(Long.class, LinkedAttributeDto::new, null),
    LONG(Long.class, LongAttributeDto::new, 0L),
    STRING(String.class, StringAttributeDto::new, "");

    private final Class<?> javaType;
    private final AttributeFactory factory;
    private final Object defaultValue;

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
        throw new PlicanteInstanceException(CLASS_MISMATCH.formatted(value, javaType, value.getClass()));
    }

    @FunctionalInterface
    public interface AttributeFactory {
        AttributeDto<?> create(long id, Object value);
    }
}
