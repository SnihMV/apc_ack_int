package su.petrosoft.apk_ack_integration.util;

import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.DictionaryData;
import su.petrosoft.apk_ack_integration.model.FinancingSource;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LinkedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.LongAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.Filter;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.LongFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;
import su.petrosoft.apk_ack_integration.model.enums.ViewType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPKR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KCSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KFSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KVR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KVSR;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.dictionaryDataById;

public class FinancingSourceUtil {
    public static final long TEMPLATE_ID = 25387;
    public static final String FS_TITLE = "Источник финансирования";

    public static final long ID_ATTR = 3383;
    public static final long YEAR_ATTR = 3393;
    public static final long KVSR_ATTR = 3449;
    public static final long KFSR_ATTR = 3450;
    public static final long KCSR_ATTR = 3451;
    public static final long KVR_ATTR = 3452;
    public static final long KOSGU_ATTR = 3453;
    public static final long DOPFK_ATTR = 3454;
    public static final long DOPEK_ATTR = 3455;
    public static final long DOPKR_ATTR = 3456;
    public static final long PURPOSE_ATTR = 3457;
    public static final long OWNERSHIP_FORM_ATTR = 3394;
    public static final long SUBSIDY_PROGRAM_ATTR = 3461;
    public static final long CASH_PLAN_LIMITS_ATTR = 4429;
    public static final long CONCAT_KBK_ATTR = 3842;

    public static GetAttributesListRequestDto requestDtoToGetSourcesByYear(long year) {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(ViewType.DETAILED_FORM_VIEW)
                .filter(new Filter(List.of(
                        new LongFilterAttribute(YEAR_ATTR, year)
                )))
                .build();
    }

    public static GetAttributesListRequestDto requestDtoForGettingFsById(long id) {
        return GetAttributesListRequestDto.builder()
                .templateId(TEMPLATE_ID)
                .viewType(ViewType.DETAILED_FORM_VIEW)
                .filter(new Filter(List.of(
                        new LongFilterAttribute(ID_ATTR, id)
                )))
                .build();
    }

    public static CreateInstanceRequestDto requestDtoToCreateFinancingSource(FinancingSource source) {
        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(TEMPLATE_ID)
                        .attributes(getAttributesToCreate(source))
                        .build()
        );
    }

    public static UpdateInstanceRequestDto requestDtoForUpdateByLimits(long id, long version, Set<Long> limitIds) {
        return new UpdateInstanceRequestDto(
                InstanceDto.builder()
                        .id(id)
                        .version(version)
                        .attributes(List.of(
                                new LinkedAttribute(CASH_PLAN_LIMITS_ATTR, limitIds)
                        ))
                        .build()
        );
    }

    private static List<Attribute<?>> getAttributesToCreate(FinancingSource source) {
        if (source.getYear() == null || source.getKfsr() == null ||
                source.getKcsr() == null || source.getKvr() == null ||
                source.getKosgu() == null || source.getKvsr() == null ||
                source.getDopFk() == null || source.getDopEk() == null ||
                source.getDopKr() == null || source.getPurpose() == null ||
                source.getConcatenatedKBK() == null || source.getConcatenatedKBK().isBlank()
        ) {
            throw new IllegalStateException("Обязательный атрибут объекта не инициализирован");
        }
        List<Attribute<?>> attrs = new ArrayList<>(getIdentAttributes(source));
        attrs.add(new StringAttribute(CONCAT_KBK_ATTR, source.getConcatenatedKBK()));
        if (source.getOwnershipForm() != null) {
            attrs.add(new LinkedAttribute(OWNERSHIP_FORM_ATTR, source.getOwnershipForm()));
        }
        if (source.getCashPlanLimitIds() != null && !source.getCashPlanLimitIds().isEmpty()) {
            attrs.add(new LinkedAttribute(CASH_PLAN_LIMITS_ATTR, source.getCashPlanLimitIds()));
        }
        return attrs;
    }

    private static Collection<? extends Attribute<?>> getIdentAttributes(FinancingSource creator) {
        return List.of(
                new LongAttribute(YEAR_ATTR, creator.getYear()),
                new LinkedAttribute(KVSR_ATTR, creator.getKvsr()),
                new LinkedAttribute(KFSR_ATTR, creator.getKfsr()),
                new LinkedAttribute(KCSR_ATTR, creator.getKcsr()),
                new LinkedAttribute(KVR_ATTR, creator.getKvr()),
                new LinkedAttribute(KOSGU_ATTR, creator.getKosgu()),
                new LinkedAttribute(DOPFK_ATTR, creator.getDopFk()),
                new LinkedAttribute(DOPEK_ATTR, creator.getDopEk()),
                new LinkedAttribute(DOPKR_ATTR, creator.getDopKr()),
                new LinkedAttribute(PURPOSE_ATTR, creator.getPurpose())
        );
    }

    public static FinancingSource extractFromLimit(CashPlanLimit cpl) {
        return FinancingSource.builder()
                .year(cpl.getYear())
                .kvsr(cpl.getKvsr())
                .kfsr(cpl.getKfsr())
                .kcsr(cpl.getKcsr())
                .kvr(cpl.getKvr())
                .kosgu(cpl.getKosgu())
                .dopFk(cpl.getDopFk())
                .dopEk(cpl.getDopEk())
                .dopKr(cpl.getDopKr())
                .purpose(cpl.getPurpose())
                .build();
    }

    public static String buildConcatKBK(FinancingSource fs, Map<Dictionary, Map<DictionaryData, Long>> codesMap) {
        return fs.getYear() +
                "-" +
                dictionaryDataById(codesMap, KVSR, fs.getKvsr()).getCode() +
                dictionaryDataById(codesMap, KFSR, fs.getKfsr()).getCode() +
                dictionaryDataById(codesMap, KCSR, fs.getKcsr()).getCode() +
                dictionaryDataById(codesMap, KVR, fs.getKvr()).getCode() +
                "-" +
                dictionaryDataById(codesMap, DOPKR, fs.getDopKr()).getCode();
    }
}
