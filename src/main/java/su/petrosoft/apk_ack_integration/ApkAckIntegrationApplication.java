package su.petrosoft.apk_ack_integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import su.petrosoft.apk_ack_integration.service.XmlDataProcessor;

@SpringBootApplication
public class ApkAckIntegrationApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(ApkAckIntegrationApplication.class, args);
        XmlDataProcessor processor = ctx.getBean(XmlDataProcessor.class);
        processor.doUpsert();
    }

}
