package su.petrosoft.apk_ack_integration.util;

public class ExceptionMessageClass {
    public static final String FILE_IS_EMPTY = "Файл [%s] пустой";
    public static final String FAILED_TO_PARSE_XML = "Не удалось обработать xml. Причина: %s\"";
    public static final String NO_CONTENT = "File [%s] has no effective content";
    public static final String MARKER_NOT_FOUND = "Не найдена строка с маркерами района [%s%s] в шаблоне";
    public static final String INVALID_MARKER = "Неверный формат маркера: [%s]";
    public static final String REPORTS_NOT_FOUND = "Не нашлось ни одного оперативного отчета с заданными параметрами";
    public static final String MACHINERY_REPORT_READING_ERROR = "Проблема в [%d] позиции отчета с id = [%d]: [\"%s\"]";
    public static final String FAILED_TO_PARSE_JSON = "Не удалось обработать json файл оперативного отчета с id = [%d]. Причина: %s";
    public static final String INVALID_FIELD_NAME = "Недопустимое имя поля [%s]";
    public static final String JSON_NODE_ABSENT = "Обязательное поле [%s] не найдено в json файле";
    public static final String JSON_FIELDS_ABSENT = "Обязательные поля вида [%s*] отсутствуют в json файле";
    public static final String INCORRECT_PARKS_COUNT = "Количество техники, приобретенной с гос. поддержкой меньше текущего значения";
    public static final String INSTANCE_NOT_FOUND_BY_ID = "Экземпляр с id = [%d] журнала [%s] не найден";
    public static final String INSTANCE_NOT_FOUND = "Запрашиваемый экземпляр журнала [%s] не найден";
    public static final String DICTIONARY_ID_NOT_FOUND = "Экземпляр справочника [%s] с id = [%d] не найден";
    public static final String DICTIONARY_CODE_NOT_FOUND = "Неверное значение [%s] справочника [%s]";
    public static final String DICTIONARY_NOT_OBTAINED = "Справочник [%s] не был извлечен из БД";
    public static final String DICTIONARY_DESCRIPTION_NOT_FOUND = "Не найдено описание кода [%s] в справочнике [%s]";
    public static final String MANAGED_DICTIONARY_NOT_FOUND = "Управляемый справочник [%s] не найден";
    public static final String INSTANCE_STALE_VERSION = "Объект [id = %d] был изменен в другой сессии";
    public static final String EMPTY_INSTANCE_ID = "Идентификатор объекта не определен (null)";
    public static final String CONVERT_TO_BIGDECIMAL_ERROR = "Не удалось преобразовать к типу BigDecimal значение: [%s]";
    public static final String JSON_NODE_UNEXPECTABLE_TYPE = "Неожиданный тип значения в json файле: [%s]";

}
