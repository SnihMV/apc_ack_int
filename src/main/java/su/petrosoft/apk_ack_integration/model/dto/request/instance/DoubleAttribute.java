package su.petrosoft.apk_ack_integration.model.dto.request.instance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DoubleAttribute(
        Long id,
        String code,
        String type,
        List<Value> value,
        String name,
        String description,
        String attributeType
) implements Attribute {

    public DoubleAttribute(long id, BigDecimal data) {
        this(id, null, "DOUBLE", List.of(new Value(data, null)), null, null, null);
    }

    @JsonIgnore
    public BigDecimal getData() {
        Object data = Attribute.super.getData();
        if (data instanceof BigDecimal bd) return bd;
        if (data instanceof Double d) return BigDecimal.valueOf(d);
        if (data instanceof Number num) return BigDecimal.valueOf(num.doubleValue());
        return null;
    }

}
