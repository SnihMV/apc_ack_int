package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.AttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.Value;

public interface AttrInfo<A extends AttributeDto<? extends Value<?>>> {
    String name();

    long id();

    Class<A> attrType();
}

