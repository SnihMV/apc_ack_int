package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.DateFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.Filter;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.LinkedFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.request.RequestedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.request.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.enums.ViewType;

import java.util.List;

public class OperationalReportUtil {
    public static final long TEMPLATE_ID = 5575;

    public static final long REPORT_TYPE_ATTR = 892;
    public static final long CURRENT_DATE_ATTR = 3850;
    public static final long FILE_JSON_ATTR = 3127;

    public static GetAttributesListRequestDto buildGetReportsForFillingMainFormRequestDto(long date) {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(ViewType.DETAILED_FORM_VIEW)
                .getBinaries(true)
                .attributes(List.of(new RequestedAttribute(FILE_JSON_ATTR)))
//                .filter(makeSimpleLongAttributeFilter(Map.of(REPORT_TYPE_ATTR, 630, CURRENT_DATE_ATTR, date)))
                .filter(new Filter(List.of(
                        new LinkedFilterAttribute(REPORT_TYPE_ATTR, 630),
                        new DateFilterAttribute(CURRENT_DATE_ATTR, date)
                )
                ))
                .build();
    }

}
