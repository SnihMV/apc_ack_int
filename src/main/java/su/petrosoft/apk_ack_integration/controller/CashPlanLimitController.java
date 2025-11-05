package su.petrosoft.apk_ack_integration.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.model.dto.response.CreateFromExcelResponseDto;
import su.petrosoft.apk_ack_integration.service.CashPlanLimitService;

@RestController
@Slf4j
@RequestMapping("api/v1/cashPlanLimits")
@RequiredArgsConstructor
public class CashPlanLimitController {
    private final CashPlanLimitService service;

    @PostMapping("excel")
    @ResponseStatus(HttpStatus.OK)
    public CreateFromExcelResponseDto uploadExcel(@RequestParam MultipartFile file) {
        log.debug("Excel: Received file [{}]", file.getOriginalFilename());
        return service.createFromExcel(file);
    }
}
