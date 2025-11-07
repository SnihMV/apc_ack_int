package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.mapper.SubsidyProgramMapper;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.excel.SubsidyProgramExcelRowDto;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class SubsidyProgramExtractor {

    private final ApkPlicanteService apkService;
    private final ExcelRowParser excelRowParser;
    private final SubsidyProgramMapper subsidyProgramMapper;

    public Set<SubsidyProgram> getFromExcel(MultipartFile file) {
        try {
            List<SubsidyProgramExcelRowDto> programs = excelRowParser.getSubsidyProgramDtoList(file);
            log.debug("Extracted {} rows from excel file", programs.size());

        } catch (IOException e) {
            log.error("Excel file reading error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
