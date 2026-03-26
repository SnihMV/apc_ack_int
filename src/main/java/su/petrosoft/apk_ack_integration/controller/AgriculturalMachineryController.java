package su.petrosoft.apk_ack_integration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
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
@Tag(name = "Agricultural Machinery Controller",
        description = "Provides endpoints for managing instances associated with Agricultural Machinery Park")
public class AgriculturalMachineryController {
    private final AgriculturalMachineryService service;

    @Operation(
            summary = "Обрабатывает отчет по парку сельскохозяйственной техники",
            description = "Создает для СХП новые объекты журнала \"Парк сельскохозяйственной техники и оборудования\"" +
                    " на основании данного отчета, и удаляет текущие")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отчет успешно обработан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные в JSON-файле отчета",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Запрашиваемый объект не найден",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "422", description = "Не выполнено условие для обработки отчета",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
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
        log.error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponseDto.notFound(e.getMessage(), request.getRequestURI()));
    }
}
