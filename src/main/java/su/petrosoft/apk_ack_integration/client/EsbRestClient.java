package su.petrosoft.apk_ack_integration.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;
import su.petrosoft.apk_ack_integration.model.dto.response.EsbGetMessageResponseDto;

@RequiredArgsConstructor
public class EsbRestClient {
    private final RestClient restClient;

    public EsbGetMessageResponseDto getMessage(String uri) {
        return restClient
                .get()
                .uri(uri)
                .retrieve()
                .body(EsbGetMessageResponseDto.class);
    }

}
