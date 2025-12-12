package su.petrosoft.apk_ack_integration.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CodeType {
    KVSR("КВСР", 10528, 1854),
    KFSR("КФСР", 10579, 1874),
    KCSR("КЦСР", 10630, 1882),
    KVR("КВР", 10681, 1878),
    KOSGU("КОСГУ", 10732, 1870),
    DOPEK("Доп.ЭК", 10834, 1861),
    DOPKR("Доп.КР", 10885, 1867),
    DOPFK("Доп.ФК", 19070, 3492),
    PURPOSE("Код цели", 11006, 1858),
    OWNERSHIP_FORM("Форма собственности", 25265, 4293),
    FINANCING_FORM("Форма финансирования", 25327, 4294);

    private final String name;
    private final long templateId;
    private final long valuedAttrId;
}
