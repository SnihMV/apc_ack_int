package su.petrosoft.apk_ack_integration.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

import static su.petrosoft.apk_ack_integration.util.SubsidyAmountUtil.TEMPLATE_ID;

@Getter
@Setter
@SuperBuilder
public class SubsidyAmount extends PlicanteInstance {
    private Long year;
    private Long recipientId;
    private Long subsidyProgramId;
    private BigDecimal sob;
    private BigDecimal sst;
    private BigDecimal sn;

    @Override
    public long getTemplateId() {
        return TEMPLATE_ID;
    }
}
