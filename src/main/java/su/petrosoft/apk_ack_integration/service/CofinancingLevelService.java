package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.mapper.CofinancingLevelMapper;
import su.petrosoft.apk_ack_integration.mapper.SubsidyProgramMapper;
import su.petrosoft.apk_ack_integration.model.CofinancingLevel;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.CofinancingLevelExcelRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static su.petrosoft.apk_ack_integration.model.enums.CodeType.FINANCING_FORM;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.buildGettingSubsidyProgramsForCreationCofinancingLevelsRequestDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class CofinancingLevelService {
    private final ExcelExtractor excelExtractor;
    private final ApkPlicanteService plicanteService;
    private final CofinancingLevelMapper cflMapper;
    private final SubsidyProgramMapper spMapper;

    public void createFromExcel(MultipartFile file) {
        List<CofinancingLevelExcelRow> rows = excelExtractor.getCofinancingLevelRows(file);
        if (rows.isEmpty()) {
            return;
        }
        Set<SubsidyProgram> existedSPs = plicanteService.findSubsidyPrograms(
                buildGettingSubsidyProgramsForCreationCofinancingLevelsRequestDto());
        existedSPs.forEach(sp -> log.debug("--- FOUND SP --- [{}]", sp));

        Map<CodeType, Map<Long, String>> codesMap = plicanteService.getCodesMap(FINANCING_FORM);

        for (CofinancingLevelExcelRow row : rows) {
            SubsidyProgram sp = spMapper.toEntity(row);
            log.debug("+++ EXCEL SP +++ [{}]", sp);
            Optional<SubsidyProgram> optionalSP = existedSPs.stream()
                    .filter(sp::equals)
                    .findFirst();
            if (optionalSP.isEmpty()) {
                continue;
            }
            SubsidyProgram foundSP = optionalSP.get();
            CofinancingLevel excelCFL = cflMapper.toEntity(row);
            CofinancingLevel createdCFL = plicanteService.createCofinancingLevel(excelCFL, codesMap);
            ArrayList<Long> copiedCflIds = new ArrayList<>(foundSP.getCofinancingLevelIds());
            copiedCflIds.add(createdCFL.getId());
            foundSP.setCofinancingLevelIds(copiedCflIds);
            SubsidyProgram updatedSP = plicanteService.updateSubsidyProgram(foundSP);
            log.debug("=== UPDATED SP === [{}]", updatedSP);
        }
    }
}
