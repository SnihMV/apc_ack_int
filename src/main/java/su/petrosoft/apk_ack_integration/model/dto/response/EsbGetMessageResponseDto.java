package su.petrosoft.apk_ack_integration.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record EsbGetMessageResponseDto(
        @JsonProperty("Org_Taxcode") String orgTaxCode,

        @JsonProperty("Org_Code_FK") String orgCodeFk,

        @JsonProperty("Org_KPP") String orgKpp,

        @JsonProperty("Document_ID") Long documentId,

        @JsonProperty("Class") Long documentClass,

        @JsonProperty("Status") Long status,

        @JsonProperty("RecordTimeStamp")
        @JsonFormat(pattern = "yyyy-MM-dd_HH-mm-ss")
        LocalDateTime recordTimeStamp,

        @JsonProperty("Data") String data,

        @JsonProperty("Sign") String sign
) {}
