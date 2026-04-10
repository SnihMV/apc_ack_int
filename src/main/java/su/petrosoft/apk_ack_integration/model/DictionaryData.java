package su.petrosoft.apk_ack_integration.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class DictionaryData {
    private final String code;
    private String description;

    public DictionaryData(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DictionaryData that = (DictionaryData) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}
