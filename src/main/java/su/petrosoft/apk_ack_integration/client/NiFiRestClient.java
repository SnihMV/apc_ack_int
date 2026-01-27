package su.petrosoft.apk_ack_integration.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient;
import su.petrosoft.apk_ack_integration.config.IntegrationProperties;
import su.petrosoft.apk_ack_integration.model.dto.nifi.GetDataFromEgrulByInnDto;
import su.petrosoft.apk_ack_integration.model.dto.response.AckGetUpdateMessageResponseDto;

@Slf4j
@RequiredArgsConstructor
public class NiFiRestClient {
    private final IntegrationProperties props;
    private final RestClient restClient;

    public AckGetUpdateMessageResponseDto getUpdateMessage() {
        return restClient
                .get()
                .uri(props.niFi().upsertLimits().path())
                .retrieve()
                .body(AckGetUpdateMessageResponseDto.class);
    }

    public GetDataFromEgrulByInnDto getCompanyByInn(String inn) {
        try {
            return restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("legal_entity_info")
                            .queryParam("inn", inn)
                            .build())
                    .retrieve()
                    .body(GetDataFromEgrulByInnDto.class);
        } catch (Exception e) {
            log.warn("Exception while getting company data by INN: [{}]. Error message: [{}]", inn, e.getMessage());
            if (e.getCause() != null) {
                log.warn("Reason: [{}]", e.getCause().getMessage());
            }
            return GetDataFromEgrulByInnDto.builder().build();
        }

    }
}
