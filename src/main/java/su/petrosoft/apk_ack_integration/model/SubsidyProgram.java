package su.petrosoft.apk_ack_integration.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPKR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KCSR;

@Data
@SuperBuilder(toBuilder = true)
public class SubsidyProgram extends PlicanteInstance implements DictionaryDataRequester {
    public static final int MAX_LEVEL = 2;

    private final Long level;
    private final Long kcsr;
    private final Long dopKr;
    private Long parentId;
    private String title;
    private Collection<Long> financingSourceIds;
    private Collection<Long> cofinLevelIds;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SubsidyProgram that = (SubsidyProgram) o;
        return Objects.equals(kcsr, that.kcsr) && Objects.equals(dopKr, that.dopKr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kcsr, dopKr);
    }

    @Override
    public Map<Dictionary, Long> requestedDictionaryIds() {
        return Map.of(
                KCSR, getKcsr(),
                DOPKR, getDopKr()
        );
    }
}

