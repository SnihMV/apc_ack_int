package su.petrosoft.apk_ack_integration.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPKR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KCSR;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.TEMPLATE_ID;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class SubsidyProgram extends PlicanteInstance implements DictionaryDataRequester {
    private final Long level;
    private final Long kcsr;
    private final Long dopKr;
    private Long parentId;
    private String title;
    private Set<Long> financingSourceIds;
    private Set<Long> cofinLevelIds;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SubsidyProgram that = (SubsidyProgram) o;
        return Objects.equals(level, that.level)
                && Objects.equals(kcsr, that.kcsr)
                && Objects.equals(dopKr, that.dopKr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, kcsr, dopKr);
    }

    @Override
    public Map<Dictionary, Long> requestedDictionaryIds() {
        Map<Dictionary, Long> requested = new HashMap<>();
        requested.put(KCSR, getKcsr());
        requested.put(DOPKR, getDopKr());
        return requested;
    }

    @Override
    public long getTemplateId() {
        return TEMPLATE_ID;
    }
}

