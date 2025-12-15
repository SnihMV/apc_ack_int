package su.petrosoft.apk_ack_integration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.service.CofinancingLevelService;

@RestController
@RequestMapping("api/v1/cofinancingLevels")
@RequiredArgsConstructor
public class CofinancingLevelController {
    private final CofinancingLevelService service;

    @PostMapping("excel")
    public void createFromExcel(@RequestParam MultipartFile file) {
        service.createFromExcel(file);
    }
}
