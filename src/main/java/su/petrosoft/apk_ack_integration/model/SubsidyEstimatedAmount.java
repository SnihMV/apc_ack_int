package su.petrosoft.apk_ack_integration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubsidyEstimatedAmount {
    private Long id;
    private Long version;
    private Long year;
    private Long applicantId;
    private Long subsidyProgramId;
    private Long sob;
    private Long sst;
    private Long sn;
}
