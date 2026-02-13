package su.petrosoft.apk_ack_integration.service;

import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.model.CropProductionMainForm;
import su.petrosoft.apk_ack_integration.model.OperationalReport;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.request.FillingMainFormRequestDto;
import su.petrosoft.apk_ack_integration.util.CropProductionUtil;
import su.petrosoft.apk_ack_integration.util.OperationalReportUtil;

import static su.petrosoft.apk_ack_integration.model.enums.ReportType.*;
import static su.petrosoft.apk_ack_integration.util.OperationalReportUtil.*;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.*;

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

        List<OperationalReport> sowingReports = plicanteService.getOperationalReports(
                requestDtoForGettingOperationalReportsByTypeAndDate(FORM_1, dto.date()));
        if (sowingReports.isEmpty()) {
            log.info("Could not found Operational Reports for Sowing filling by date [{}]", dto.date());
        } else {
            log.info("Found [{}] Operational Reports for Sowing", sowingReports.size());
            CropProductionUtil.fillSowingFields(mainForm, sowingReports);
        }

        List<OperationalReport> fodderReports = plicanteService.getOperationalReports(
                requestDtoForGettingOperationalReportsByTypeAndDate(FORM_2, dto.date()));
        if (fodderReports.isEmpty()) {
            log.info("Could not found Operational Reports for Fodder filling by date [{}]", dto.date());
        } else {
            log.info("Found [{}] Operational Reports for Fodder", fodderReports.size());
            CropProductionUtil.fillFodderFields(mainForm, fodderReports);
        }

        List<OperationalReport> harvestingReports = plicanteService.getOperationalReports(
                requestDtoForGettingOperationalReportsByTypeAndDate(FORM_3, dto.date()));
        if (harvestingReports.isEmpty()) {
            log.info("Could not found Operational Reports for Harvesting filling by date [{}]", dto.date());
        } else {
            log.info("Found [{}] Operational Reports for Harvesting", harvestingReports.size());
            CropProductionUtil.fillHarvestingFields(mainForm, harvestingReports);
        }

        UpdateInstanceResponseDto updated = plicanteService.updateCropProductionMainForm(mainForm);
        log.debug("Updated Main Form: [{}]", updated.id());
    }

    public byte[] createExcelSummaryReport(LocalDate from, LocalDate to) {
        if (from == null) {
            from = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        }
        if (to == null) {
            to = LocalDate.now();
        }
        long since = toEpochMilli(from);
        long until = toEpochMilli(to);
        plicanteService.getOperationalReports(
                requestDtoForGettingOperationalReportsByTypeAndDateInterval(FORM_1, since, until));
        return null;
    }
}
