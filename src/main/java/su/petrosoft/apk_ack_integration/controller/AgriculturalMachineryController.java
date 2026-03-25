package su.petrosoft.apk_ack_integration.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import su.petrosoft.apk_ack_integration.exception.EntityNotFoundException;
import su.petrosoft.apk_ack_integration.exception.MachineryParkReportCountValidationException;
import su.petrosoft.apk_ack_integration.exception.MachineryParkReportParsingException;
import su.petrosoft.apk_ack_integration.model.dto.response.ErrorResponseDto;
import su.petrosoft.apk_ack_integration.service.AgriculturalMachineryService;

@Slf4j
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

    @ExceptionHandler({MachineryParkReportParsingException.class})
    public ResponseEntity<ErrorResponseDto> handleJsonParsingException(MachineryParkReportParsingException e, HttpServletRequest request) {
        log.error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponseDto.badRequest(e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler({MachineryParkReportCountValidationException.class})
    public ResponseEntity<ErrorResponseDto> handleReportValidationException(MachineryParkReportCountValidationException e, HttpServletRequest request) {
        log.error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponseDto.of(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), request.getRequestURI()
                ));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponseDto.notFound(e.getMessage(), request.getRequestURI()));
    }
}
