package su.petrosoft.apk_ack_integration.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class SoapLoggingInterceptor implements ClientHttpRequestInterceptor {

    private final XmlMapper xmlMapper;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        if (!log.isDebugEnabled()) {
            return execution.execute(request, body);
        }

        String rawRequest = new String(body, StandardCharsets.UTF_8);
        String formattedRequest = formatXml(rawRequest);

        String requestId = UUID.randomUUID().toString().substring(0, 8);
        log.debug("[SOAP-{}] >>> {} {}", requestId, request.getMethod(), request.getURI());
        log.debug("[SOAP-{}] >>> Request body:\n{}", requestId, formattedRequest);

        long start = System.currentTimeMillis();
        ClientHttpResponse response = execution.execute(request, body);
        long duration = System.currentTimeMillis() - start;

        String rawResponse = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
        String formattedResponse = formatXml(rawResponse);

        log.debug("[SOAP-{}] <<< {} ({} ms)", requestId, response.getStatusCode(), duration);
        log.debug("[SOAP-{}] <<< Response body:\n{}", requestId, formattedResponse);

        return response;
    }
    private String formatXml(String xml) {
        try {
            JsonNode node = xmlMapper.readTree(xml);
            return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (Exception e) {
            log.trace("Could not format XML: {}", e.getMessage());
            return xml;
        }
    }
}
