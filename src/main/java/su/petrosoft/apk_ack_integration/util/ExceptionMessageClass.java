package su.petrosoft.apk_ack_integration.util;

public class ExceptionMessageClass {
    public static final String FILE_IS_EMPTY = "File [%s] is empty";
    public static final String INVALID_XML_FORMAT = "Can not parse xml by the reason: [%s]";
    public static final String NO_CONTENT = "File [%s] has no effective content";
    public static final String MARKER_NOT_FOUND = "Не найдена строка с маркерами района [%s%s] в шаблоне";
    public static final String INVALID_MARKER = "Неверный формат маркера: [%s]";
    public static final String REPORTS_NOT_FOUND = "Не нашлось ни одного оперативного отчета с заданными параметрами";
    public static final String FAILED_TO_READ_JSON_FILE = "Не удалось прочитать json файл. Причина: %s";
    public static final String INVALID_FIELD_NAME = "Недопустимое имя поля [%s]";
    public static final String JSON_NODE_ABSENT = "Required node [%s] is absent";
    public static final String JSON_FIELD_ABSENT = "Required field [%s] is absent";
    public static final String INSTANCE_NOT_FOUND_BY_ID = "Экземпляр с id = [%d] журнала [%s] не найден";

}
