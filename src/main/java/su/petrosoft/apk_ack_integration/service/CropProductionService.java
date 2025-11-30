package su.petrosoft.apk_ack_integration.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.model.OperationalReport;
import su.petrosoft.apk_ack_integration.model.dto.request.FillingMainFormRequestDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class CropProductionService {

    private final ApkPlicanteService plicanteService;

    public void fillMainForm(FillingMainFormRequestDto dto) {

        List<OperationalReport> reports = plicanteService.getReportsForSowingCampaignFilling(dto);

    }
}
