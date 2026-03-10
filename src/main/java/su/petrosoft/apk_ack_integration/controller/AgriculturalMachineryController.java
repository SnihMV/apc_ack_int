package su.petrosoft.apk_ack_integration.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import su.petrosoft.apk_ack_integration.exception.JsonParsingException;
import su.petrosoft.apk_ack_integration.model.dto.response.ErrorResponseDto;
import su.petrosoft.apk_ack_integration.service.AgriculturalMachineryService;

@RestController
@RequestMapping("api/v1/agriculturalMachinery")
@RequiredArgsConstructor
public class AgriculturalMachineryController {
    private final AgriculturalMachineryService service;

    @PostMapping("reportProcessing/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void processReport(@PathVariable(name = "id") Long id) {
        service.processReport(id);
    }

    @ExceptionHandler(JsonParsingException.class)
    public ResponseEntity<ErrorResponseDto> handleJsonParsingException(JsonParsingException e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponseDto.badRequest(e.getMessage(), request.getRequestURI()));
    }
}
