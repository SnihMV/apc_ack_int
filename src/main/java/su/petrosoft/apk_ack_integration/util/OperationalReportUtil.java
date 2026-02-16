package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.dto.plicante.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.RequestedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.DateFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.Filter;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.LinkedFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.StatusFilterAttribute;
import su.petrosoft.apk_ack_integration.model.enums.ReportType;
import su.petrosoft.apk_ack_integration.model.enums.SqlOperation;
import su.petrosoft.apk_ack_integration.model.enums.ViewType;

import java.util.List;

public class OperationalReportUtil {

    public static final long TEMPLATE_ID = 5575;
    public static final long STATUS_ATTR = 889;

    public static final long REPORT_TYPE_ATTR = 892;
    public static final long RECIPIENT_ATTR = 3780;
    public static final long REPORT_DATE_ATTR = 3850;
    public static final long FILE_JSON_ATTR = 3127;


    public static GetAttributesListRequestDto requestDtoForGettingOperationalReportsByTypeAndDate(
        ReportType reportType, long date) {
        return GetAttributesListRequestDto.builder()
            .templateId(TEMPLATE_ID)
            .viewType(ViewType.DETAILED_FORM_VIEW)
            .getBinaries(true)
            .attributes(List.of(
                new RequestedAttribute(RECIPIENT_ATTR),
                new RequestedAttribute(REPORT_TYPE_ATTR),
                new RequestedAttribute(REPORT_DATE_ATTR),
                new RequestedAttribute(FILE_JSON_ATTR)
            ))
            .filter(new Filter(List.of(
                new LinkedFilterAttribute(REPORT_TYPE_ATTR, reportType.getId()),
                new DateFilterAttribute(REPORT_DATE_ATTR, date)
            )))
            .build();
    }

    public static GetAttributesListRequestDto requestDtoForGettingOperationalReportsByTypeAndStatusAndDateInterval(
        ReportType reportType, long from, long to) {
        return GetAttributesListRequestDto.builder()
            .templateId(TEMPLATE_ID)
            .viewType(ViewType.DETAILED_FORM_VIEW)
            .getBinaries(true)
            .attributes(List.of(
                new RequestedAttribute(RECIPIENT_ATTR),
                new RequestedAttribute(REPORT_TYPE_ATTR),
                new RequestedAttribute(REPORT_DATE_ATTR),
                new RequestedAttribute(FILE_JSON_ATTR)
            ))
            .filter(new Filter(List.of(
                new LinkedFilterAttribute(REPORT_TYPE_ATTR, reportType.getId()),
                new StatusFilterAttribute(STATUS_ATTR, 5858),
                new DateFilterAttribute(REPORT_DATE_ATTR, List.of(SqlOperation.BETWEEN), from, to)
            )))
            .build();
    }

}
