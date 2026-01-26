package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;
import su.petrosoft.apk_ack_integration.mapper.SubsidyProgramMapper;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.model.dto.plicante.instance.InstanceDto;
import su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.getAllSubsidyProgramsRequestDto;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.getSecondLevelSpRequestDto;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.getThirdLevelSpRequestDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubsidyProgramService {

    private final ApkPlicanteService apkService;
    private final PlicanteRestClient plicanteRestClient;
    private final SubsidyProgramMapper subsidyProgramMapper;

    public Set<SubsidyProgram> getAllSecondLevelSpFromDb() {
        Set<SubsidyProgram> allSecondLevelSpFromDB = apkService.findSubsidyPrograms(getSecondLevelSpRequestDto());
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

    public Map<Long, Set<SubsidyProgram>> getAllSpFromDb() {
        List<InstanceDto> dtoList = plicanteRestClient.getTableAttributesList(getAllSubsidyProgramsRequestDto());
        log.debug("Found [{}] Subsidy Programs in DB overall", dtoList.size());
        Map<Long, Set<SubsidyProgram>> resultMap = dtoList.stream()
                .map(subsidyProgramMapper::toEntity)
                .filter(SubsidyProgramUtil::validate)
                .collect(groupingBy(
                        SubsidyProgram::getLevel,
                        toSet()
                ));
        log.info("Found [{}] valid Subsidy Programs in DB overall", resultMap.values().stream().mapToInt(Collection::size).sum());
        return resultMap;
    }
}
