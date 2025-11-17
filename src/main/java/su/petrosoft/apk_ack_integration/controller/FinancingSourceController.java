package su.petrosoft.apk_ack_integration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.service.FinancingSourceService;

@RestController
@RequestMapping("api/v1/financingSources")
@RequiredArgsConstructor
public class FinancingSourceController {
    private final FinancingSourceService service;

    @PostMapping("excel")
    @ResponseStatus(HttpStatus.OK)
    public void createFinancingSources(@RequestParam MultipartFile file) {
        service.createFinancingSources(file);
    }
}
