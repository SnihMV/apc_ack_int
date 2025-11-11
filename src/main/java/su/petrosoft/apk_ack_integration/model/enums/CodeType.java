package su.petrosoft.apk_ack_integration.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CodeType {
    KADMR("КВСР", 10528L, 1733L),
    KFSR("КФСР", 10579L, 1734L),
    KCSR("КЦСР", 10630L, 1735L),
    KVR("КВР", 10681L, 1736L),
    KESR("КОСГУ", 10732L, 1737L),
    KDE("Доп.ЭК", 10834L, 1739L),
    KDR("Доп.КР", 10885L, 1740L),
    PURPOSEFULGRANT("Код цели", 11006L, 1751L),
    KDF("Доп.ФК", 19070L, 3448L);

    private final String name;
    private final Long templateId;
    private final Long attributeId;
}
