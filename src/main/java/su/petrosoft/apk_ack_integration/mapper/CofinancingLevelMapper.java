package su.petrosoft.apk_ack_integration.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.CofinancingLevel;
import su.petrosoft.apk_ack_integration.model.excel.CofinancingLevelExcelRow;

import java.math.BigDecimal;
import java.time.LocalDate;

import static su.petrosoft.apk_ack_integration.model.enums.FinancingForm.define;

@Slf4j
@Component
public class CofinancingLevelMapper {
    public CofinancingLevel toEntity(CofinancingLevelExcelRow row) {
        BigDecimal obCoeff = BigDecimal.valueOf(row.obCoeff());
        BigDecimal fbCoeff = BigDecimal.valueOf(row.fbCoeff());
        return CofinancingLevel.builder()
                .year((long) LocalDate.now().getYear())
                .startDate(LocalDate.of(LocalDate.now().getYear(), 1, 1))
                .obCoeff(obCoeff)
                .fbCoeff(fbCoeff)
                .financingForm(define(obCoeff, fbCoeff).getName())
                .build();
    }
}
