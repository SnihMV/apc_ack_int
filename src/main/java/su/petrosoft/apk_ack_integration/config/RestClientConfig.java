package su.petrosoft.apk_ack_integration.config;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestClient;
import su.petrosoft.apk_ack_integration.client.NiFiRestClient;
import su.petrosoft.apk_ack_integration.client.ApkPlicanteRestClient;
import su.petrosoft.apk_ack_integration.client.SoapLoggingInterceptor;
import su.petrosoft.apk_ack_integration.client.TechPlicanteSoapClient;

@Slf4j
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
    public TechPlicanteSoapClient techPlicanteSoapClient(
            IntegrationProperties props,
            XmlMapper xmlMapper
    ) {
        log.info("Configuring SOAP client for URL: {}", props.technolog().soapUrl());

        MappingJackson2XmlHttpMessageConverter xmlConverter = new MappingJackson2XmlHttpMessageConverter(xmlMapper);
        return new TechPlicanteSoapClient(
                RestClient.builder()
                        .requestFactory(
                                new BufferingClientHttpRequestFactory(
                                        new SimpleClientHttpRequestFactory()))
                        .baseUrl(props.technolog().soapUrl())
                        .defaultHeader("Content-Type", MediaType.TEXT_XML_VALUE)
                        .messageConverters(converters -> converters.add(xmlConverter))
                        .defaultHeader("SOAPAction", "")
                        .requestInterceptor(new BasicAuthenticationInterceptor(
                                props.technolog().username(),
                                props.technolog().password()))
                        .requestInterceptor(new SoapLoggingInterceptor(xmlMapper))
                        .build()
        );
    }

}
