package su.petrosoft.apk_ack_integration.util;

import lombok.extern.slf4j.Slf4j;
import su.petrosoft.apk_ack_integration.model.SubsidyProgram;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Slf4j
public class SubsidyProgramUtil {
    public static final long TEMPLATE_ID = 9492;
    public static final long LEVEL_ATTR = 3399;
    public static final long PARENT_ATTR = 3400;
    public static final long NAME_ATTR = 1759;
    public static final long CODE_ATTR = 3398;
    public static final long KCSR_ATTR = 3548;
    public static final long DOPKR_ATTR = 3549;

    public static boolean validate(SubsidyProgram sp) {
        Long lvl = sp.getLevel();
        if (lvl == null) {
            return false;
        }
        if (lvl == 1) {
            return sp.getCode() != null
                    && sp.getKcsr() == null
                    && sp.getDopKr() == null
                    && sp.getParentId() == null;
        }
        if (lvl == 2) {
            return sp.getKcsr() != null &&
                    sp.getDopKr() == null;
        }
        if (lvl == 3) {
            return sp.getKcsr() != null &&
                    sp.getDopKr() != null;
        }
        return false;
    }

    public static Map<Long, Map<SubsidyProgram, Long>> buildMapByLevel(List<SubsidyProgram> list) {
        return list.stream()
                .filter(SubsidyProgramUtil::validate)
                .collect(groupingBy(SubsidyProgram::getLevel, toMap(
                        Function.identity(),
                        SubsidyProgram::getId,
                        (p1, p2) -> p2
                )));
        log.debug("There are [{}] unique and valid SubsidyPrograms", count);
    }
    public static long getCount(Map<Long, Map<SubsidyProgram, Long>> map){
        return map.entrySet().stream()
                .flatMap(e -> e.getValue().keySet().stream())
                .count();
    }
}
