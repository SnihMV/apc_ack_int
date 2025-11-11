package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.SubsidyProgramExcelRowDto;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KCSR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KDR;
import static su.petrosoft.apk_ack_integration.util.DictionaryUtil.getCodeId;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.buildMapByLevel;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.getCount;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubsidyProgramProcessor {

    private final ApkPlicanteService apkService;
    private final ExcelExtractor excelExtractor;

    public void createNewSubsidyPrograms(MultipartFile file) {
        List<SubsidyProgram> list = apkService.getAllSubsidyPrograms();
        log.debug("Received [{}] SubsidyPrograms from DB in total", list.size());

        Map<Long, Set<SubsidyProgram>> subsidyProgramMap = buildMapByLevel(list);
        log.debug("There are [{}] valid and unique SubsidyPrograms of all", getCount(subsidyProgramMap));
        printSpMap(subsidyProgramMap);

        List<SubsidyProgramExcelRowDto> subsidyProgramDtoList = excelExtractor.getSubsidyProgramDtoList(file);
        log.debug("Extracted from excel file: [{}] SubsidyProgramRowDto", subsidyProgramDtoList.size());

        if (!subsidyProgramDtoList.isEmpty()) {
            Map<CodeType, Map<Long, String>> codesMap = apkService.getCodesMap(KCSR, KDR);
            subsidyProgramDtoList.forEach(dto -> processRow(dto, subsidyProgramMap, codesMap));
        }
        printSpMap(subsidyProgramMap);
    }

    private void processRow(
            SubsidyProgramExcelRowDto dto,
            Map<Long, Set<SubsidyProgram>> subsidiesMap,
            Map<CodeType, Map<Long, String>> codesMap) {

        SubsidyProgram fstLevelSp = getFirstLevelSP(dto, subsidiesMap);
        SubsidyProgram scdLevelSp = getSecondLevelSP(dto, fstLevelSp, codesMap, subsidiesMap);
        SubsidyProgram trdLevelSp = getThirdLevelSP(dto, scdLevelSp, codesMap, subsidiesMap);
        log.debug("Subsidy Program Map count: {}", getCount(subsidiesMap));
    }

    private void printSpMap(Map<Long, Set<SubsidyProgram>> subsidyProgramMap) {
        subsidyProgramMap.entrySet().stream()
                .peek(k -> System.out.println("### " + k.getKey() +" Subsidy Program Level:"))
                .flatMap(k -> k.getValue().stream())
                .forEach(System.out::println);
    }

    private SubsidyProgram getFirstLevelSP(SubsidyProgramExcelRowDto dto, Map<Long, Set<SubsidyProgram>> map) {
        SubsidyProgram fstLvlSp = SubsidyProgram.builder()
                .level(1L)
                .code(dto.code())
                .title("Направление № " + dto.code())
                .build();
        Long id = obtainSubsidyProgramId(map, fstLvlSp);
        fstLvlSp.setId(id);
        log.debug("First level Subsidy Program from excel row: [{}]", fstLvlSp);
        return fstLvlSp;

    }

    private SubsidyProgram getSecondLevelSP(
            SubsidyProgramExcelRowDto dto, SubsidyProgram fstLevelSp,
            Map<CodeType, Map<Long, String>> codesMap, Map<Long, Set<SubsidyProgram>> subsidiesMap) {
        SubsidyProgram scdLvlSP = SubsidyProgram.builder()
                .level(2L)
                .parentId(fstLevelSp.getId())
                .title(dto.kcsrTitle())
                .kcsr(getCodeId(codesMap, KCSR, dto.kcsr()))
                .build();
        Long id = obtainSubsidyProgramId(subsidiesMap, scdLvlSP);
        scdLvlSP.setId(id);
        log.debug("Second level Subsidy Program from excel row: [{}]", scdLvlSP);
        return scdLvlSP;
    }

    private SubsidyProgram getThirdLevelSP(SubsidyProgramExcelRowDto dto, SubsidyProgram scdLevelSp, Map<CodeType, Map<Long, String>> codesMap, Map<Long, Set<SubsidyProgram>> subsidiesMap) {
        SubsidyProgram trdLvlSP = SubsidyProgram.builder()
                .level(3L)
                .parentId(scdLevelSp.getId())
                .title(dto.dopKrTitle())
                .kcsr(scdLevelSp.getKcsr())
                .dopKr(getCodeId(codesMap, KDR, dto.dopKr()))
                .build();
        Long id = obtainSubsidyProgramId(subsidiesMap, trdLvlSP);
        trdLvlSP.setId(id);
        log.debug("Third level Subsidy Program from excel row: [{}]", trdLvlSP);
        return trdLvlSP;
    }

    private Long obtainSubsidyProgramId(Map<Long, Set<SubsidyProgram>> map, SubsidyProgram sp) {
        Set<SubsidyProgram> setByLevel = map.computeIfAbsent(sp.getLevel(), m -> new HashSet<>());

        return setByLevel.stream()
                .filter(existed -> existed.equals(sp))
                .findFirst()
                .map(SubsidyProgram::getId)
                .orElseGet(() -> {
                    log.debug("No such Subsidy Program among existing. Trying to save it");
                    SubsidyProgram saved = apkService.createProgram(sp);
                    log.debug("Subsidy Program successfully saved with id: [{}]", saved.getId());
                    setByLevel.add(saved);
                    return saved.getId();
                });
    }
}
