package su.petrosoft.apk_ack_integration.model.instance;

import su.petrosoft.apk_ack_integration.exception.PlicanteInstanceException;
import su.petrosoft.apk_ack_integration.exception.UpdateInstanceException;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.AttributeDto;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.IDENT_MODIFICATION;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.MISMATCH_INSTANCES;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.MISSING_IDENT_KEY;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.MISSING_IDENT_VALUE;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.UPDATER_IS_NULL;

public abstract class BasePlicanteInstance<A extends Enum<A> & InstanceAttributeEnum> {

    protected Long id;
    protected Long version;
    private final Map<A, Object> identData;
    protected final Map<A, Object> simpleData;

    public BasePlicanteInstance() {
        this.identData = Collections.emptyMap();
        this.simpleData = new EnumMap<>(getAttributeInfoClass());
    }

    public BasePlicanteInstance(Map<A, Object> values) {
        Map<A, Object> tempIdents = new EnumMap<>(getAttributeInfoClass());
        this.simpleData = new EnumMap<>(getAttributeInfoClass());

        for (Map.Entry<A, Object> entry : values.entrySet()) {
            A key = entry.getKey();
            Object value = entry.getValue();
            if (key.isIdentifying()) {
                if (value == null) {
                    throw new PlicanteInstanceException(MISSING_IDENT_VALUE.formatted(key.name()));
                }
                tempIdents.put(key, value);
            } else {
                if (value != null && !key.isEmpty(value)) {
                    simpleData.put(key, value);
                }
            }
        }
        for (A key : getAttributeInfoClass().getEnumConstants()) {
            if (key.isIdentifying() && !tempIdents.containsKey(key)) {
                throw new PlicanteInstanceException(MISSING_IDENT_KEY.formatted(key.name()));
            }
        }
        this.identData = Collections.unmodifiableMap(tempIdents);
    }

    public <T> void set(A key, T value) {
        if (key.isIdentifying()) {
            throw new PlicanteInstanceException(IDENT_MODIFICATION.formatted(key));
        }
        if (value == null || key.isEmpty(value)) {
            simpleData.remove(key);
        } else {
            simpleData.put(key, value);
        }
    }

    public List<? extends AttributeDto<?>> attributesToCreate() {
        return Stream.concat(identData.entrySet().stream(), simpleData.entrySet().stream())
                .filter(e -> !e.getKey().isEmpty(e.getValue()))
                .map(e -> e.getKey().createAttributeDto(e.getValue()))
                .toList();
    }

    public List<? extends AttributeDto<?>> attributesToUpdate(Map<A, Object> oldData, Map<A, Object> newData) {
        return newData.entrySet().stream()
                .filter(e -> !Objects.equals(oldData.get(e.getKey()), e.getValue()))
                .map(e -> e.getKey().createAttributeDto(e.getValue()))
                .toList();
    }

    public List<? extends AttributeDto<?>> updateAndGetRenewalAttributes(BasePlicanteInstance<A> renewal) {
        checkRenewalInstance(renewal);
        Map<A, Object> updatedData = updateData(renewal.simpleData);
        return attributesToUpdate(simpleData, updatedData);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BasePlicanteInstance<?> that = (BasePlicanteInstance<?>) o;
        return Objects.equals(identData, that.identData);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(identData);
    }

    private void checkRenewalInstance(BasePlicanteInstance<A> updater) {
        if (updater == null) {
            throw new UpdateInstanceException(UPDATER_IS_NULL);
        }
        if (!equals(updater)) {
            throw new UpdateInstanceException(MISMATCH_INSTANCES);
        }
    }

    @Override
    public String toString() {
        return "Plicante Instance {" +
                "id=" + id +
                ", version=" + version +
                ", identData=" + identData +
                ", simpleData=" + simpleData +
                '}';
    }

    protected abstract Class<A> getAttributeInfoClass();

    protected abstract Map<A, Object> updateData(Map<A, Object> updatingData);

}
