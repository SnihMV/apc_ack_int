package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.AgriculturalMachineryReport;
import su.petrosoft.apk_ack_integration.model.dto.plicante.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.RequestedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.Filter;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.LongFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.request.GettingInstanceRepresentationRequestDto;

import java.util.Collection;
import java.util.List;

import static su.petrosoft.apk_ack_integration.model.enums.ViewType.DETAILED_FORM_VIEW;

public class AgriculturalMachineryReportUtil {
    public static final long TEMPLATE_ID = 6248;
    public static final String TITLE = "Отчёты по парку сельскохозяйственной техники и оборудования";

    public static final long ID_ATTR = 950;
    public static final long RECIPIENT_ID = 957;
    public static final long JSON_FILE_ATTR = 3066;
    public static final long MACHINE_PARK_ATTR = 3070;

    public static GettingInstanceRepresentationRequestDto requestDtoForReportProcessing(Long id) {
        return GettingInstanceRepresentationRequestDto.builder()
                .instance(InstanceDto.builder()
                        .id(id)
                        .build())
                .getBinaries(true)
                .build();

    }

    public static GetAttributesListRequestDto requestDtoForGetReportById(long id) {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(DETAILED_FORM_VIEW)
                .getBinaries(true)
                .attributes(List.of(
                        new RequestedAttribute(RECIPIENT_ID),
                        new RequestedAttribute(JSON_FILE_ATTR)
                ))
                .filter(new Filter(List.of(
                        new LongFilterAttribute(ID_ATTR, id)
                )))
                .build();
    }

    public static UpdateInstanceRequestDto requestDtoForUpdateReportByParks(AgriculturalMachineryReport report, List<Long> parkIds) {
        return new UpdateInstanceRequestDto(
                InstanceDto.builder()
                        .id(report.getId())
                        .templateId(AgriculturalMachineryReportUtil.TEMPLATE_ID)
                        .version(report.getVersion())
                        .attributes(List.of(
                                new LinkedAttribute(MACHINE_PARK_ATTR, parkIds)))
                        .build());
    }
}
