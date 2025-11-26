package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;
import su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil;

import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static su.petrosoft.apk_ack_integration.util.SubsidyProgramUtil.getThirdLevelSpRequestDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubsidyProgramService {

    private final ApkPlicanteService apkService;

    public Set<SubsidyProgram> getAllThirdLevelSpFromDb() {
        Set<SubsidyProgram> allThirdLevelSpFromDB = apkService.findSubsidyPrograms(getThirdLevelSpRequestDto());
        log.debug("Found [{}] Subsidy Programs in DB with level 3", allThirdLevelSpFromDB.size());
        Set<SubsidyProgram> allValidThirdLvlSPFromDb = allThirdLevelSpFromDB.stream()
                .filter(SubsidyProgramUtil::validate)
                .collect(toSet());
        log.info("Found [{}] valid Subsidy Programs in DB with level 3", allValidThirdLvlSPFromDb.size());
        return allValidThirdLvlSPFromDb;
    }
}
