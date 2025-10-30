package su.petrosoft.apk_ack_integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestClient;
import su.petrosoft.apk_ack_integration.client.AckRestClient;
import su.petrosoft.apk_ack_integration.client.ApkPlicanteRestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public AckRestClient ackRestClient(IntegrationProperties props) {
        return new AckRestClient(props,
                RestClient.builder()
                        .baseUrl(props.ack().baseUrl())
                        .build()
        );
    }

    @Bean
    public ApkPlicanteRestClient apkPlicanteRestClient(IntegrationProperties props) {
        return new ApkPlicanteRestClient(props,
                RestClient.builder()
                        .baseUrl(props.apk().baseUrl())
                        .requestInterceptor(new BasicAuthenticationInterceptor(
                                props.apk().username(),
                                props.apk().password()))
                        .build()
        );
    }
}
