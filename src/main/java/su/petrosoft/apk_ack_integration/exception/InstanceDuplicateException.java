package su.petrosoft.apk_ack_integration.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class InstanceDuplicateException extends RuntimeException {

    public static final String NEW_LINE_BEGINNING = "\n   - ";

    private final List<List<Long>> duplicateIdsList;

    public InstanceDuplicateException(long templateId, List<List<Long>> duplicateIdsList) {
        super(buildMessage(templateId, duplicateIdsList));
        this.duplicateIdsList = duplicateIdsList;
    }

    private static String buildMessage(long templateId, List<List<Long>> duplicateIdsList) {
        StringBuilder sb = new StringBuilder("Среди объектов шаблона [%d] найдены дубликаты:".formatted(templateId));

        for (List<Long> duplicateIds : duplicateIdsList) {
            sb.append(NEW_LINE_BEGINNING).append(duplicateIds);
        }
        return sb.toString();
    }
}
