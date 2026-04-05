package su.petrosoft.apk_ack_integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.retry.annotation.EnableRetry;
import su.petrosoft.apk_ack_integration.config.IntegrationProperties;

@SpringBootApplication
@EnableConfigurationProperties(IntegrationProperties.class)
@EnableRetry
public class ApkAckIntegrationApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(ApkAckIntegrationApplication.class, args);
    }
}

