package su.petrosoft.apk_ack_integration.model;

import java.math.BigDecimal;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import su.petrosoft.apk_ack_integration.model.enums.ReportType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class OperationalReport extends PlicanteInstance {
    private Long recipientId;
    private ReportType reportType;
    private Long reportDate;
    private Map<String, BigDecimal> reportValues;

    public void addValue(String fieldCode, BigDecimal value) {
        reportValues.put(fieldCode, value);
    }
}
