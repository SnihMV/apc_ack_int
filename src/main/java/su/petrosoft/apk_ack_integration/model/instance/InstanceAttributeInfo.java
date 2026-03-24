package su.petrosoft.apk_ack_integration.model.instance;

import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.AttributeDto;
import su.petrosoft.apk_ack_integration.model.enums.AttributeType;

import java.math.BigDecimal;
import java.util.Map;

public interface InstanceAttributeInfo {

    long getId();

    AttributeType getType();

    Map<Long, ? extends InstanceAttributeInfo> getIdMap();

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

    @Override
    boolean equals(Object o);

}
