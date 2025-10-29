package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import java.math.BigDecimal;
import java.util.List;

public record DoubleAttribute(
        Integer id,
        String code,
        String type,
        List<DoubleValue> value
) implements Attribute {

    public DoubleAttribute(int id, BigDecimal data) {
        this(id, null, "DOUBLE", List.of(new DoubleValue(data)));
    }

    public DoubleAttribute(String code, BigDecimal data) {
        this(null, code, "DOUBLE", List.of(new DoubleValue(data)));
    }

    public record DoubleValue(
            BigDecimal data
    ) {
    }
}
