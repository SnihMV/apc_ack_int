package su.petrosoft.apk_ack_integration.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class PlicanteInstance {
    protected Long id;
    protected Long version;
}
