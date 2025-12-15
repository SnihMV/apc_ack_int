package su.petrosoft.apk_ack_integration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgriculturalMachineryReport {
    private Long id;
    private Long version;
    private Long recipientId;
    private String codedReport;
}
