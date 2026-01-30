package su.petrosoft.apk_ack_integration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SubsidyAmount extends PlicanteInstance {
    private Long year;
    private Long recipientId;
    private Long subsidyProgramId;
    private BigDecimal sob;
    private BigDecimal sst;
    private BigDecimal sn;
}
