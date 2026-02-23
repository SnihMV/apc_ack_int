package su.petrosoft.apk_ack_integration.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import lombok.Data;

@Data
public class ProducerData {

    private final String name;
    private final String inn;
    private final Map<String, BigDecimal> values;

    public BigDecimal getValue(String key) {
        return values.getOrDefault(key, BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
    }

    public void addValue(String key, BigDecimal value) {
        values.put(key, value);
    }
}
