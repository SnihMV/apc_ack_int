package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class DictionaryService {

    private final ExcelExtractor excelExtractor;

    public void createNewCodes(MultipartFile file) {
        excelExtractor.budgetItemDictionaries(file);
    }
}
