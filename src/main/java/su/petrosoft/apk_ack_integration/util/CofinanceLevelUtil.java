package su.petrosoft.apk_ack_integration.util;

import static su.petrosoft.apk_ack_integration.model.enums.FinancingForm.OB;
import static su.petrosoft.apk_ack_integration.util.DictionaryUtil.DEFAULT_OWNERSHIP_FORM;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.toEpochMilli;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import su.petrosoft.apk_ack_integration.model.CofinancingLevel;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DateAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.DoubleAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttributeDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.request.GettingInstanceRepresentationRequestDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

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
            .financingForm(OB.getId())
            .ownershipForm(DEFAULT_OWNERSHIP_FORM)
            .build();
    }

    public static GettingInstanceRepresentationRequestDto getCofinLevelRepresentationRequestDto(Long id) {
        return GettingInstanceRepresentationRequestDto.builder()
                .instance(InstanceDto.builder()
                        .id(id)
                        .build())
                .build();
    }

    public static CreateInstanceRequestDto creatingRequestDto(CofinancingLevel cflToSave, Map<Dictionary, Map<String, Long>> codesMap) {
        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(TEMPLATE_ID)
                        .attributes(List.of(
                                new LongAttributeDto(YEAR_ATTR, cflToSave.getYear()),
                                new DateAttributeDto(START_DATE_ATTR, toEpochMilli(cflToSave.getStartDate())),
                                new DoubleAttributeDto(COEFF_OB_ATTR, cflToSave.getObCoeff()),
                                new DoubleAttributeDto(COEFF_FB_ATTR, cflToSave.getFbCoeff()),
                                new LinkedAttributeDto(FIN_FORM_ATTR, cflToSave.getFinancingForm()),
                                new LinkedAttributeDto(OWN_FORM_ATTR, cflToSave.getOwnershipForm())))
                        .build());
    }
}