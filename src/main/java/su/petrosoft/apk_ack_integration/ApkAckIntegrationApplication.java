package su.petrosoft.apk_ack_integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import su.petrosoft.apk_ack_integration.config.IntegrationProperties;
import su.petrosoft.apk_ack_integration.model.data.excel.BudgetItemRow;
import su.petrosoft.apk_ack_integration.service.ScriptService;

@SpringBootApplication
@EnableConfigurationProperties(IntegrationProperties.class)
public class ApkAckIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApkAckIntegrationApplication.class, args);
    }
}
