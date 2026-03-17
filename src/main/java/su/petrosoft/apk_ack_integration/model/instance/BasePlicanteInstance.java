package su.petrosoft.apk_ack_integration.model.instance;

import su.petrosoft.apk_ack_integration.exception.PlicanteInstanceException;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.MISMATCH_IDENT_VALUES;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.MISSING_IDENT_KEY;

public abstract class BasePlicanteInstance<I extends Enum<I> & IdentKey, V extends Enum<V> & ValueKey> {

    protected Long id;
    protected Long version;
    private final Map<I, Object> identData;
    protected final Map<V, Object> valueData;

    public BasePlicanteInstance() {
        this.identData = Collections.emptyMap();
        this.valueData = new EnumMap<>(getValueKeyClass());
    }

    public BasePlicanteInstance(Map<I, Object> identData) {
        Map<I, Object> tmp = new EnumMap<>(getIdentKeyClass());
        if (identData == null || identData.isEmpty()) {
            this.identData = Collections.emptyMap();
        } else {
            for (I key : getIdentKeyClass().getEnumConstants()) {
                Object value = identData.get(key);
                if (value == null) {
                    throw new PlicanteInstanceException(MISSING_IDENT_KEY.formatted(key.title()));
                }
                tmp.put(key, value);
            }
            this.identData = Collections.unmodifiableMap(tmp);
        }
        this.valueData = new EnumMap<>(getValueKeyClass());
    }


    public <T> void set(V key, T value) {
        if (value == null || key.isNull(value)) {
            valueData.remove(key);
        } else {
            valueData.put(key, value);
        }
    }

    public List<? extends Attribute<?>> attributesToCreate() {
        return Stream.concat(identData.entrySet().stream(), valueData.entrySet().stream())
                .map(e -> e.getKey().createAttribute(e.getValue()))
                .toList();
    }

    public List<? extends Attribute<?>> attributesToUpdate(BasePlicanteInstance<I, V> updatedInstance) {
        return updatedInstance.valueData.entrySet().stream()
                .filter(entry -> !Objects.equals(entry.getKey(), valueData.get(entry.getKey())))
                .map(entry -> entry.getKey().createAttribute(entry.getValue()))
                .toList();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BasePlicanteInstance<?, ?> that = (BasePlicanteInstance<?, ?>) o;
        return Objects.equals(identData, that.identData);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(identData);
    }

    protected abstract Class<I> getIdentKeyClass();

    protected abstract Class<V> getValueKeyClass();
}
