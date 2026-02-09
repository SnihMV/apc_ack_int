package su.petrosoft.apk_ack_integration.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.model.CropProductionMainForm;
import su.petrosoft.apk_ack_integration.model.OperationalReport;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.request.FillingMainFormRequestDto;
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

        List<OperationalReport> sowingReports = plicanteService.getOperationalReports(FORM_1, dto.date());
        if (sowingReports.isEmpty()) {
            log.info("Could not found Operational Reports for Sowing filling by date [{}]", dto.date());
        } else {
            log.info("Found [{}] Operational Reports for Sowing", sowingReports.size());
            CropProductionUtil.fillSowingFields(mainForm, sowingReports);
        }

        List<OperationalReport> fodderReports = plicanteService.getOperationalReports(FORM_2, dto.date());
        if (fodderReports.isEmpty()) {
            log.info("Could not found Operational Reports for Fodder filling by date [{}]", dto.date());
        } else {
            log.info("Found [{}] Operational Reports for Fodder", fodderReports.size());
            CropProductionUtil.fillFodderFields(mainForm, fodderReports);
        }

        List<OperationalReport> harvestingReports = plicanteService.getOperationalReports(FORM_3, dto.date());
        if (harvestingReports.isEmpty()) {
            log.info("Could not found Operational Reports for Harvesting filling by date [{}]", dto.date());
        } else {
            log.info("Found [{}] Operational Reports for Harvesting", harvestingReports.size());
            CropProductionUtil.fillHarvestingFields(mainForm, harvestingReports);
        }

        UpdateInstanceResponseDto updated = plicanteService.updateCropProductionMainForm(mainForm);
        log.debug("Updated Main Form: [{}]", updated.id());
    }
}
