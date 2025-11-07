package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.excel.SubsidyProgramExcelRowDto;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubsidyProgramService {
    private final SubsidyProgramProcessor processor;


    public void createNewProgramsFromExcel(MultipartFile file) {

    }

}
