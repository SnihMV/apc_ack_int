package su.petrosoft.apk_ack_integration.service;

import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KCSR;
import static su.petrosoft.apk_ack_integration.model.enums.CodeType.KDR;
import static su.petrosoft.apk_ack_integration.util.DictionaryUtil.getCodeId;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.buildMapByLevel;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.getCount;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.enums.CodeType;
import su.petrosoft.apk_ack_integration.model.excel.SubsidyProgramExcelRowDto;

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
        log.debug("There are [{}] valid and unique SubsidyPrograms of all",
            getCount(subsidyProgramMap));

        List<SubsidyProgramExcelRowDto> subsidyProgramDtoList =
            excelExtractor.getSubsidyProgramDtoList(file);

        if (!subsidyProgramDtoList.isEmpty()) {
            Map<CodeType, Map<Long, String>> codesMap = apkService.getCodesMap(KCSR, KDR);
            subsidyProgramDtoList.forEach(dto -> processRow(dto, subsidyProgramMap, codesMap));
        }
        subsidyProgramMap.entrySet().stream()
            .peek(k-> System.out.println("### "+k.getKey()))
            .flatMap(k->k.getValue().stream())
            .forEach(System.out::println);
    }

    private void processRow(
        SubsidyProgramExcelRowDto dto,
        Map<Long, Set<SubsidyProgram>> map,
        Map<CodeType, Map<Long, String>> codesMap) {

        SubsidyProgram fstLevelSp = SubsidyProgram.builder()
            .level(1L)
            .code(dto.code())
            .title("Направление № " + dto.code())
            .build();
        log.debug("First level Subsidy Program from excel row: [{}]", fstLevelSp);

        Long fstLvlSpId = obtainSubsidyProgramId(map, fstLevelSp);
        log.debug("Subsidy Program Map: {}", getCount(map));

        SubsidyProgram scdLevelSp = SubsidyProgram.builder()
            .level(2L)
            .parentId(fstLvlSpId)
            .title(dto.kcsrTitle())
            .kcsr(getCodeId(codesMap, KCSR, dto.kcsr()))
            .build();
        log.debug("Second level Subsidy Program from excel row: [{}]", scdLevelSp);

        Long scdLvlSpId = obtainSubsidyProgramId(map, scdLevelSp);
        log.debug("Subsidy Program Map: {}", getCount(map));

        SubsidyProgram trdLevelSp = SubsidyProgram.builder()
            .level(3L)
            .parentId(scdLvlSpId)
            .title(dto.dopKrTitle())
            .kcsr(scdLevelSp.getKcsr())
            .dopKr(getCodeId(codesMap, KDR, dto.dopKr()))
            .build();
        log.debug("Third level Subsidy Program from excel row: [{}]", trdLevelSp);

        Long trdLvlSpId = obtainSubsidyProgramId(map, trdLevelSp);
        log.debug("Subsidy Program Map: {}", getCount(map));
    }

    private Long obtainSubsidyProgramId(Map<Long, Set<SubsidyProgram>> map, SubsidyProgram sp) {
        Set<SubsidyProgram> setByLevel = map.computeIfAbsent(sp.getLevel(), m -> new HashSet<>());

        return setByLevel.stream()
            .filter(existed -> existed.equals(sp))
            .findFirst()
            .map(SubsidyProgram::getId)
            .orElseGet(()->{
                log.debug("No such Subsidy Program within existed. Trying to save it as new");
//                SubsidyProgram saved = apkService.createProgram(sp);
                SubsidyProgram saved = new SubsidyProgram(new Random().nextLong(), 1L,
                    sp.getParentId(), sp.getLevel(),
                    sp.getCode(), sp.getKcsr(), sp.getDopKr(), sp.getTitle());
                log.debug("Subsidy Program successfully saved with id: [{}]", saved.getId());
                setByLevel.add(saved);
                return saved.getId();
            });
//
//        return fstLevelSet.computeIfAbsent(sp, s -> {
//            log.debug("No such Subsidy Program within existed. Trying to save it as new");
////            Long id = apkService.createProgram(s).getId();
//            Long id = new Random().nextLong();
//            log.debug("Subsidy Program successfully saved with id: [{}]", id);
//            return id;
//        });
    }

}
