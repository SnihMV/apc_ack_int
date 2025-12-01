package su.petrosoft.apk_ack_integration.model.dto.plicante.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import su.petrosoft.apk_ack_integration.model.enums.ValueType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public sealed interface FilterAttribute<T>
        permits LongFilterAttribute, StringFilterAttribute, DoubleFilterAttribute,
        LinkedFilterAttribute, BlobFileFilterAttribute, DateFilterAttribute, BooleanFilterAttribute {

    Long id();

    ValueType type();

    Condition<T> condition();
}
