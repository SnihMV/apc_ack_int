package su.petrosoft.apk_ack_integration.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;
import su.petrosoft.apk_ack_integration.config.IntegrationProperties;
import su.petrosoft.apk_ack_integration.model.dto.nifi.GetCompanyByInnResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.response.AckGetUpdateMessageResponseDto;

@RequiredArgsConstructor
public class NiFiRestClient {
    private final IntegrationProperties props;
    private final RestClient restClient;

    public AckGetUpdateMessageResponseDto getUpdateMessage() {
        return restClient
                .get()
                .uri(props.ack().updating().path())
                .retrieve()
                .body(AckGetUpdateMessageResponseDto.class);
    }

    public GetCompanyByInnResponseDto getCompanyByInn(String inn) {
        return restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("legal_entity_info")
                        .queryParam("inn", inn)
                        .build())
                .retrieve()
                .body(GetCompanyByInnResponseDto.class);
    }
}
