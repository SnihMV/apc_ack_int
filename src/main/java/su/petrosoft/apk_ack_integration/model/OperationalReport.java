package su.petrosoft.apk_ack_integration.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import su.petrosoft.apk_ack_integration.model.dto.plicante.ReportFieldDto;
import su.petrosoft.apk_ack_integration.model.enums.ReportType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationalReport {
    private Long id;
    private Long version;
    private ReportType reportType;
    private List<ReportFieldDto> reportFile;

}
