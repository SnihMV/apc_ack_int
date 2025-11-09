package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import java.math.BigDecimal;
import java.util.List;

public record DoubleAttribute(
        Long id,
        String code,
        String type,
        List<Value> value
) implements Attribute {

    public DoubleAttribute(long id, BigDecimal data) {
        this(id, null, "DOUBLE", List.of(new Value(data, null)));
    }

    public DoubleAttribute(String code, BigDecimal data) {
        this(null, code, "DOUBLE", List.of(new Value(data, null)));
    }

    public BigDecimal getData() {
        return (BigDecimal) Attribute.super.getData();
    }

    public BigDecimal getBigDecimalData(){
        if (value == null || value.get(0) == null) {
            return null;
        }
        return (BigDecimal) value.get(0).data();
    }
}
