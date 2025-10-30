package su.petrosoft.apk_ack_integration.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;
import su.petrosoft.apk_ack_integration.config.IntegrationProperties;
import su.petrosoft.apk_ack_integration.model.dto.response.AckGetUpsertMessageResponseDto;

@RequiredArgsConstructor
public class AckRestClient {
    private final IntegrationProperties props;
    private final RestClient restClient;

    public AckGetUpsertMessageResponseDto getUpsertMessage() {
        return restClient
                .get()
                .uri(props.ack().updating().path())
                .retrieve()
                .body(AckGetUpsertMessageResponseDto.class);
    }

}
