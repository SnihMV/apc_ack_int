package su.petrosoft.apk_ack_integration.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.CofinancingLevel;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DateAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DoubleAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.CofinancingLevelExcelRow;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static su.petrosoft.apk_ack_integration.model.enums.CodeType.*;
import static su.petrosoft.apk_ack_integration.model.enums.FinancingForm.define;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.COEFF_FB_ATTR;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.COEFF_OB_ATTR;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.FIN_FORM_ATTR;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.START_DATE_ATTR;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.TEMPLATE_ID;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.YEAR_ATTR;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractData;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractShortForm;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.getCodeId;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.toEpochMilli;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.toLocalDate;

@Slf4j
@Component
public class CofinancingLevelMapper {
    public CofinancingLevel toEntity(CofinancingLevelExcelRow row) {
        BigDecimal obCoeff = BigDecimal.valueOf(row.obCoeff());
        BigDecimal fbCoeff = BigDecimal.valueOf(row.fbCoeff());
        return CofinancingLevel.builder()
                .year((long) LocalDate.now().getYear())
                .startDate(LocalDate.of(LocalDate.now().getYear(), 1, 1))
                .obCoeff(obCoeff)
                .fbCoeff(fbCoeff)
                .financingForm(define(obCoeff, fbCoeff).getName())
                .build();
    }

    public CofinancingLevel toEntity(InstanceDto created) {
        List<Attribute<?>> attributes = created.attributes();
        return CofinancingLevel.builder()
                .id(created.id())
                .version(created.version())
                .year(extractData(attributes, YEAR_ATTR))
                .startDate(toLocalDate(extractData(attributes, START_DATE_ATTR)))
                .obCoeff(extractData(attributes, COEFF_OB_ATTR))
                .fbCoeff(extractData(attributes, COEFF_FB_ATTR))
                .financingForm(extractShortForm(attributes, FIN_FORM_ATTR))
                .build();
    }

    public CreateInstanceRequestDto toCreatingDto(CofinancingLevel cl, Map<CodeType, Map<Long, String>> codesMap) {
        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(TEMPLATE_ID)
                        .attributes(List.of(
                                new LongAttribute(YEAR_ATTR, cl.getYear()),
                                new DateAttribute(START_DATE_ATTR, toEpochMilli(cl.getStartDate())),
                                new DoubleAttribute(COEFF_FB_ATTR, cl.getFbCoeff()),
                                new DoubleAttribute(COEFF_OB_ATTR, cl.getObCoeff()),
                                new LinkedAttribute(FIN_FORM_ATTR, getCodeId(codesMap, FINANCING_FORM, cl.getFinancingForm()))
                        ))
                        .build()
        );
    }

}
