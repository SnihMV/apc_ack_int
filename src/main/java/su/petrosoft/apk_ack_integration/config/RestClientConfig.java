package su.petrosoft.apk_ack_integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestClient;
import su.petrosoft.apk_ack_integration.client.EsbRestClient;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public EsbRestClient esbRestClient() {
        return new EsbRestClient(
                RestClient.builder()
                        .baseUrl("http://192.168.0.177:9094")
                        .build()
        );
    }

    @Bean
    public PlicanteRestClient plicanteRestClient() {
        return new PlicanteRestClient(
                RestClient.builder()
                        .baseUrl("http://plicante-apk-test-vue.plicante.ru/")
                        .requestInterceptor(new BasicAuthenticationInterceptor("operator", "123"))
                        .build()
        );
    }
}
