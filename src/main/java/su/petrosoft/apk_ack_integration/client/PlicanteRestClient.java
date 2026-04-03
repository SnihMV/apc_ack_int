package su.petrosoft.apk_ack_integration.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import su.petrosoft.apk_ack_integration.exception.StaleVersionException;
import su.petrosoft.apk_ack_integration.model.dto.plicante.ChangeGroupStatusRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.PlicanteErrorDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.request.GettingInstanceRepresentationRequestDto;

import java.io.IOException;
import java.util.List;

import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.INSTANCE_STALE_VERSION;

@Slf4j
@RequiredArgsConstructor
public class PlicanteRestClient {
    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    public List<InstanceDto> getTableAttributesList(GetAttributesListRequestDto dto) {
        return restClient
                .post()
                .uri("register-rest/operator/v2/table/attributes/list")
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

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

    public List<Attribute<?>> getInstanceRepresentation(GettingInstanceRepresentationRequestDto dto) {
        return restClient
                .post()
                .uri("register-rest/operator/v2/representation/instance/data-view")
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public ResponseEntity<Void> changeStatus(ChangeGroupStatusRequestDto dto) {
        return restClient
                .post()
                .uri("register-rest/operator/v2/status/change/group")
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    public UpdateInstanceResponseDto updateInstance(UpdateInstanceRequestDto dto) {
        try {
            return restClient
                    .post()
                    .uri("register-rest/operator/v2/instance/crud/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(dto)
                    .retrieve()
                    .body(UpdateInstanceResponseDto.class);
        } catch (HttpServerErrorException e) {
            try {
                PlicanteErrorDto errorDto = objectMapper.readValue(e.getResponseBodyAsByteArray(), PlicanteErrorDto.class);
                if ("STALE_VERSION".equals(errorDto.code())) {
                    throw new StaleVersionException(INSTANCE_STALE_VERSION.formatted(dto.instance().id()));
                }
                throw new RuntimeException(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (Exception e) {
            log.error("Could not update instance [{}]. Error message: [{}]", dto.instance().id(), e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
