package su.petrosoft.apk_ack_integration.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import su.petrosoft.apk_ack_integration.model.CofinancingLevel;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DateAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DoubleAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.request.GettingInstanceRepresentationRequestDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

import java.util.List;
import java.util.Map;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.FINANCING_FORM;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.OWNERSHIP_FORM;
import static su.petrosoft.apk_ack_integration.model.enums.FinancingForm.*;
import static su.petrosoft.apk_ack_integration.model.enums.OwnershipForm.*;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.*;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.dictionaryIdByCode;

public class CofinanceLevelUtil {
    public static final long TEMPLATE_ID = 25588;
    public static final String SP_TITLE = "Уровень софинансирования";

    public static final long YEAR_ATTR = 3410;
    public static final long START_DATE_ATTR = 3411;
    public static final long COEFF_FB_ATTR = 3494;
    public static final long COEFF_OB_ATTR = 3495;
    public static final long OWN_FORM_ATTR = 3413;
    public static final long FIN_FORM_ATTR = 3414;

    public static CofinancingLevel getDefaultCfl() {
        int currentYear = LocalDate.now().getYear();
        return CofinancingLevel.builder()
            .year((long) currentYear)
            .startDate(LocalDate.of(currentYear, 1, 1))
            .obCoeff(BigDecimal.ONE)
            .fbCoeff(BigDecimal.ZERO)
            .financingForm(OB)
            .ownershipForm(ALL)
            .build();
    }

    public static GettingInstanceRepresentationRequestDto getCofinLevelRepresentationRequestDto(Long id) {
        return GettingInstanceRepresentationRequestDto.builder()
                .instance(InstanceDto.builder()
                        .id(id)
                        .build())
                .build();
    }

    public static CreateInstanceRequestDto creatingRequestDto(CofinancingLevel cflToSave, Map<Dictionary, Map<Long, String>> codesMap) {
        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(TEMPLATE_ID)
                        .attributes(List.of(
                                new LongAttribute(YEAR_ATTR, cflToSave.getYear()),
                                new DateAttribute(START_DATE_ATTR, toEpochMilli(cflToSave.getStartDate())),
                                new DoubleAttribute(COEFF_OB_ATTR, cflToSave.getObCoeff()),
                                new DoubleAttribute(COEFF_FB_ATTR, cflToSave.getFbCoeff()),
                                new LinkedAttribute(FIN_FORM_ATTR, dictionaryIdByCode(codesMap, FINANCING_FORM, cflToSave.getFinancingForm().getCode())),
                                new LinkedAttribute(OWN_FORM_ATTR, dictionaryIdByCode(codesMap, OWNERSHIP_FORM, cflToSave.getOwnershipForm().getCode()))))
                        .build());
    }
}