package su.petrosoft.apk_ack_integration.service;

import static java.util.Collections.emptyList;
import static java.util.Comparator.comparingLong;
import static java.util.function.BinaryOperator.maxBy;
import static java.util.stream.Collectors.toMap;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DISTRICT;
import static su.petrosoft.apk_ack_integration.model.enums.ReportType.FORM_1;
import static su.petrosoft.apk_ack_integration.model.enums.ReportType.FORM_2;
import static su.petrosoft.apk_ack_integration.model.enums.ReportType.FORM_3;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessage.RECIPIENT_BY_ID_NOT_FOUND;
import static su.petrosoft.apk_ack_integration.util.OperationalReportUtil.requestDtoForGettingOperationalReportsByTypeAndDate;
import static su.petrosoft.apk_ack_integration.util.OperationalReportUtil.requestDtoForGettingOperationalReportsByTypeAndStatusAndDateInterval;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.toEpochMilli;
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

    private static final String TEMPLATE_PATH = "/templates/summaryReportForm1.xlsx";
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
        LocalDate from = dto.from() != null ? dto.from() : LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate to = dto.to() != null ? dto.to() : LocalDate.now();

        long since = toEpochMilli(from);
        long until = toEpochMilli(to);

        List<OperationalReport> operationalReports = plicanteService.getOperationalReports(
                requestDtoForGettingOperationalReportsByTypeAndStatusAndDateInterval(dto.type(), since,
                        until));

//        List<OperationalReport> or = plicanteService.getOperationalReports(
//                requestDtoForGettingOperationalReportsByTypesAndStatusAndDateInterval(since, until,
//                        5858, List.of(FORM_1.getId(), FORM_2.getId(), FORM_3.getId())));

        Map<Dictionary, Map<String, Long>> codesMap = plicanteService.getDictionariesCodesMap(Set.of(DISTRICT));

//        Map<Long, Map<ReportType, OperationalReport>> collect = or.stream()
//                .collect(groupingBy(
//                        OperationalReport::getRecipientId,
//                        toMap(
//                                OperationalReport::getReportType,
//                                Function.identity(),
//                                maxBy(comparingLong(OperationalReport::getReportDate))
//                        )));
//
//        Map<String, List<ProducerData>> collect1 = collect.entrySet().stream()
//                .map(e -> {
//                    SubsidyRecipient recipient = getRecipientInfoById(e.getKey());
//                    String districtName = dictionaryCodeById(codesMap, DISTRICT, recipient.getDistrictId());
//                    Map<String, BigDecimal> summaryValues = e.getValue().values().stream()
//                            .flatMap(rep -> rep.getReportValues().entrySet().stream())
//                            .collect(toMap(
//                                    Entry::getKey,
//                                    Entry::getValue
//                            ));
//                    return Map.entry(
//                            districtName,
//                            new ProducerData(
//                                    recipient.getShortTitle(),
//                                    recipient.getInn(),
//                                    summaryValues
//                            ));
//                })
//                .collect(groupingBy(
//                        Entry::getKey,
//                        mapping(Entry::getValue, toList())
//                ));

        if (operationalReports == null || operationalReports.isEmpty()) {
            return null;
        }

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
                        distIdToProducersList.getOrDefault(entry.getKey(), emptyList())
                ))
                .collect(Collectors.toCollection(
                        TreeSet::new
                ));

        Map<String, Object> headerData = Map.of(
                "reportDate", LocalDate.now(),
                "year", LocalDate.now().getYear()
        );

        return excelReportFiller.fillReport(
                getClass().getResourceAsStream(TEMPLATE_PATH), result, headerData, dto.isDetailed());
    }

    private SubsidyRecipient getRecipientInfoById(Long id) {
        List<InstanceDto> dtoList = plicanteRestClient.getTableAttributesList(
                requestDtoToFindRecipientNameAndDistrictById(id));
        if (dtoList == null || dtoList.isEmpty()) {
            throw new EntityNotFoundException(RECIPIENT_BY_ID_NOT_FOUND.getMessage().formatted(id));
        }
        return srMapper.toEntity(dtoList.get(0));
    }
}
