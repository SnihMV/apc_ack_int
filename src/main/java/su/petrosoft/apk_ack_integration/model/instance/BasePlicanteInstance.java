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

import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.MISMATCH_INSTANCES;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.MISSING_IDENT_KEY;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.MISSING_IDENT_VALUE;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.UNKNOWN_ATTRIBUTE;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.UPDATER_IS_NULL;

public abstract class BasePlicanteInstance<IA extends Enum<IA> & InstanceAttributeInfo, OA extends Enum<OA> & InstanceAttributeInfo> {

    protected Long id;
    protected Long version;
    private final Map<IA, Object> identData;
    protected final Map<OA, Object> optionData;

    public BasePlicanteInstance() {
        this.identData = Collections.emptyMap();
        this.optionData = new EnumMap<>(getOptionalAttributeInfoClass());
    }

    public BasePlicanteInstance(Map<? extends InstanceAttributeInfo, Object> values) {
        Map<IA, Object> tempIdents = new EnumMap<>(getIdentifyAttributeInfoClass());
        this.optionData = new EnumMap<>(getOptionalAttributeInfoClass());

        for (Map.Entry<? extends InstanceAttributeInfo, Object> entry : values.entrySet()) {
            InstanceAttributeInfo attr = entry.getKey();
            Object value = entry.getValue();
            Class<IA> identAttrClass = getIdentifyAttributeInfoClass();
            Class<OA> simpleAttrClass = getOptionalAttributeInfoClass();
            if (identAttrClass.isInstance(attr)) {
                IA identAttr = identAttrClass.cast(attr);
                if (value == null) {
                    throw new PlicanteInstanceException(MISSING_IDENT_VALUE.formatted(identAttr.name()));
                }
                tempIdents.put(identAttr, value);
            } else if (simpleAttrClass.isInstance(attr)) {
                OA optionAttr = simpleAttrClass.cast(attr);
                if (!optionAttr.isEmpty(value)) {
                    optionData.put(optionAttr, value);
                }
            } else throw new PlicanteInstanceException(UNKNOWN_ATTRIBUTE.formatted(attr.getId()));
        }
        for (IA key : getIdentifyAttributeInfoClass().getEnumConstants()) {
            if (!tempIdents.containsKey(key)) {
                throw new PlicanteInstanceException(MISSING_IDENT_KEY.formatted(key.name()));
            }
        }
        this.identData = Collections.unmodifiableMap(tempIdents);
    }

    public <T> void set(OA key, T value) {
        if (value == null || key.isEmpty(value)) {
            optionData.remove(key);
        } else {
            optionData.put(key, value);
        }
    }

    public List<? extends AttributeDto<?>> attributesToCreate() {
        return Stream.concat(identData.entrySet().stream(), optionData.entrySet().stream())
                .filter(e -> !e.getKey().isEmpty(e.getValue()))
                .map(e -> e.getKey().createAttributeDto(e.getValue()))
                .toList();
    }

    public List<? extends AttributeDto<?>> attributesToUpdate(Map<OA, Object> oldData, Map<OA, Object> newData) {
        return newData.entrySet().stream()
                .filter(e -> !Objects.equals(oldData.get(e.getKey()), e.getValue()))
                .map(e -> e.getKey().createAttributeDto(e.getValue()))
                .toList();
    }

    public List<? extends AttributeDto<?>> updateAndGetRenewalAttributes(BasePlicanteInstance<IA, OA> renewal) {
        checkRenewalInstance(renewal);
        Map<OA, Object> updatedData = updateData(renewal.optionData);
        return attributesToUpdate(optionData, updatedData);
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

    @Override
    public String toString() {
        return "Plicante Instance {" +
                "id=" + id +
                ", version=" + version +
                ", identData=" + identData +
                ", simpleData=" + optionData +
                '}';
    }

    private void checkRenewalInstance(BasePlicanteInstance<IA, OA> updater) {
        if (updater == null) {
            throw new UpdateInstanceException(UPDATER_IS_NULL);
        }
        if (!equals(updater)) {
            throw new UpdateInstanceException(MISMATCH_INSTANCES);
        }
    }

    protected abstract Class<IA> getIdentifyAttributeInfoClass();

    protected abstract Class<OA> getOptionalAttributeInfoClass();

    protected abstract Map<OA, Object> updateData(Map<OA, Object> updatingData);

}
