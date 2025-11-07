package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.excel.SubsidyProgramExcelRowDto;
import su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil;

import java.util.List;
import java.util.Map;

import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubsidyProgramProcessor {
    private final ApkPlicanteService apkService;
    private final ExcelExtractor excelExtractor;

    public void createNewSubsidyPrograms(MultipartFile file) {
        List<SubsidyProgram> list = apkService.getAllSubsidyPrograms();
        log.debug("Received [{}] SubsidyPrograms from DB in total", list.size());

        Map<Long, Map<SubsidyProgram, Long>> mapByLevel = buildMapByLevel(list);
        log.debug("There are [{}] valid and unique SubsidyPrograms of all", getCount(mapByLevel));

        List<SubsidyProgramExcelRowDto> subsidyProgramDtoList = excelExtractor.getSubsidyProgramDtoList(file);
    }
}
