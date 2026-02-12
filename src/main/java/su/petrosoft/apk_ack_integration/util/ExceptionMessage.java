package su.petrosoft.apk_ack_integration.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessage {
    RECIPIENT_BY_ID_NOT_FOUND("Получатель государственной поддержки с id = %d не найден"),
    FAILED_TO_READ_JSON_FILE("Не удалось прочитать json файл. Причина: %s");

    private final String message;

}
