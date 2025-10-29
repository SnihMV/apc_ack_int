package su.petrosoft.apk_ack_integration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import su.petrosoft.apk_ack_integration.model.xml.UpsertCashPlanLimitXml;
import su.petrosoft.apk_ack_integration.service.XmlExtractorService;

@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class XmlParserController {
    private final XmlExtractorService service;

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void processXml(@RequestBody UpsertCashPlanLimitXml dto) {
    }

}
