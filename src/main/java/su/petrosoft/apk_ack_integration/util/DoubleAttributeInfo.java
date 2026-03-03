package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DoubleAttribute;

import java.math.BigDecimal;

public record DoubleAttributeInfo(
        String name,
        long id
) implements AttrInfo<DoubleAttribute> {
    @Override
    public Class<DoubleAttribute> attrType() {
        return DoubleAttribute.class;
    }
}
