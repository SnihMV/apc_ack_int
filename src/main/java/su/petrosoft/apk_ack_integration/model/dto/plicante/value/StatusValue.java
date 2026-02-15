package su.petrosoft.apk_ack_integration.model.dto.plicante.value;

public record StatusValue(
    long id
) implements Value<Long> {

    @Override
    public Long data() {
        return id;
    }
}
