package su.petrosoft.apk_ack_integration.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import static su.petrosoft.apk_ack_integration.util.AgriculturalMachineryReportUtil.TEMPLATE_ID;

@Getter
@Setter
@SuperBuilder
public class AgriculturalMachineryReport extends PlicanteInstance {
    private Long recipientId;
    private String jsonReport;

    @Override
    public long getTemplateId() {
        return TEMPLATE_ID;
    }
}
