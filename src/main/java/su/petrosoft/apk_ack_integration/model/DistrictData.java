package su.petrosoft.apk_ack_integration.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DistrictData {

    private final String name;
    private final List<ProducerData> producers;
    private final Map<String, BigDecimal> sums = new HashMap<>();

    public void addProducer(ProducerData producerData) {
        producers.add(producerData);
        for (Entry<String, BigDecimal> entry : producerData.getValues().entrySet()) {
            sums.merge(entry.getKey(), entry.getValue(), BigDecimal::add);
        }
    }

    public BigDecimal getSum(String key) {
        return sums.getOrDefault(key, BigDecimal.ZERO);
    }

    public void calculateDistrictTotal() {
        sums.clear();
        for (ProducerData producer : producers) {
            for (Entry<String, BigDecimal> entry : producer.getValues().entrySet()) {
                sums.merge(entry.getKey(), entry.getValue(), BigDecimal::add);
            }
        }
    }

    public BigDecimal percent(String numeratorKey, String denominatorKey) {
        BigDecimal numerator = getSum(numeratorKey);
        BigDecimal denominator = getSum(denominatorKey);

        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return numerator.multiply(BigDecimal.valueOf(100))
            .divide(denominator, 2, RoundingMode.HALF_UP);
    }
}
