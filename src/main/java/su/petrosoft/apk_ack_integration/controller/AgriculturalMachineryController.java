package su.petrosoft.apk_ack_integration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import su.petrosoft.apk_ack_integration.service.AgriculturalMachineryService;

@RestController
@RequestMapping("api/v1/agriculturalMachinery")
@RequiredArgsConstructor
public class AgriculturalMachineryController {
    private final AgriculturalMachineryService service;

    @PostMapping("reportProcessing/{id}")
    public void processReport(@PathVariable(name = "id") Long id) {
        service.processReport(id);
    }
}
