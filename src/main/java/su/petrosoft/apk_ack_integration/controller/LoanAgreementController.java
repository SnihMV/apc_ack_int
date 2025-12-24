package su.petrosoft.apk_ack_integration.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.service.LoanAgreementService;

@Slf4j
@RestController
@RequestMapping("api/v1/loanAgreements")
@RequiredArgsConstructor
public class LoanAgreementController {
    private final LoanAgreementService service;

    @PostMapping("subsidyAmounts/xml")
    public void createAllFromXml(@RequestParam(name = "file") MultipartFile file) {
        log.info("Received file: [{}]", file.getOriginalFilename());
        service.createSubsidyAmountsFromXml(file);
    }
}
