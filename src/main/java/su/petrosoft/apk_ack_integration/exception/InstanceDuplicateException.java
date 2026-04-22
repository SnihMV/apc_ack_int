package su.petrosoft.apk_ack_integration.exception;

import lombok.Getter;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

@Getter
public class InstanceDuplicateException extends RuntimeException {

    private final Map<Long, List<List<Long>>> duplicates;

    public InstanceDuplicateException(Map<Long, List<List<Long>>> duplicates) {
        super(buildMessage(duplicates));
        this.duplicates = duplicates;
    }

    private static String buildMessage(Map<Long, List<List<Long>>> duplicates) {
        return duplicates.entrySet().stream()
                .map(entry -> String.format(
                        "  Среди объектов шаблона %d:\n%s",
                        entry.getKey(),
                        entry.getValue().stream()
                                .map(ids -> "    - " + ids)
                                .collect(joining("\n"))
                ))
                .collect(joining("\n", "Найдены дубликаты:\n", ""));
    }
}
