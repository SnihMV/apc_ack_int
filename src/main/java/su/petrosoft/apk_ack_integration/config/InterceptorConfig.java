package su.petrosoft.apk_ack_integration.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import su.petrosoft.apk_ack_integration.client.RestLoggingInterceptor;
import su.petrosoft.apk_ack_integration.client.SoapLoggingInterceptor;

@Configuration
public class InterceptorConfig {

    @Bean
    public RestLoggingInterceptor restLoggingInterceptor(ObjectMapper objectMapper) {
        return new RestLoggingInterceptor(objectMapper);
    }

    @Bean
    public SoapLoggingInterceptor soapLoggingInterceptor(XmlMapper xmlMapper) {
        return new SoapLoggingInterceptor(xmlMapper);
    }
}
