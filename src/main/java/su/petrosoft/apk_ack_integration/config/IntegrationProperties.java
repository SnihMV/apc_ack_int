package su.petrosoft.apk_ack_integration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "integration")
public record IntegrationProperties(
        Apk apk,
        Ack ack
) {
    public record Apk(
            String baseUrl,
            String username,
            String password
    ){}
    public record Ack(
            String baseUrl,
            String username,
            String password,
            Creating creating,
            Updating updating
    ){
        public record Creating(
                String path
        ){}
        public record Updating(
                String path
        ){}
    }
}
