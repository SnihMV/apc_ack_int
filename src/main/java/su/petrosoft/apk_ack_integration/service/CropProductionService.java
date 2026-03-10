package su.petrosoft.apk_ack_integration.service;

import static java.util.Collections.emptyList;
import static java.util.Comparator.comparingLong;
import static java.util.function.BinaryOperator.maxBy;
import static java.util.stream.Collectors.toMap;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DISTRICT;
import static su.petrosoft.apk_ack_integration.model.enums.ReportType.FORM_1;
import static su.petrosoft.apk_ack_integration.model.enums.ReportType.FORM_2;
import static su.petrosoft.apk_ack_integration.model.enums.ReportType.FORM_3;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.INSTANCE_NOT_FOUND_BY_ID;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.REPORTS_NOT_FOUND;
import static su.petrosoft.apk_ack_integration.util.OperationalReportUtil.requestDtoForGettingOperationalReportsByTypeAndDate;
import static su.petrosoft.apk_ack_integration.util.OperationalReportUtil.requestDtoForGettingOperationalReportsByTypeAndStatusAndDateInterval;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.toEpochMilli;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.TEMPLATE_TITLE;
import static su.petrosoft.apk_ack_integration.util.SubsidyRecipientUtil.requestDtoToFindRecipientNameAndDistrictById;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;
import su.petrosoft.apk_ack_integration.exception.EntityNotFoundException;
import su.petrosoft.apk_ack_integration.exception.NoDataFoundException;
import su.petrosoft.apk_ack_integration.mapper.SubsidyRecipientMapper;
import su.petrosoft.apk_ack_integration.model.CropProductionMainForm;
import su.petrosoft.apk_ack_integration.model.DistrictData;
import su.petrosoft.apk_ack_integration.model.OperationalReport;
import su.petrosoft.apk_ack_integration.model.ProducerData;
import su.petrosoft.apk_ack_integration.model.SubsidyRecipient;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.request.FillingMainFormRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.request.GetCropProductionSummaryReportDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;
import su.petrosoft.apk_ack_integration.service.excel.ExcelReportFiller;
import su.petrosoft.apk_ack_integration.util.CropProductionUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class CropProductionService {

    private final ApkPlicanteService plicanteService;
    private final PlicanteRestClient plicanteRestClient;
    private final SubsidyRecipientMapper srMapper;
    private final ExcelReportFiller excelReportFiller;

    public void fillMainForm(FillingMainFormRequestDto dto) {
        CropProductionMainForm mainForm = CropProductionMainForm.builder()
                .id(dto.id())
                .version(dto.version())
                .date(dto.date())
                .build();

        List<OperationalReport> sowingReports = plicanteService.getOperationalReports(
                requestDtoForGettingOperationalReportsByTypeAndDate(FORM_1, dto.date()));
        if (sowingReports.isEmpty()) {
            log.info("Could not found Operational Reports for Sowing filling by date [{}]",
                    dto.date());
        } else {
            log.info("Found [{}] Operational Reports for Sowing", sowingReports.size());
            CropProductionUtil.fillSowingFields(mainForm, sowingReports);
        }

        List<OperationalReport> fodderReports = plicanteService.getOperationalReports(
                requestDtoForGettingOperationalReportsByTypeAndDate(FORM_2, dto.date()));
        if (fodderReports.isEmpty()) {
            log.info("Could not found Operational Reports for Fodder filling by date [{}]",
                    dto.date());
        } else {
            log.info("Found [{}] Operational Reports for Fodder", fodderReports.size());
            CropProductionUtil.fillFodderFields(mainForm, fodderReports);
        }

        List<OperationalReport> harvestingReports = plicanteService.getOperationalReports(
                requestDtoForGettingOperationalReportsByTypeAndDate(FORM_3, dto.date()));
        if (harvestingReports.isEmpty()) {
            log.info("Could not found Operational Reports for Harvesting filling by date [{}]",
                    dto.date());
        } else {
            log.info("Found [{}] Operational Reports for Harvesting", harvestingReports.size());
            CropProductionUtil.fillHarvestingFields(mainForm, harvestingReports);
        }

        UpdateInstanceResponseDto updated = plicanteService.updateCropProductionMainForm(mainForm);
        log.debug("Updated Main Form: [{}]", updated.id());
    }

    public byte[] createExcelSummaryReport(GetCropProductionSummaryReportDto dto) {
        LocalDate to = dto.to() != null ? dto.to() : LocalDate.now();
        LocalDate from = dto.from() != null && dto.from().isBefore(to) ? dto.from() : LocalDate.of(to.getYear(), 1, 1);

        long since = toEpochMilli(from);
        long until = toEpochMilli(to);

        List<OperationalReport> operationalReports = plicanteService.getOperationalReports(
                requestDtoForGettingOperationalReportsByTypeAndStatusAndDateInterval(dto.type(), since, until));

        if (operationalReports == null || operationalReports.isEmpty()) {
            throw new NoDataFoundException(REPORTS_NOT_FOUND);
        }

        Map<Dictionary, Map<String, Long>> codesMap = plicanteService.getDictionariesCodesMap(Set.of(DISTRICT));

        Map<Long, OperationalReport> recipientIdToLastReportMap = operationalReports.stream()
                .collect(toMap(
                        OperationalReport::getRecipientId,
                        Function.identity(),
                        maxBy(comparingLong(OperationalReport::getReportDate))
                ));

        Map<Long, List<ProducerData>> distIdToProducersList = new HashMap<>();
        for (Entry<Long, OperationalReport> entry : recipientIdToLastReportMap.entrySet()) {
            SubsidyRecipient recipient = getRecipientInfoById(entry.getKey());
            ProducerData producerData = new ProducerData(
                    recipient.getShortTitle(), recipient.getInn(), entry.getValue().getReportValues());
            distIdToProducersList.computeIfAbsent(recipient.getDistrictId(), k -> new ArrayList<>()).add(producerData);
        }

        Set<DistrictData> result = codesMap.get(DISTRICT).entrySet().stream()
                .map(entry -> new DistrictData(
                        entry.getKey(),
                        distIdToProducersList.getOrDefault(entry.getValue(), emptyList())
                ))
                .collect(Collectors.toCollection(
                        TreeSet::new
                ));

        Map<String, Object> headerData = Map.of(
                "reportDate", to,
                "year", to.getYear(),
                "lastYear", to.getYear() - 1
        );

        return excelReportFiller.fillReport(
                getClass().getResourceAsStream(dto.type().getTemplatePath()), result, headerData, dto.isDetailed());
    }

    private SubsidyRecipient getRecipientInfoById(Long id) {
        List<InstanceDto> dtoList = plicanteRestClient.getTableAttributesList(
                requestDtoToFindRecipientNameAndDistrictById(id));
        if (dtoList == null || dtoList.isEmpty()) {
            throw new EntityNotFoundException(INSTANCE_NOT_FOUND_BY_ID.formatted(id, TEMPLATE_TITLE));
        }
        return srMapper.toEntity(dtoList.get(0));
    }
}
