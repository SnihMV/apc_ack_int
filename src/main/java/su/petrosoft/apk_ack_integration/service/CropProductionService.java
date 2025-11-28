package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.model.dto.request.FillingMainFormRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.GetAttributesListRequestDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class CropProductionService {
    private final ApkPlicanteService plicanteService;

    public void fillMainForm(FillingMainFormRequestDto dto) {

            plicanteService.getReportsForSowingCampaignFilling(dto);
    }
}
