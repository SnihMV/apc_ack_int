package su.petrosoft.apk_ack_integration.client;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
public class SoapLoggingInterceptor implements ClientHttpRequestInterceptor {

    private final XmlMapper xmlMapper;

    public SoapLoggingInterceptor(XmlMapper xmlMapper) {
        this.xmlMapper = xmlMapper;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        if (!log.isDebugEnabled()) {
            return execution.execute(request, body);
        }

        String requestId = UUID.randomUUID().toString().substring(0, 8);
        log.debug("[SOAP-{}] >>> {} {}", requestId, request.getMethod(), request.getURI());
        log.debug("[SOAP-{}] Request headers: {}", requestId, request.getHeaders());
        log.debug("[SOAP-{}] Request body:\n{}", requestId,
                new String(body, StandardCharsets.UTF_8));

        long start = System.currentTimeMillis();
        ClientHttpResponse response = execution.execute(request, body);
        long duration = System.currentTimeMillis() - start;

        String responseBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
        log.debug("[SOAP-{}] <<< {} ({} ms)", requestId, response.getStatusCode(), duration);
        log.debug("[SOAP-{}] Response headers: {}", requestId, response.getHeaders());
        log.debug("[SOAP-{}] Response body:\n{}", requestId, responseBody);

        return response;

    }
}
