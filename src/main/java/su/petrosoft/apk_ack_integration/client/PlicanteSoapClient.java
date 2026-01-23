package su.petrosoft.apk_ack_integration.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient;
import su.petrosoft.apk_ack_integration.model.dto.plicante.soap.DeleteInstanceSoapRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.soap.DeleteInstancesListSoapRequestDto;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class PlicanteSoapClient {
    private final RestClient restClient;

    public void deleteInstanceById(long instanceId) {
        log.info("Sending request to delete instance with ID: {}", instanceId);
        restClient.post()
                .body(new DeleteInstanceSoapRequestDto(instanceId))
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteInstancesList(List<Long> ids) {
        log.info("Sending request to delete instances with IDs: {}", ids);
        restClient.post()
                .body(new DeleteInstancesListSoapRequestDto(ids))
                .retrieve()
                .toBodilessEntity();
    }

}
