package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttributeDto;

public record StringAttributeInfo(
        String name,
        long id
) implements AttrInfo<StringAttributeDto> {
    @Override
    public Class<StringAttributeDto> attrType() {
        return StringAttributeDto.class;
    }
}
