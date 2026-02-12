package su.petrosoft.apk_ack_integration.service;

import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;
import su.petrosoft.apk_ack_integration.model.data.DictionaryContaining;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DictionaryService {

    private final PlicanteRestClient restClient;
    private final ApkPlicanteService apkPlicanteService;

    public Map<Dictionary, Set<Long>> updateCodesMap(
            Map<Dictionary, Map<Long, Map.Entry<String, String>>> existingDictionariesMap,
            List<? extends DictionaryContaining> rows
    ) {
        if (rows != null && !rows.isEmpty()) {
            Set<Dictionary> dictionaries = rows.get(0).dictionariesData().keySet();
            Map<Dictionary, Map<Long, Entry<String, String>>> dictionariesCodesMap = apkPlicanteService.getDictionariesNamedCodesMap(
                dictionaries);
        }
        log.info("Find new dictionaries data ...");
        Map<Dictionary, Set<Long>> createdDictionaries = new HashMap<>();
        if (rows != null && !rows.isEmpty()) {
            Set<Dictionary> updatableDictionaries = new HashSet<>(existingDictionariesMap.keySet());
            updatableDictionaries.retainAll(rows.get(0).dictionariesData().keySet());
            if (!updatableDictionaries.isEmpty()) {
                for (DictionaryContaining containable : rows) {
                    updateDictionariesByContainableObject(containable, updatableDictionaries, existingDictionariesMap, createdDictionaries);
                }
            }
        }
        log.info("Created [{}] new dictionary entries", createdDictionaries.values().stream().mapToInt(Set::size).sum());
        return createdDictionaries;
    }

    private void updateDictionariesByContainableObject(
            DictionaryContaining containable,
            Set<Dictionary> updatableDictionaries,
            Map<Dictionary, Map<Long, Map.Entry<String, String>>> existingDictionariesMap,
            Map<Dictionary, Set<Long>> createdDictionaries
    ) {
        for (Dictionary updatingDictionary : updatableDictionaries) {
            Map<Long, Map.Entry<String, String>> existingValues = existingDictionariesMap.get(updatingDictionary);
            Optional<Long> createdId = createNewIfPresent(updatingDictionary, containable, existingValues);
            createdId.ifPresent(id -> createdDictionaries.computeIfAbsent(updatingDictionary, k -> new HashSet<>()).add(id));
        }
    }

    private Optional<Long> createNewIfPresent(
            Dictionary dictionary,
            DictionaryContaining containable,
            Map<Long, Map.Entry<String, String>> existingValues
    ) {
        String code = containable.dictionariesData().get(dictionary).getKey();
        String description = containable.dictionariesData().get(dictionary).getValue();
//        for (Map.Entry<Long, Map.Entry<String, String>> entry : existingValues.entrySet()) {
//            if (entry.getValue().getKey().equalsIgnoreCase(code)) {
//                if (entry.getValue().getValue().equalsIgnoreCase(description) || description.isBlank()) {
//                    continue;
//                }
//                updateDictionaryDescription(entry.getKey(), description);
//            }
//        }
        return existingValues.entrySet().stream()
                .anyMatch(entry -> code.equalsIgnoreCase(entry.getValue().getKey()))
                ? Optional.empty()
                : Optional.of(createNewDictionaryInstance(dictionary, code, description, existingValues));
    }

//    private long updateDictionaryDescription(
//            Dictionary dictionary,
//            Long key,
//            String description) {
//        restClient.getTableAttributesList()
//restClient.updateInstance()
//    }

    private long createNewDictionaryInstance(
            Dictionary dictionary,
            String code,
            String description,
            Map<Long, Entry<String, String>> existingValues) {
        log.info("Creating new [{}] dictionary instance ...", dictionary);
        long id = restClient.createInstance(creatingDictionaryInstanceRequestDto(dictionary, code, description)).id();
        existingValues.put(id, Map.entry(code, description));
        log.info("Added new [{}] dictionary instance. Code: [{}], Description: [{}], id: [{}]",
                dictionary, code, description, id);
        return id;
    }
}
