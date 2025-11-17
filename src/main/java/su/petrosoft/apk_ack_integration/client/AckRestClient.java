package su.petrosoft.apk_ack_integration.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;
import su.petrosoft.apk_ack_integration.config.IntegrationProperties;
import su.petrosoft.apk_ack_integration.model.dto.response.AckGetUpdateMessageResponseDto;

@RequiredArgsConstructor
public class AckRestClient {
    private final IntegrationProperties props;
    private final RestClient restClient;

    public AckGetUpdateMessageResponseDto getUpdateMessage() {
        return restClient
                .get()
                .uri(props.ack().updating().path())
                .retrieve()
                .body(AckGetUpdateMessageResponseDto.class);
    }

}
