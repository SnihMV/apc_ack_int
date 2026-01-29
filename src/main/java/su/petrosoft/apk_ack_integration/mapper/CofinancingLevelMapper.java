package su.petrosoft.apk_ack_integration.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.CofinancingLevel;
import su.petrosoft.apk_ack_integration.model.data.CofinancingLevelData;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DateAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DoubleAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;
import su.petrosoft.apk_ack_integration.model.enums.FinancingForm;
import su.petrosoft.apk_ack_integration.model.enums.OwnershipForm;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.*;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.COEFF_FB_ATTR;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.COEFF_OB_ATTR;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.FIN_FORM_ATTR;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.OWN_FORM_ATTR;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.START_DATE_ATTR;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.TEMPLATE_ID;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.YEAR_ATTR;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractData;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractShortForm;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.getDictionaryCodeById;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.getDictionaryIdByCode;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.toEpochMilli;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.toLocalDate;

@Slf4j
@Component
public class CofinancingLevelMapper {
    public CofinancingLevel toEntity(CofinancingLevelData row) {
        BigDecimal obCoeff = BigDecimal.valueOf(row.obCoeff());
        BigDecimal fbCoeff = BigDecimal.valueOf(row.fbCoeff());
        return CofinancingLevel.builder()
                .year((long) LocalDate.now().getYear())
                .startDate(LocalDate.of(LocalDate.now().getYear(), 1, 1))
                .obCoeff(obCoeff)
                .fbCoeff(fbCoeff)
                .financingForm(FinancingForm.define(obCoeff, fbCoeff).getName())
                .ownershipForm(OwnershipForm.define(row.kosgu()).getName())
                .build();
    }

    public CofinancingLevel toEntity(InstanceDto created, Map<Dictionary, Map<String, Long>> codesMap) {
        List<Attribute<?>> attributes = created.attributes();
        return CofinancingLevel.builder()
                .id(created.id())
                .version(created.version())
                .year(extractData(attributes, YEAR_ATTR))
                .startDate(toLocalDate(extractData(attributes, START_DATE_ATTR)))
                .obCoeff(extractData(attributes, COEFF_OB_ATTR))
                .fbCoeff(extractData(attributes, COEFF_FB_ATTR))
                .financingForm(getDictionaryCodeById(codesMap, FINANCING_FORM, extractData(attributes, FIN_FORM_ATTR)))
                .ownershipForm(getDictionaryCodeById(codesMap, OWNERSHIP_FORM, extractData(attributes, OWN_FORM_ATTR)))
                .build();
    }

    public CofinancingLevel toEntity(List<Attribute<?>> attributes, Map<Dictionary, Map<String, Long>> codesMap) {
        return CofinancingLevel.builder()
                .year(extractData(attributes, YEAR_ATTR))
                .startDate(toLocalDate(extractData(attributes, START_DATE_ATTR)))
                .obCoeff(extractData(attributes, COEFF_OB_ATTR))
                .fbCoeff(extractData(attributes, COEFF_FB_ATTR))
                .financingForm(getDictionaryCodeById(codesMap, FINANCING_FORM, extractData(attributes, FIN_FORM_ATTR)))
                .ownershipForm(getDictionaryCodeById(codesMap, OWNERSHIP_FORM, extractData(attributes, OWN_FORM_ATTR)))
                .build();
    }

    public CreateInstanceRequestDto toCreatingDto(CofinancingLevel cl, Map<Dictionary, Map<String, Long>> codesMap) {
        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(TEMPLATE_ID)
                        .attributes(List.of(
                                new LongAttribute(YEAR_ATTR, cl.getYear()),
                                new DateAttribute(START_DATE_ATTR, toEpochMilli(cl.getStartDate())),
                                new DoubleAttribute(COEFF_FB_ATTR, cl.getFbCoeff()),
                                new DoubleAttribute(COEFF_OB_ATTR, cl.getObCoeff()),
                                new LinkedAttribute(OWN_FORM_ATTR, getDictionaryIdByCode(codesMap, OWNERSHIP_FORM, cl.getOwnershipForm())),
                                new LinkedAttribute(FIN_FORM_ATTR, getDictionaryIdByCode(codesMap, FINANCING_FORM, cl.getFinancingForm()))))
                        .build());
    }

}
