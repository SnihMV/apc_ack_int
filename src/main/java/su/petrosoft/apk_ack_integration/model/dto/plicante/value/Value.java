package su.petrosoft.apk_ack_integration.model.dto.plicante.value;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public sealed interface Value<T>
        permits StringValue, DoubleValue, LongValue,
        BooleanValue, LinkedValue, BlobFileValue {

    T data();

}
