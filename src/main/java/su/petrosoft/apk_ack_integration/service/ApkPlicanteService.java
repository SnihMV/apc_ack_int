package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.ApkPlicanteRestClient;
import su.petrosoft.apk_ack_integration.model.CodeType;
import su.petrosoft.apk_ack_integration.model.dto.request.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.response.GetAttributesListResponseDto;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApkPlicanteService {
    private final ApkPlicanteRestClient apkClient;

    public Map<CodeType, Map<Long, String>> getAllCodes() {
        log.debug("Receiving all existed codes");
        Map<CodeType, Map<Long, String>> codes = new EnumMap<>(CodeType.class);
        for (CodeType codeType : CodeType.values()) {
            codes.put(codeType, getCodesByType(codeType));
            log.debug("{} code map", codeType.name());
            log.debug(codes.get(codeType).toString());
        }
        log.debug("Found {} codes overall", codes.size());
        return codes;
    }

    private Map<Long, String> getCodesByType(CodeType codeType) {
        log.debug("Receiving codes for type {}", codeType.name());
        List<GetAttributesListResponseDto> list = apkClient.getTableAttributesList(
                new GetAttributesListRequestDto(codeType.getTemplateId(), null));
        return list.stream()
                .filter(dto -> dto.shortForm() != null)
                .collect(Collectors.toMap(
                        GetAttributesListResponseDto::id,
                        GetAttributesListResponseDto::shortForm
                ));
    }
}
