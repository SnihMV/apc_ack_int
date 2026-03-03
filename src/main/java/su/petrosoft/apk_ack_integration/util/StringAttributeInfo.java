package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttribute;

public record StringAttributeInfo(
        String name,
        long id
) implements AttrInfo<StringAttribute> {
    @Override
    public Class<StringAttribute> attrType() {
        return StringAttribute.class;
    }
}
