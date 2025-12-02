package su.petrosoft.apk_ack_integration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Crop Production controller",
        description = "Provides endpoints for managing instances associated with Crop Production")
public class CropProductionController {
    private final CropProductionService service;

    @Operation(
            summary = "Update main form",
            description = "Updates main form by filling fields with associated operational reports files values")
    @PatchMapping("fillMainForm")
    public void fillMainForm(@RequestBody FillingMainFormRequestDto dto) {
        service.fillMainForm(dto);
    }
}
