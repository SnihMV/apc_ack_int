package su.petrosoft.apk_ack_integration.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.service.DictionaryService;

@Slf4j
@RestController
@RequestMapping("api/v1/dictionaries")
@RequiredArgsConstructor
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @PostMapping("upsert/excel")
    @ResponseStatus(HttpStatus.OK)
    public void createNewCodes(@RequestParam("file") MultipartFile file) {
        log.debug("Received file [{}] to extract and create new codes from",
            file.getOriginalFilename());
        dictionaryService.createNewCodes(file);
    }

}
