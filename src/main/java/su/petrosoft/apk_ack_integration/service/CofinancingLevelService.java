package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.mapper.CofinancingLevelMapper;
import su.petrosoft.apk_ack_integration.model.CofinancingLevel;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.excel.CofinancingLevelExcelRow;

import java.util.List;
import java.util.Set;

import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.getSpByKcsrAndDopkrRequestDto;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.getSpForCreateCofinLevelsRequestDto;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.getThirdLevelSpRequestDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class CofinancingLevelService {
    private final ExcelExtractor excelExtractor;
    private final ApkPlicanteService plicanteService;
    private final CofinancingLevelMapper mapper;

    public void createFromExcel(MultipartFile file) {
        List<CofinancingLevelExcelRow> rows = excelExtractor.getCofinancingLevelRows(file);
//        for (CofinancingLevelExcelRow row : rows) {
            Set<SubsidyProgram> subsidyPrograms = plicanteService.findSubsidyPrograms(
                    getSpForCreateCofinLevelsRequestDto());
        for (SubsidyProgram subsidyProgram : subsidyPrograms) {

            log.debug("=== SP === [{}]", subsidyProgram);
        }
//        }

    }
}
