package su.petrosoft.apk_ack_integration.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DistrictData implements Comparable<DistrictData> {

    private final String name;
    private final List<ProducerData> producers;
    private final Map<String, BigDecimal> sums = new HashMap<>();

    public void addProducer(ProducerData producerData) {
        producers.add(producerData);
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

    @Override
    public int compareTo(DistrictData that) {
        return this.name.compareTo(that.name);
    }
}
