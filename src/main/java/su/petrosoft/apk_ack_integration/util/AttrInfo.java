package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.value.Value;

public interface AttrInfo<A extends Attribute<? extends Value<?>>> {
    String name();

    long id();

    Class<A> attrType();
}

