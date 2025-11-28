package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record LongAttribute(
        Long id,
        String code,
        String type,
        List<LongValue> value
) implements Attribute<LongValue> {

    public LongAttribute(long id) {
        this(id, null);
    }

    public LongAttribute(long id, Long data) {
        this(id, null, "LONG", List.of(new LongValue(data)));
    }

    @Override
    @JsonIgnore
    public LongValue getFirstValue() {
        return Attribute.super.getFirstValue();
    }

    @Override
    @JsonIgnore
    public Long getData(){
        LongValue firstValue = getFirstValue();
        return firstValue != null ? firstValue.data() : null;
    }
}