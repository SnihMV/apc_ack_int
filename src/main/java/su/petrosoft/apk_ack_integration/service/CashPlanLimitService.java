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

import static su.petrosoft.apk_ack_integration.model.enums.Dictionary.*;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.*;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.INSTANCE_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class CashPlanLimitService {

    private final PlicanteInstanceService apkService;
    private final NiFiRestClient niFiRestClient;
    private final ExcelExtractor excelExtractor;
    private final XmlExtractor xmlExtractor;
    private final CashPlanLimitMapper cplMapper;
    private final InstanceUpdater instanceUpdater;
    private final PlicanteRestClient plicanteRestClient;
    private final DictionaryService dictionaryService;

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

    public Set<CashPlanLimit> getLimitsForCurrentYear() {
        Map<Dictionary, Map<String, Long>> codesMap = apkService.getDictionariesCodesMap(
                Set.of(KVSR, KFSR, KCSR, KVR, KOSGU, DOPEK, DOPKR, DOPFK, PURPOSE));
        Set<CashPlanLimit> allSplByCurrentYear = apkService.findCashPlanLimits(
                requestDtoToGetCplIdentAttrsByCurrentYear());
        log.info("Found [{}] Cash Plan Limits for [{}] year in DB", allSplByCurrentYear.size(),
                LocalDateTime.now().getYear());
        return allSplByCurrentYear;
    }
}
