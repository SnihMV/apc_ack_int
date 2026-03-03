package su.petrosoft.apk_ack_integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;
import su.petrosoft.apk_ack_integration.config.IntegrationProperties;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.request.GettingInstanceRepresentationRequestDto;
import su.petrosoft.apk_ack_integration.service.ApkPlicanteService;
import su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil;

import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties(IntegrationProperties.class)
public class ApkAckIntegrationApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(ApkAckIntegrationApplication.class, args);
        PlicanteRestClient bean = ctx.getBean(PlicanteRestClient.class);
        CashPlanLimitMapper mapper = ctx.getBean(CashPlanLimitMapper.class);
//        List<InstanceDto> dtoList = bean.getTableAttributesList(CashPlanLimitUtil.requestDtoToGettingCplEqualsFieldsByCurrentYear());
//        List<InstanceDto> dtoList = bean.getTableAttributesList(CashPlanLimitUtil.requestDtoToGettingExpenseFieldsById(107305));
        List<Attribute<?>> attributes = bean.getInstanceRepresentation(GettingInstanceRepresentationRequestDto.builder()
                .instance(InstanceDto.builder()
                        .id(107305L)
                        .build())
                .build());
        mapper.toEntity(attributes);
    }
}
