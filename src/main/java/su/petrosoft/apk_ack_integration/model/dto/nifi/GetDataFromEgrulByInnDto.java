package su.petrosoft.apk_ack_integration.model.dto.nifi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record GetDataFromEgrulByInnDto(
        @JsonProperty("ogrn")
        String ogrn,

        @JsonProperty("ogrn_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate ogrnDate,

        @JsonProperty("inn")
        String inn,

        @JsonProperty("kpp")
        String kpp,

        @JsonProperty("full_title")
        String fullTitle,

        @JsonProperty("short_title")
        String shortTitle,

        @JsonProperty("okved")
        List<Okved> okved
) {
    public record Okved(
            @JsonProperty("code")
            String code,

            @JsonProperty("name")
            String name,

            @JsonProperty("main")
            boolean main
    ) {}
}
