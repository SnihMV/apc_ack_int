package su.petrosoft.apk_ack_integration.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessage {
    DICTIONARY_ID_NOT_FOUND("Не найден объект справочника [%s] с id = [%d]"),
    DICTIONARY_CODE_NOT_FOUND("Неверное значение [\"%s\"] справочника [%s]"),
    DICTIONARY_DESCRIPTION_NOT_FOUND("Не найдено описание кода [%s] в справочнике [%s]");
    private final String message;

}
