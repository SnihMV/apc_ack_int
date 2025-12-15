package su.petrosoft.apk_ack_integration.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OwnershipForm {
    GOS("гос", "244"),
    NEGOS("негос", "245"),
    IP("ИП", "246"),
    ALL("все", "default");

    private String name;
    private String kosgu;
}
