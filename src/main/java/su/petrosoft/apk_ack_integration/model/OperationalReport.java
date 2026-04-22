package su.petrosoft.apk_ack_integration.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import su.petrosoft.apk_ack_integration.model.enums.ReportType;

import java.math.BigDecimal;
import java.util.Map;

import static su.petrosoft.apk_ack_integration.util.OperationalReportUtil.TEMPLATE_ID;

@Getter
@Setter
@SuperBuilder
public class OperationalReport extends PlicanteInstance {
    private Long recipientId;
    private ReportType reportType;
    private Long reportDate;
    private Map<String, BigDecimal> reportValues;

    public void addValue(String fieldCode, BigDecimal value) {
        reportValues.put(fieldCode, value);
    }

    @Override
    public long getTemplateId() {
        return TEMPLATE_ID;
    }
}
