package su.petrosoft.apk_ack_integration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubsidyEstimatedAmount {
    private Long id;
    private Long version;
    private Long year;
    private Long recipientId;
    private Long subsidyProgramId;
    private BigDecimal sob;
    private BigDecimal sst;
    private BigDecimal sn;
}
