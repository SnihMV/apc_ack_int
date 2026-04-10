package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.client.NiFiRestClient;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;
import su.petrosoft.apk_ack_integration.exception.EntityNotFoundException;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.DictionaryData;
import su.petrosoft.apk_ack_integration.model.data.xml.UpdateCashPlanLimitXml;
import su.petrosoft.apk_ack_integration.model.dto.response.AckGetUpdateMessageResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.response.UpdateCashPlanLimitResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPEK;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPFK;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.DOPKR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KCSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KFSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KOSGU;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KVR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.KVSR;
import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.PURPOSE;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.TEMPLATE_TITLE;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.requestDtoToGetCplIdentAttrsByYearAndInn;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.requestDtoToGetCplIdentAttrsByCurrentYear;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.requestDtoToGetMonetaryFieldsById;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.INSTANCE_NOT_FOUND;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.INSTANCE_NOT_FOUND_BY_ID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CashPlanLimitService {

    private final ApkPlicanteService apkService;
    private final NiFiRestClient niFiRestClient;
    private final ExcelExtractor excelExtractor;
    private final XmlExtractor xmlExtractor;
    private final CashPlanLimitMapper cplMapper;
    private final InstanceUpdater instanceUpdater;
    private final PlicanteRestClient plicanteRestClient;
    private final DictionaryService dictionaryService;

//    public CreatingInstancesFromFileResponseDto createFromRosterKBKExcel(MultipartFile file) {
//
//        List<? extends CashPlanLimitData> dtoList = excelExtractor.getRosterKbkRows(file);
//        log.debug("Extracted from excel file: [{}] CashPlanLimit rows", dtoList.size());
//        List<CashPlanLimit> createdLimits = new ArrayList<>();
//        if (!dtoList.isEmpty()) {
//            Map<Dictionary, Map<String, Long>> codesMap = apkService.getDictionariesCodesMap(
//                    Set.of(KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE));
//            Set<CashPlanLimit> existedLimits = apkService.findCashPlanLimits(
//                    requestDtoToGetCplIdentAttrsByCurrentYear());
//            log.debug("Found in Plicante {} CashPlanLimits in total", existedLimits.size());
//
//            List<CashPlanLimit> limitsFromExcel = dtoList.stream()
//                    .map(dto -> cplMapper.toEntity(dto, codesMap))
//                    .collect(Collectors.toList());
//
//            limitsFromExcel.removeAll(existedLimits);
//            if (!limitsFromExcel.isEmpty()) {
//
//                limitsFromExcel.stream()
//                        .map(cpl -> apkService.createCashPlanLimit(cpl))
//                        .forEach(createdLimits::add);
//            }
//        }
//        return creatingInstancesFromFileResponseDto(dtoList, createdLimits, CashPlanLimit::getId);
//    }

   /* public CreatingInstancesFromFileResponseDto createFromUniBudgetExcel(MultipartFile file) {
        List<BaseUniBudgetExcelRow> dtoList = excelExtractor.getUniBudgetCodedRows(file);
        List<DescriptedBudgetItemData> dtoList = excelExtractor.getUniBudget2026ClarifiedRows(file);
        List<DescriptedBudgetItemData> dtoList = excelExtractor.getUniBudget20262801Rows(file);
        Set<CashPlanLimit> existingCPL = getLimitsForCurrentYear();
        List<CashPlanLimit> fromExcelCPL = uniBudgetRowService.getLimitsFromExcel(dtoList);
        fromExcelCPL.removeAll(existingCPL);
        log.info("Limits to save count: [{}]", fromExcelCPL.size());

        List<CashPlanLimit> createdLimits = new ArrayList<>();
        if (!fromExcelCPL.isEmpty()) {
            Map<Dictionary, Map<String, Long>> codesMap = apkService.getDictionariesCodesMap(
                Set.of(KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE));

            createdLimits = fromExcelCPL.stream()
                .map(cpl -> apkService.createCashPlanLimit(cpl, codesMap))
                .toList();
        }
        return creatingInstancesFromFileResponseDto(dtoList, createdLimits, CashPlanLimit::getId);
    }*/


    public UpdateCashPlanLimitResponseDto updateByXmlFile(MultipartFile file) {
        UpdateCashPlanLimitResponseDto response = UpdateCashPlanLimitResponseDto.builder()
                .updatedIds(new ArrayList<>())
                .build();

        UpdateCashPlanLimitXml xml = xmlExtractor.extractFromFile(file, UpdateCashPlanLimitXml.class);

        Map<Dictionary, Map<DictionaryData, Long>> codesMap = dictionaryService.getDataMap(
                Set.of(KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE));

        CashPlanLimit updater = cplMapper.toEntity(xml, codesMap);

        Long updatingId = plicanteRestClient.getTableAttributesList(
                        requestDtoToGetCplIdentAttrsByYearAndInn(LocalDate.now().getYear(), updater.getRecipientInn()))
                .stream()
                .map(cplMapper::toEntity)
                .filter(updater::equals)
                .findFirst()
                .map(CashPlanLimit::getId)
                .orElseThrow(() -> new EntityNotFoundException(INSTANCE_NOT_FOUND.formatted(TEMPLATE_TITLE)));

        instanceUpdater.updateCpl(updater, updatingId).ifPresent(id -> response.updatedIds().add(id));
        return response;
    }


    public UpdateCashPlanLimitResponseDto updateByXml() {
        UpdateCashPlanLimitResponseDto response = UpdateCashPlanLimitResponseDto.builder()
                .updatedIds(new ArrayList<>())
                .build();
        AckGetUpdateMessageResponseDto message = niFiRestClient.getUpdateMessage();
        UpdateCashPlanLimitXml xml = xmlExtractor.extractFromBase64String(message, UpdateCashPlanLimitXml.class);
        if (xml == null) {
            return response;
        }
        log.debug("Received request for Cash Plan Limit update: [{}]", xml);

        Map<Dictionary, Map<DictionaryData, Long>> codesMap = dictionaryService.getDataMap(
                Set.of(KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE));
        CashPlanLimit updatingCPL = cplMapper.toEntity(xml, codesMap);
        log.debug("Mapped to CashPlanLimit: [{}]", updatingCPL);

        Set<CashPlanLimit> allCashPlanLimits = apkService.findCashPlanLimits(
                requestDtoToGetCplIdentAttrsByCurrentYear());
        log.debug("Exist [{}] CashPlanLimits for [{}] year in DB", allCashPlanLimits.size(),
                LocalDateTime.now().getYear());

        CashPlanLimit cplToUpdate = allCashPlanLimits.stream()
                .filter(cpl -> cpl.equals(updatingCPL))
                .findFirst()
                .orElse(null);
        if (cplToUpdate == null) {
            return response;
        }
        log.debug("Trying to update CashPlanLimit [{}]", cplToUpdate.getId());
        updatingCPL.setId(cplToUpdate.getId());
        updatingCPL.setVersion(cplToUpdate.getVersion());
        long updatedCplId = apkService.updateCashPlanLimit(updatingCPL);
        log.info("CashPlanLimit [{}] updated", updatedCplId);
        response.updatedIds().add(updatedCplId);
        return response;
    }


    private CashPlanLimit findCplToUpdate(long id) {
        Set<CashPlanLimit> cashPlanLimits = apkService.findCashPlanLimits(requestDtoToGetMonetaryFieldsById(id));
        if (cashPlanLimits.isEmpty()) {
            throw new EntityNotFoundException(INSTANCE_NOT_FOUND_BY_ID.formatted(id, TEMPLATE_TITLE));
        }
        return cashPlanLimits.iterator().next();
    }

    public Set<CashPlanLimit> getLimitsForCurrentYear() {
        Map<Dictionary, Map<String, Long>> codesMap = apkService.getDictionariesCodesMap(
                Set.of(KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE));
        Set<CashPlanLimit> allSplByCurrentYear = apkService.findCashPlanLimits(
                requestDtoToGetCplIdentAttrsByCurrentYear());
        log.info("Found [{}] Cash Plan Limits for [{}] year in DB", allSplByCurrentYear.size(),
                LocalDateTime.now().getYear());
        return allSplByCurrentYear;
    }
//
//    public UpdateCashPlanLimitResponseDto updateByExcel(MultipartFile file) {
//        Map<Dictionary, Map<String, Long>> codesMap = apkService.getDictionariesCodesMap(
//                Set.of(KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE));
//        List<DescriptedBudgetItemData> uniBudgetExcelRows = excelExtractor.uniBudgetExcelRows(file);
//        Set<CashPlanLimit> limitsFromExcel = uniBudgetExcelRows.stream()
//                .map(cpl -> cplMapper.toEntity(cpl, codesMap))
//                .collect(Collectors.toSet());
//        Set<CashPlanLimit> existingCurrentYearLimits = getLimitsForCurrentYear();
//
//        Set<CashPlanLimit> intersection = new HashSet<>(limitsFromExcel);
//        intersection.retainAll(existingCurrentYearLimits);
//        ArrayList<Long> updatedCplIds = new ArrayList<>();
//        if (!intersection.isEmpty()) {
//            log.info("[{}] CashPlanLimits found to be updated", intersection.size());
//
//            for (CashPlanLimit excelCpl : intersection) {
//                for (CashPlanLimit existingCpl : existingCurrentYearLimits) {
//                    if (existingCpl.equals(excelCpl)) {
//                        excelCpl.setId(existingCpl.getId());
//                        excelCpl.setVersion(existingCpl.getVersion());
//                        long updatedCplId = apkService.updateCashPlanLimit(excelCpl);
//                        updatedCplIds.add(updatedCplId);
//                    }
//                }
//            }
//        }
//        return UpdateCashPlanLimitResponseDto.builder()
//                .incomingCount(limitsFromExcel.size())
//                .intersectedCount(intersection.size())
//                .updatedIds(updatedCplIds)
//                .build();
//    }
}
