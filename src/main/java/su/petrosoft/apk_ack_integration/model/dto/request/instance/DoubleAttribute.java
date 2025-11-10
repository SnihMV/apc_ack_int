package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.List;

public record DoubleAttribute(
        Long id,
        String code,
        String type,
        List<Value> value,
        String name,
        String description,
        String attributeType
) implements Attribute {

    public DoubleAttribute(long id, BigDecimal data) {
        this(id, null, "DOUBLE", List.of(new Value(data, null)), null, null, null);
    }

    @JsonIgnore
    public BigDecimal getData() {
        return (BigDecimal) Attribute.super.getData();
    }

}
