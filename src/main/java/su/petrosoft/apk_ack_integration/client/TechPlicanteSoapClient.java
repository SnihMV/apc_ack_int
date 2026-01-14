package su.petrosoft.apk_ack_integration.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TechPlicanteSoapClient {
    private final RestClient restClient;

    private static final String DELETE_INSTANCE_BY_ID_XML = """
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"\s
            xmlns:reg="http://ru.petrosoft/register">
                       <soapenv:Header/>
                       <soapenv:Body>
                          <reg:deleteInstance>
                             <instanceId>%d</instanceId>
                          </reg:deleteInstance>
                       </soapenv:Body>
                    </soapenv:Envelope>
            """;

    public void deleteInstanceById(long instanceId) {
        log.info("Sending request to delete instance with ID: {}", instanceId);
        restClient.post()
                .body(DELETE_INSTANCE_BY_ID_XML.formatted(instanceId))
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteInstances(List<Long> instanceIds) {

    }
}
