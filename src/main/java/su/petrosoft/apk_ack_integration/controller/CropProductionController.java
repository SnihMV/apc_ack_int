package su.petrosoft.apk_ack_integration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import su.petrosoft.apk_ack_integration.model.dto.request.FillingMainFormRequestDto;
import su.petrosoft.apk_ack_integration.service.CropProductionService;

@RestController
@RequestMapping("api/v1/cropProduction")
@RequiredArgsConstructor
public class CropProductionController {
    private final CropProductionService service;

    @PatchMapping("mainForm")
    public void fillMainForm(@RequestBody FillingMainFormRequestDto dto) {
        service.fillMainForm(dto);
    }
}
