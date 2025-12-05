package su.petrosoft.apk_ack_integration.model;

import java.math.BigDecimal;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import su.petrosoft.apk_ack_integration.model.enums.ReportType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationalReport {
    private Long id;
    private Long version;
    private ReportType reportType;
    private Long reportDate;
    private Map<String, BigDecimal> reportValues;

}
