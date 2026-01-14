package su.petrosoft.apk_ack_integration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "integration")
public record IntegrationProperties(
        Apk apk,
        NiFi niFi,
        Technolog technolog
) {

    public record Technolog(
            String soapUrl,
            String username,
            String password
    ){}

    public record Apk(
            String baseUrl,
            String username,
            String password
    ){}
    public record NiFi(
            String baseUrl,
            String username,
            String password,
            CreateLimits createLimits,
            UpsertLimits upsertLimits
    ){
        public record CreateLimits(
                String path
        ){}
        public record UpsertLimits(
                String path
        ){}
    }
}
