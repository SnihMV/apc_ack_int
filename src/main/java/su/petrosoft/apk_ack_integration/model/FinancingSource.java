package su.petrosoft.apk_ack_integration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancingSource {
    private Long id;
    private Long version;
    private Long year;
    private String kvsr;
    private String kfsr;
    private String kcsr;
    private String kvr;
    private String kosgu;
    private Long subsidyProgramId;
}
