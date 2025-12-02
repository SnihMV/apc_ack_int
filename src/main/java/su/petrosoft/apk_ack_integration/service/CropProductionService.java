package su.petrosoft.apk_ack_integration.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.model.CropProductionMainForm;
import su.petrosoft.apk_ack_integration.model.OperationalReport;
import su.petrosoft.apk_ack_integration.model.dto.request.FillingMainFormRequestDto;
import su.petrosoft.apk_ack_integration.util.CropProductionUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class CropProductionService {

    private final ApkPlicanteService plicanteService;

    public void fillMainForm(FillingMainFormRequestDto dto) {

        List<OperationalReport> reports = plicanteService.getReportsForSowingCampaignFilling(dto);
        if (reports.isEmpty()) {
            log.info("Could not found Operational Reports for Sowing Campaign filling by date [{}]", dto.date());
            return;
        }
        CropProductionMainForm mainForm = CropProductionMainForm.builder()
                .id(dto.id())
                .version(dto.version())
                .date(dto.date())
                .build();
        CropProductionUtil.fillSowingCampaignFields(mainForm, reports);
        log.debug("Updated Main Form after Sowing Campaign filling: [{}]", mainForm);

        CropProductionMainForm cropProductionMainForm = plicanteService.updateCropProductionMainForm(mainForm);

    }
}
