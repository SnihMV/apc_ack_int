package su.petrosoft.apk_ack_integration.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CodeType {
    KVSR("КВСР", 10528),
    KFSR("КФСР", 10579),
    KCSR("КЦСР", 10630),
    KVR("КВР", 10681),
    KOSGU("КОСГУ", 10732),
    DOPEK("Доп.ЭК", 10834),
    DOPKR("Доп.КР", 10885),
    DOPFK("Доп.ФК", 19070),
    PURPOSE("Код цели", 11006),
    OWNERSHIP_FORM("Форма собственности", 25265),
    FINANCING_FORM("Форма финансирования", 25327);

    private final String name;
    private final long templateId;
    private final long valuedAttrId;
}
