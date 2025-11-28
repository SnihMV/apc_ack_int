package su.petrosoft.apk_ack_integration.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import su.petrosoft.apk_ack_integration.config.IntegrationProperties;
import su.petrosoft.apk_ack_integration.model.dto.request.ChangeInstanceStatusRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.response.GetAttributesListResponseDto;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ApkPlicanteRestClient {
    private final IntegrationProperties props;
    private final RestClient restClient;

    public InstanceDto createInstance(CreateInstanceRequestDto dto) {
        try {
            return restClient
                    .post()
                    .uri("register-rest/operator/v2/instance/crud/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(dto)
                    .retrieve()
                    .body(InstanceDto.class);
        } catch (Exception e) {
            log.error("Could not create instance [{}]. Error message: [{}]", dto.instance().id(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<InstanceDto> getTableAttributesList(GetAttributesListRequestDto dto) {
        return restClient
                .post()
                .uri("register-rest/operator/v2/table/attributes/list")
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public List<InstanceDto> getExistedInstances(InstanceDto dto) {
        return restClient
            .post()
            .uri("register-rest/operator/v2/table/attributes/list")
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto)
            .retrieve()
            .body(new ParameterizedTypeReference<>() {});
    }

    public ResponseEntity<Void> changeStatus(ChangeInstanceStatusRequestDto dto) {
        return restClient
                .post()
                .uri("register-rest/operator/v2/status/change/group")
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    public InstanceDto updateInstance(UpdateInstanceRequestDto dto) {
        log.debug("Updating instance [{}]", dto);
        try {
            return restClient
                    .post()
                    .uri("register-rest/operator/v2/instance/crud/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(dto)
                    .retrieve()
                    .body(InstanceDto.class);
        } catch (Exception e) {
            log.error("Could not update instance [{}]. Error message: [{}]", dto.instance().id(), e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
