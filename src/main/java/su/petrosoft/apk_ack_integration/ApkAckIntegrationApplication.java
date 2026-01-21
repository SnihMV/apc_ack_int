package su.petrosoft.apk_ack_integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import su.petrosoft.apk_ack_integration.config.IntegrationProperties;
import su.petrosoft.apk_ack_integration.service.ScriptService;

@SpringBootApplication
@EnableConfigurationProperties(IntegrationProperties.class)
public class ApkAckIntegrationApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(ApkAckIntegrationApplication.class, args);
        ScriptService bean = ctx.getBean(ScriptService.class);
//        bean.refreshMunicipalitiesData();
    }
}
