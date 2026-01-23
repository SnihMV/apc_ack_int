package su.petrosoft.apk_ack_integration.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestClient;
import su.petrosoft.apk_ack_integration.client.NiFiRestClient;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;
import su.petrosoft.apk_ack_integration.client.RestLoggingInterceptor;
import su.petrosoft.apk_ack_integration.client.SoapLoggingInterceptor;
import su.petrosoft.apk_ack_integration.client.PlicanteSoapClient;

import java.util.List;

@Slf4j
@Configuration
public class RestClientsConfig {

    @Bean
    public NiFiRestClient nifiRestClient(IntegrationProperties props) {
        ObjectMapper customMapper = new ObjectMapper();
        customMapper.registerModule(new JavaTimeModule());
        customMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        customMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(customMapper);
        return new NiFiRestClient(
                props,
                RestClient.builder()
                        .baseUrl(props.niFi().baseUrl())
                        .messageConverters(List.of(
                                new StringHttpMessageConverter(),
                                converter
                        ))
                        .requestInterceptor(new BasicAuthenticationInterceptor(
                                props.niFi().username(),
                                props.niFi().password()))
                        .build()
        );
    }

    @Bean
    public PlicanteRestClient plicanteRestClient(IntegrationProperties props) {
        return new PlicanteRestClient(
                RestClient.builder()
                        .baseUrl(props.apk().baseUrl())
                        .requestInterceptor(new BasicAuthenticationInterceptor(
                                props.apk().username(),
                                props.apk().password()))
                        .requestInterceptor(new RestLoggingInterceptor())
                        .build()
        );
    }

    @Bean
    public PlicanteSoapClient plicanteSoapClient(
            IntegrationProperties props,
            XmlMapper xmlMapper
    ) {
        log.info("Configuring SOAP client for URL: {}", props.technolog().soapUrl());

        MappingJackson2XmlHttpMessageConverter xmlConverter = new MappingJackson2XmlHttpMessageConverter(xmlMapper);

        return new PlicanteSoapClient(
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
