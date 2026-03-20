package su.petrosoft.apk_ack_integration.model.instance;

import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.AttributeDto;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;

import java.math.BigDecimal;

public interface InstanceAttributeEnum {
    Long getId();
    AttributeType getType();
    boolean isIdentifying();

    default AttributeDto<?> createAttributeDto(Object value) {
        if (value == null) {
            return null;
        }
        Object castedValue = getType().cast(value);
        return getType().getFactory().create(getId(), castedValue);
    }

    default boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        }
         return switch (getType()) {
             case LONG -> (Long) value == 0;
             case DOUBLE -> ((BigDecimal) value).compareTo(BigDecimal.ZERO) == 0;
             case STRING, BLOB -> ((String) value).isEmpty() || ((String) value).isBlank();
             default -> false;
         };
    }



}
