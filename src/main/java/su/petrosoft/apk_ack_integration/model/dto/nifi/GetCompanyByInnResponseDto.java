package su.petrosoft.apk_ack_integration.model.dto.nifi;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record GetCompanyByInnResponseDto(
        String ogrn,
        LocalDate ogrnDate,
        String kpp,
        String fullTitle,
        String shortTitle
) {
}
