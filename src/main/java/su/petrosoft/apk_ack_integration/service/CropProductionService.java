package su.petrosoft.apk_ack_integration.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.model.CropProductionMainForm;
import su.petrosoft.apk_ack_integration.model.OperationalReport;
import su.petrosoft.apk_ack_integration.model.dto.request.FillingMainFormRequestDto;
import su.petrosoft.apk_ack_integration.model.enums.ReportType;
import su.petrosoft.apk_ack_integration.util.CropProductionUtil;

import static su.petrosoft.apk_ack_integration.model.enums.ReportType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CropProductionService {

    private final ApkPlicanteService plicanteService;

    public void fillMainForm(FillingMainFormRequestDto dto) {
        CropProductionMainForm mainForm = CropProductionMainForm.builder()
                .id(dto.id())
                .version(dto.version())
                .date(dto.date())
                .build();

        List<OperationalReport> sowingCampaignReports = plicanteService.getOperationalReports(FORM_1, dto.date());
        if (sowingCampaignReports.isEmpty()) {
            log.info("Could not found Operational Reports for Sowing Campaign filling by date [{}]", dto.date());
        } else {
            log.info("Found [{}] Operational Reports for Sowing Campaign", sowingCampaignReports.size());
            CropProductionUtil.fillSowingCampaignFields(mainForm, sowingCampaignReports);
        }

        List<OperationalReport> fodderHarvestingReports = plicanteService.getOperationalReports(FORM_2, dto.date());
        if (fodderHarvestingReports.isEmpty()) {
            log.info("Could not found Operational Reports for Fodder Harvesting filling by date [{}]", dto.date());
        } else {
            log.info("Found [{}] Operational Reports for Fodder Harvesting", fodderHarvestingReports.size());
            CropProductionUtil.fillFodderHarvestingFields(mainForm, fodderHarvestingReports);
        }

        CropProductionMainForm cropProductionMainForm = plicanteService.updateCropProductionMainForm(mainForm);
        log.debug("Updated Main Form: [{}]", cropProductionMainForm);
    }
}
