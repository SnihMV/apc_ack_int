package su.petrosoft.apk_ack_integration.service;

import java.util.Map.Entry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;
import su.petrosoft.apk_ack_integration.model.data.DescriptedBudgetItemData;
import su.petrosoft.apk_ack_integration.model.data.DictionaryContaining;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static su.petrosoft.apk_ack_integration.util.PlicanteInstanceUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DictionaryService {

    private final PlicanteRestClient restClient;
    private final ApkPlicanteService apkPlicanteService;

    public Map<Dictionary, Set<Long>> addNewDictionaryCodes(
            Map<Dictionary, Map<Long, String>> existingDictionariesMap,
            List<DescriptedBudgetItemData> rows
    ) {
        Set<Dictionary> extractableDictionaries = rows.get(0).dictionariesData().keySet();
        fillMapWithExtraDictionaries(existingDictionariesMap, extractableDictionaries);

        log.info("Find new dictionaries data ...");
        Map<Dictionary, Set<Long>> createdDictionaries = new HashMap<>();
        for (DescriptedBudgetItemData row : rows) {
            updateDictionariesByContainableObject(row, extractableDictionaries, existingDictionariesMap, createdDictionaries);
        }
        log.info("Created [{}] new dictionary entries", createdDictionaries.values().stream().mapToInt(Set::size).sum());
        return createdDictionaries;
    }

    private void fillMapWithExtraDictionaries(Map<Dictionary, Map<Long, String>> codesMap, Set<Dictionary> extractableDictionaries) {
        Set<Dictionary> extraDictionaries = extractableDictionaries.stream()
                .filter(d -> !codesMap.containsKey(d))
                .collect(toSet());
        codesMap.putAll(apkPlicanteService.getDictionariesCodesMap(extraDictionaries));
    }

    private void updateDictionariesByContainableObject(
            DictionaryContaining containable,
            Set<Dictionary> updatableDictionaries,
            Map<Dictionary, Map<Long, String>> existingDictionariesMap,
            Map<Dictionary, Set<Long>> createdDictionaries
    ) {
        for (Dictionary updatingDictionary : updatableDictionaries) {
            Map<Long, String> existingValues = existingDictionariesMap.get(updatingDictionary);
            Optional<Long> createdId = createNewIfPresent(updatingDictionary, containable, existingValues);
            createdId.ifPresent(id -> createdDictionaries.computeIfAbsent(updatingDictionary, k -> new HashSet<>()).add(id));
        }
    }

    private Optional<Long> createNewIfPresent(
            Dictionary dictionary,
            DictionaryContaining containable,
            Map<Long, String> existingValues
    ) {
        String code = containable.dictionariesData().get(dictionary).getKey();
        String description = containable.dictionariesData().get(dictionary).getValue();

        return existingValues.entrySet().stream()
                .anyMatch(entry -> code.equalsIgnoreCase(entry.getValue()))
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
            Map<Long, String> existingValues
    ) {
        log.info("Creating new [{}] dictionary instance ...", dictionary);
        long id = restClient.createInstance(creatingDictionaryInstanceRequestDto(dictionary, code, description)).id();
        existingValues.put(id, code);
        log.info("Added new [{}] dictionary instance. Code: [{}], Description: [{}], id: [{}]",
                dictionary, code, description, id);
        return id;
    }
}
