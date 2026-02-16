package su.petrosoft.apk_ack_integration.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessage {
    RECIPIENT_BY_ID_NOT_FOUND("Получатель государственной поддержки с id = %d не найден"),
    FAILED_TO_READ_JSON_FILE("Не удалось прочитать json файл. Причина: %s"),
    DICTIONARY_ID_NOT_FOUND("Не найден объект справочника [%s] с id = [%d]"),
    DICTIONARY_CODE_NOT_FOUND("Не найден код [%s] в справочнике [%s]"),
    DICTIONARY_DESCRIPTION_NOT_FOUND("Не найдено описание кода [%s] в справочнике [%s]");
    private final String message;

}
