package su.petrosoft.apk_ack_integration.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.service.CashPlanLimitService;

@RestController
@Slf4j
@RequestMapping("api/v1/cashPlanLimit")
@RequiredArgsConstructor
public class CashPlanLimitController {
    private final CashPlanLimitService service;

    @PostMapping("upload-excel")
    @ResponseStatus(HttpStatus.OK)
    public void uploadExcel(@RequestParam MultipartFile file) {
        log.debug("upload-excel: Received file [{}]", file.getName());
        service.createFromExcel(file);
    }
}
