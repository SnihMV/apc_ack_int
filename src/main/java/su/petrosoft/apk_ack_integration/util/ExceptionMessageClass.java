package su.petrosoft.apk_ack_integration.util;

public class ExceptionMessageClass {
    public static final String FILE_IS_EMPTY = "File [%s] is empty";
    public static final String INVALID_XML_FORMAT = "Can not parse xml by the reason: [%s]";
    public static final String NO_CONTENT = "File [%s] has no effective content";
    public static final String MARKER_NOT_FOUND = "Не найдена строка с маркерами района [%s%s] в шаблоне";
    public static final String INVALID_MARKER = "Неверный формат маркера: [%s]";
    public static final String REPORTS_NOT_FOUND = "Не нашлось ни одного оперативного отчета с заданными параметрами";
    public static final String FAILED_TO_PARSE_JSON_FILE = "Не удалось интерпретировать json файл. Причина: %s";
    public static final String INVALID_FIELD_NAME = "Недопустимое имя поля [%s]";
    public static final String JSON_NODE_ABSENT = "Обязательное поле [%s] не найдено в json файле";
    public static final String JSON_FIELDS_ABSENT = "Обязательные поля вида [%s*] отсутствуют в json файле";
    public static final String INSTANCE_NOT_FOUND_BY_ID = "Экземпляр с id = [%d] журнала [%s] не найден";
    public static final String DICTIONARY_ID_NOT_FOUND = "Не найден экземпляр справочника [%s] с id = [%d]";
    public static final String DICTIONARY_CODE_NOT_FOUND = "Неверное значение [%s] справочника [%s]";
    public static final String DICTIONARY_DESCRIPTION_NOT_FOUND = "Не найдено описание кода [%s] в справочнике [%s]";

}
