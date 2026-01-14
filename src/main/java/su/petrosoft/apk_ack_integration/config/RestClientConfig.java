package su.petrosoft.apk_ack_integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestClient;
import su.petrosoft.apk_ack_integration.client.NiFiRestClient;
import su.petrosoft.apk_ack_integration.client.ApkPlicanteRestClient;
import su.petrosoft.apk_ack_integration.client.TechPlicanteSoapClient;

@Configuration
public class RestClientConfig {

    @Bean
    public NiFiRestClient ackRestClient(IntegrationProperties props) {
        return new NiFiRestClient(props,
                RestClient.builder()
                        .baseUrl(props.niFi().baseUrl())
                        .build()
        );
    }

    @Bean
    public ApkPlicanteRestClient apkPlicanteRestClient(IntegrationProperties props) {
        return new ApkPlicanteRestClient(
                RestClient.builder()
                        .baseUrl(props.apk().baseUrl())
                        .requestInterceptor(new BasicAuthenticationInterceptor(
                                props.apk().username(),
                                props.apk().password()))
                        .build()
        );
    }

    @Bean
    public TechPlicanteSoapClient techPlicanteSoapClient(IntegrationProperties props) {
        return new TechPlicanteSoapClient(
                RestClient.builder()
                        .baseUrl(props.technolog().soapUrl())
                        .defaultHeader("Content-Type", MediaType.TEXT_XML_VALUE)
                        .defaultHeader("SOAPAction", "")
//                        .defaultHeader("Accept-Encoding", "gzip, deflate")
                        .requestInterceptor(new BasicAuthenticationInterceptor(
                                props.technolog().username(),
                                props.technolog().password()))
                        .build()
        );
    }

}
