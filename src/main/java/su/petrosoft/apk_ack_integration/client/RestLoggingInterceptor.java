package su.petrosoft.apk_ack_integration.client;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class RestLoggingInterceptor implements ClientHttpRequestInterceptor {

    private final ObjectMapper objectMapper;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        if (!log.isDebugEnabled()) {
            return execution.execute(request, body);
        }

        String requestId = UUID.randomUUID().toString().substring(0, 8);
        logRequest(requestId, request, body);

        long start = System.currentTimeMillis();
        ClientHttpResponse response = execution.execute(request, body);
        long duration = System.currentTimeMillis() - start;

        logResponse(requestId, response, duration);

        return response;
    }

    private void logRequest(String requestId, HttpRequest request, byte[] body) {
        String json = body.length > 0 ? prettyPrint(new String(body, StandardCharsets.UTF_8)) : "{}";
        log.debug("[REST-{}] >>> {} {}", requestId, request.getMethod(), request.getURI());
        log.debug("[REST-{}] >>> Request body:\n{}", requestId, json);
    }

    private void logResponse(String requestId, ClientHttpResponse response, long duration) throws IOException {
        byte[] body = response.getBody().readAllBytes();
        String json = body.length > 0 ? prettyPrint(new String(body, StandardCharsets.UTF_8)) : "{}";
        log.debug("[REST-{}] <<< {} ({} ms)", requestId, response.getStatusCode(), duration);
        log.debug("[REST-{}] <<< Response body:\n{}", requestId, json);
    }

    private String prettyPrint(String stringBody) {
        try {
            Object obj = objectMapper.readValue(stringBody, Object.class);
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Could not read response body as JSON: [{}]", e.getMessage());
            return stringBody;
        }
    }
}
