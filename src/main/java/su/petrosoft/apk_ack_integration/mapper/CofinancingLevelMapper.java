package su.petrosoft.apk_ack_integration.mapper;

import static su.petrosoft.apk_ack_integration.model.enums.FinancingForm.finFormByCoeffs;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.COEFF_FB_ATTR;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.COEFF_OB_ATTR;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.FIN_FORM_ATTR;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.OWN_FORM_ATTR;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.START_DATE_ATTR;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.TEMPLATE_ID;
import static su.petrosoft.apk_ack_integration.util.CofinanceLevelUtil.YEAR_ATTR;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.extractData;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.toEpochMilli;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.toLocalDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.CofinancingLevel;
import su.petrosoft.apk_ack_integration.model.data.CofinancingLevelData;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.AttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DateAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DoubleAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;
import su.petrosoft.apk_ack_integration.util.DictionaryUtil;

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
                .financingForm(finFormByCoeffs(obCoeff, fbCoeff).getId())
                .ownershipForm(DictionaryUtil.ownershipForm(row.kosgu()))
                .build();
    }

    public CofinancingLevel toEntity(InstanceDto created, Map<Dictionary, Map<String, Long>> codesMap) {

        List<AttributeDto<?>> attributeDtos = created.attributeDtos();

        return CofinancingLevel.builder()
                .id(created.id())
                .version(created.version())
                .year(extractData(attributeDtos, YEAR_ATTR))
                .startDate(toLocalDate(extractData(attributeDtos, START_DATE_ATTR)))
                .obCoeff(extractData(attributeDtos, COEFF_OB_ATTR))
                .fbCoeff(extractData(attributeDtos, COEFF_FB_ATTR))
                .financingForm(extractData(attributeDtos, FIN_FORM_ATTR))
                .ownershipForm(extractData(attributeDtos, OWN_FORM_ATTR))
                .build();
    }

    public CofinancingLevel toEntity(List<AttributeDto<?>> attributeDtos, Map<Dictionary, Map<String, Long>> codesMap) {

//        FinancingForm financingForm = finFormByCode(dictionaryCodeById(codesMap, FINANCING_FORM, extractData(attributes, FIN_FORM_ATTR)));

        return CofinancingLevel.builder()
                .year(extractData(attributeDtos, YEAR_ATTR))
                .startDate(toLocalDate(extractData(attributeDtos, START_DATE_ATTR)))
                .obCoeff(extractData(attributeDtos, COEFF_OB_ATTR))
                .fbCoeff(extractData(attributeDtos, COEFF_FB_ATTR))
                .financingForm(extractData(attributeDtos, FIN_FORM_ATTR))
                .ownershipForm(extractData(attributeDtos, OWN_FORM_ATTR))
                .build();
    }

    public CreateInstanceRequestDto toCreatingDto(CofinancingLevel cl, Map<Dictionary, Map<String, Long>> codesMap) {

        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(TEMPLATE_ID)
                        .attributes(List.of(
                                new LongAttributeDto(YEAR_ATTR, cl.getYear()),
                                new DateAttributeDto(START_DATE_ATTR, toEpochMilli(cl.getStartDate())),
                                new DoubleAttributeDto(COEFF_FB_ATTR, cl.getFbCoeff()),
                                new DoubleAttributeDto(COEFF_OB_ATTR, cl.getObCoeff()),
                                new LinkedAttributeDto(OWN_FORM_ATTR, cl.getOwnershipForm()),
                                new LinkedAttributeDto(FIN_FORM_ATTR, cl.getFinancingForm())))
                        .build());
    }

}
