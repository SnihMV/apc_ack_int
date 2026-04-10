package su.petrosoft.apk_ack_integration.util;

import static su.petrosoft.apk_ack_integration.model.enums.SqlOperation.*;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.ATTRIBUTE_TYPE_MISMATCH;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.DICTIONARY_CODE_NOT_FOUND;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.DICTIONARY_DESCRIPTION_NOT_FOUND;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.DICTIONARY_ID_NOT_FOUND;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.DICTIONARY_NOT_OBTAINED;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import lombok.extern.slf4j.Slf4j;
import su.petrosoft.apk_ack_integration.exception.DictionaryException;
import su.petrosoft.apk_ack_integration.exception.InstanceIllegalStateException;
import su.petrosoft.apk_ack_integration.model.DictionaryData;
import su.petrosoft.apk_ack_integration.model.dto.plicante.CreateInstanceRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.GetAttributesListRequestDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.RequestedAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Pair;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.StringAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.Filter;
import su.petrosoft.apk_ack_integration.model.dto.plicante.filter.StringFilterAttribute;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.dto.response.CreatingInstancesFromFileResponseDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;

@Slf4j
public class PlicanteInstanceUtil {

    public static final ZoneId MOSCOW_ZONE = ZoneId.of("Europe/Moscow");

    public static <T> CreatingInstancesFromFileResponseDto creatingInstancesFromFileResponseDto(
            List<?> dtoList,
            List<T> createdInstances,
            Function<T, Long> function
    ) {
        return new CreatingInstancesFromFileResponseDto(
                dtoList.size(),
                createdInstances.size(),
                createdInstances.stream()
                        .map(function)
                        .toList());
    }

    public static GetAttributesListRequestDto requestDtoToGetDictionaryDataByCodes(
            Dictionary dictionary, Set<String> codes) {
        return GetAttributesListRequestDto.builder()
                .templateId(dictionary.getTemplateId())
                .attributes(List.of(
                        new RequestedAttribute(dictionary.getCodeAttrId()),
                        new RequestedAttribute(dictionary.getDescriptionAttrId())
                ))
                .filter(new Filter(List.of(
                        new StringFilterAttribute(dictionary.getCodeAttrId(), List.of(IN), codes)
                )))
                .build();
    }

    public static CreateInstanceRequestDto requestDtoToCreateDictionaryInstance(
            Dictionary type, String code, String description) {
        return new CreateInstanceRequestDto(
                InstanceDto.builder()
                        .templateId(type.getTemplateId())
                        .attributes(List.of(
                                new StringAttribute(type.getCodeAttrId(), code),
                                new StringAttribute(type.getDescriptionAttrId(), description)))
                        .build());
    }

//    public static UpdateInstanceRequestDto requestDtoForUpdatingDictionaryDescription(Dictionary dictionary, long id, String description) {
//        return new UpdateInstanceRequestDto(
//                InstanceDto.builder()
//                        .id(id)
//                        .templateId(dictionary.getTemplateId())
//                        .version(updatedSP.getVersion())
//                        .attributes(List.of(
//                                new LinkedAttribute(COFIN_LVL_ATTR, updatedSP.getCofinancingLevelIds())))
//                        .build());
//    }

    public static Long dictionaryIdByCode(
            Map<Dictionary, Map<DictionaryData, Long>> codesMap,
            Dictionary dictionary,
            String code
    ) {
        Map<DictionaryData, Long> dataToIdMap= codesMap.get(dictionary);
        if (dataToIdMap == null) {
            throw new IllegalStateException(DICTIONARY_NOT_OBTAINED.formatted(dictionary));
        }
        Long id = dataToIdMap.get(new DictionaryData(code));
        if (id == null) {
            throw new DictionaryException(DICTIONARY_CODE_NOT_FOUND.formatted(code, dictionary));
        }
        return id;
    }

    public static String dictionaryCodeById(Map<Dictionary, Map<DictionaryData, Long>> codesMap, Dictionary dictionary, long id) {
        return codesMap.get(dictionary).entrySet().stream()
                .filter(entry -> entry.getValue().equals(id))
                .findFirst()
                .map(entry -> entry.getKey().getCode())
                .orElseThrow(() -> new DictionaryException(DICTIONARY_ID_NOT_FOUND.formatted(dictionary, id)));
    }

    public static String dictionaryCodeDescription(
            Map<Dictionary, Map<String, String>> allCodes, Dictionary dictionary, String code) {
        String description = allCodes.get(dictionary).get(code);
        if (description == null) {
            throw new DictionaryException(DICTIONARY_DESCRIPTION_NOT_FOUND.formatted(code, dictionary));
        }
        return description;
    }

    public static Long toEpochMilli(LocalDate day) {
        return toEpochMilli(day, MOSCOW_ZONE);
    }

    public static Long toEpochMilli(LocalDate day, ZoneId zone) {
        if (day == null) {
            return null;
        }
        ZoneId targetZone = zone != null ? zone : MOSCOW_ZONE;
        return day.atStartOfDay(targetZone)
                .toInstant()
                .toEpochMilli();
    }

    public static LocalDate toLocalDate(Long epochMilli) {
        if (epochMilli == null) {
            return null;
        }
        return Instant.ofEpochMilli(epochMilli)
                .atZone(MOSCOW_ZONE)
                .toLocalDate();
    }

    @SuppressWarnings("unchecked")
    public static <T> T extractData(List<Attribute<?>> attributes, long attributeId) {
        return findAttribute(attributes, attributeId)
            .map(attr -> (T) attr.getData())
            .orElse(null);
    }

    @SuppressWarnings("unchecked")
    public static <T> Collection<T> extractAllData(List<Attribute<?>> attributes, long attributeId) {
        return findAttribute(attributes, attributeId)
                .map(attr -> (Collection<T>) attr.getAllData())
                .orElse(new ArrayList<>());
    }

    public static GetAttributesListRequestDto requestDtoToGetDictionaryData(Dictionary dictionary) {
        return GetAttributesListRequestDto.builder()
                .templateId(dictionary.getTemplateId())
                .attributes(List.of(
                        new RequestedAttribute(dictionary.getCodeAttrId()),
                        new RequestedAttribute(dictionary.getDescriptionAttrId())
                ))
                .build();
    }

    private static Optional<Attribute<?>> findAttribute(List<Attribute<?>> attributes, long attributeId) {
        return attributes.stream()
                .filter(a -> a.id().equals(attributeId))
                .findFirst();
    }

}
