package su.petrosoft.apk_ack_integration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.service.SubsidyProgramService;

@RestController
@RequestMapping("api/v1/subsidyPrograms")
@RequiredArgsConstructor
public class SubsidyProgramController {
    private final SubsidyProgramService service;

    @PostMapping("excel")
    public void uploadExcel(@RequestParam MultipartFile file) {
        service.createNewProgramsFromExcel(file);
    }
}
