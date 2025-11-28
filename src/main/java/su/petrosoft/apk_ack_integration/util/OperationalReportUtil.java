package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.dto.request.AttributeDefinition;
import su.petrosoft.apk_ack_integration.model.dto.request.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.enums.ViewType;

import java.util.List;
import java.util.Map;

import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.makeSimpleLongAttributeFilter;

public class OperationalReportUtil {
    public static final long TEMPLATE_ID = 5575;

    public static final long REPORT_TYPE_ATTR = 892;
    public static final long CURRENT_DATE_ATTR = 3850;
    public static final long FILE_JSON_ATTR = 3127;

    public static GetAttributesListRequestDto buildGetReportsForFillingMainFormRequestDto(Long date) {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(ViewType.DETAILED_FORM_VIEW)
                .getBinaries(true)
                .attributes(List.of(new AttributeDefinition(FILE_JSON_ATTR)))
                .filter(makeSimpleLongAttributeFilter(Map.of(REPORT_TYPE_ATTR, 630, CURRENT_DATE_ATTR, date)))
                .build();
    }

}
