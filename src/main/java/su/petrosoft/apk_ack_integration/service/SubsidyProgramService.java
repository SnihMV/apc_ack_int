package su.petrosoft.apk_ack_integration.service;

import static java.util.stream.Collectors.toSet;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.requestDtoToGetAllPrograms;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.requestDtoToFindSecondLevelSubsidyPrograms;

import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;
import su.petrosoft.apk_ack_integration.mapper.SubsidyProgramMapper;
import su.petrosoft.apk_ack_integration.model.DictionaryData;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.model.enums.Dictionary;
import su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubsidyProgramService {

    private final ApkPlicanteService apkService;
    private final PlicanteRestClient plicanteRestClient;
    private final SubsidyProgramMapper spMapper;

    public Set<SubsidyProgram> getAllSecondLevelSpFromDb(Map<Dictionary, Map<DictionaryData, Long>> codesMap) {
        Set<SubsidyProgram> allSecondLevelSpFromDB = apkService.findSubsidyPrograms(
                requestDtoToFindSecondLevelSubsidyPrograms());
        log.debug("Found [{}] Subsidy Programs in DB with level 2", allSecondLevelSpFromDB.size());
        Set<SubsidyProgram> allValidSecondLvlSPFromDb = allSecondLevelSpFromDB.stream()
                .filter(SubsidyProgramUtil::validate)
                .collect(toSet());
        log.info("Found [{}] valid Subsidy Programs in DB with level 2", allValidSecondLvlSPFromDb.size());
        return allValidSecondLvlSPFromDb;
    }

//    public Set<SubsidyProgram> getAllThirdLevelSpFromDb() {
//        Set<SubsidyProgram> allThirdLevelSpFromDB = apkService.findSubsidyPrograms(getThirdLevelSpRequestDto());
//        log.debug("Found [{}] Subsidy Programs in DB with level 3", allThirdLevelSpFromDB.size());
//        Set<SubsidyProgram> allValidThirdLvlSPFromDb = allThirdLevelSpFromDB.stream()
//                .filter(SubsidyProgramUtil::validate)
//                .collect(toSet());
//        log.info("Found [{}] valid Subsidy Programs in DB with level 3", allValidThirdLvlSPFromDb.size());
//        return allValidThirdLvlSPFromDb;
//    }

    public Set<SubsidyProgram> getAllSubsidyProgram(Map<Dictionary, Map<DictionaryData, Long>> codesMap) {
        log.info("Getting all Subsidy_Programs ...");
        List<InstanceDto> dtoList = plicanteRestClient.getTableAttributesList(
                requestDtoToGetAllPrograms());
        Set<SubsidyProgram> subsidyPrograms = dtoList.stream()
                .map(dto -> spMapper.toEntity(dto))
                .collect(toSet());
        log.info("Found Subsidy_Programs count: [{}]", subsidyPrograms.size());
        return subsidyPrograms;
    }
}
