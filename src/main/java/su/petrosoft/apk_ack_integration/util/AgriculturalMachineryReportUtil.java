package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.request.GettingInstanceRepresentationRequestDto;

public class AgriculturalMachineryReportUtil {
    public static final long TEMPLATE_ID = 6248;
    public static final String TITLE = "Отчёты по парку сельскохозяйственной техники и оборудования";

    public static final long RECIPIENT_ID = 957;
    public static final long JSON_FILE_ATTR = 3066;

    public static GettingInstanceRepresentationRequestDto buildRequestDtoForReportProcessing(Long id) {
        return GettingInstanceRepresentationRequestDto.builder()
                .instance(InstanceDto.builder()
                        .id(id)
                        .build())
                .getBinaries(true)
                .build();

    }
}
