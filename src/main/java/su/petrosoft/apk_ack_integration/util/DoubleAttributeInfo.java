package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DoubleAttributeDto;

public record DoubleAttributeInfo(
        String name,
        long id
) implements AttrInfo<DoubleAttributeDto> {
    @Override
    public Class<DoubleAttributeDto> attrType() {
        return DoubleAttributeDto.class;
    }
}
