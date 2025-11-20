package su.petrosoft.apk_ack_integration.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CodeType {
    KVSR("КВСР", 10528L),
    KFSR("КФСР", 10579L),
    KCSR("КЦСР", 10630L),
    KVR("КВР", 10681L),
    KOSGU("КОСГУ", 10732L),
    DOPEK("Доп.ЭК", 10834L),
    DOPKR("Доп.КР", 10885L),
    DOPFK("Доп.ФК", 19070L),
    PURPOSE("Код цели", 11006L);

    private final String name;
    private final Long templateId;
}
