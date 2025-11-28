package su.petrosoft.apk_ack_integration.model.dto.request.instance;

public sealed interface Value<T>
        permits StringValue, DoubleValue, LongValue,
        BooleanValue, DateValue, LinkedValue, BlobFileValue {

    T data();

}
