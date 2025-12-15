package su.petrosoft.apk_ack_integration.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.excel.CofinancingLevelExcelRow;
import su.petrosoft.apk_ack_integration.model.excel.RosterKbkExcelRow;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetCodedExcelRow;
import su.petrosoft.apk_ack_integration.model.excel.UniBudgetExcelRow;

@Slf4j
@Component
public class ExcelRowMapper {

    public UniBudgetCodedExcelRow parseToUniBudgetCodedRow(Row row) {
        return new UniBudgetCodedExcelRow(
                row.getCell(0).getStringCellValue(),
                row.getCell(1).getStringCellValue(),
                row.getCell(2).getStringCellValue(),
                row.getCell(3).getStringCellValue(),
                row.getCell(4).getStringCellValue(),
                row.getCell(5).getStringCellValue(),
                row.getCell(6).getStringCellValue(),
                row.getCell(7).getStringCellValue(),
                row.getCell(8).getStringCellValue(),
                row.getCell(9).getStringCellValue(),
                row.getCell(10).getStringCellValue(),
                row.getCell(11).getStringCellValue(),
                row.getCell(12).getStringCellValue(),
                row.getCell(13).getStringCellValue(),
                row.getCell(14).getStringCellValue(),
                row.getCell(15).getStringCellValue(),
                row.getCell(16).getStringCellValue(),
                row.getCell(17).getStringCellValue(),
                row.getCell(18).getStringCellValue(),
                row.getCell(19).getStringCellValue(),
                row.getCell(20).getStringCellValue(),
                row.getCell(21).getStringCellValue(),
                row.getCell(22).getNumericCellValue(),
                row.getCell(23).getNumericCellValue(),
                row.getCell(24).getNumericCellValue(),
                row.getCell(25).getNumericCellValue(),
                row.getCell(26).getNumericCellValue(),
                row.getCell(27).getNumericCellValue(),
                row.getCell(28).getNumericCellValue(),
                row.getCell(29).getNumericCellValue(),
                row.getCell(30).getNumericCellValue(),
                row.getCell(31).getNumericCellValue(),
                row.getCell(32).getNumericCellValue(),
                row.getCell(33).getNumericCellValue(),
                row.getCell(34).getNumericCellValue(),
                row.getCell(35).getNumericCellValue(),
                row.getCell(36).getNumericCellValue(),
                row.getCell(37).getNumericCellValue(),
                row.getCell(38).getNumericCellValue(),
                row.getCell(39).getNumericCellValue(),
                row.getCell(40).getNumericCellValue()
        );
    }

    public RosterKbkExcelRow parseToRosterKbkRow(Row row) {
        return RosterKbkExcelRow.builder()
                .section(row.getCell(0).getStringCellValue())
                .subsection(row.getCell(1).getStringCellValue())
                .kcsr(row.getCell(2).getStringCellValue())
                .dopKr(row.getCell(3).getStringCellValue())
                .kvr(row.getCell(4).getStringCellValue())
                .kosgu(row.getCell(5).getStringCellValue())
                .kvsr(row.getCell(6).getStringCellValue())
                .dopFk(row.getCell(7).getStringCellValue())
                .dopEk(row.getCell(8).getStringCellValue())
                .purpose(row.getCell(9).getStringCellValue())
                .assignTotal(row.getCell(10).getNumericCellValue())
                .assignFederal(row.getCell(11).getNumericCellValue())
                .assignRegional(row.getCell(12).getNumericCellValue())
                .financeTotal(row.getCell(13).getNumericCellValue())
                .requested(row.getCell(14).getNumericCellValue())
                .m01Amt(row.getCell(15).getNumericCellValue())
                .m02Amt(row.getCell(16).getNumericCellValue())
                .m03Amt(row.getCell(17).getNumericCellValue())
                .m04Amt(row.getCell(18).getNumericCellValue())
                .m05Amt(row.getCell(19).getNumericCellValue())
                .m06Amt(row.getCell(20).getNumericCellValue())
                .m07Amt(row.getCell(21).getNumericCellValue())
                .m08Amt(row.getCell(22).getNumericCellValue())
                .m09Amt(row.getCell(23).getNumericCellValue())
                .m10Amt(row.getCell(24).getNumericCellValue())
                .m11Amt(row.getCell(25).getNumericCellValue())
                .m12Amt(row.getCell(26).getNumericCellValue())
                .build();
    }

    public UniBudgetExcelRow parseToUniBudgetRow(Row row) {
        return UniBudgetExcelRow.builder()
                .section(row.getCell(0).getStringCellValue())
                .subsection(row.getCell(1).getStringCellValue())
                .kcsr(row.getCell(2).getStringCellValue())
                .kcsrTitle(row.getCell(3).getStringCellValue())
                .dopKr(row.getCell(4).getStringCellValue())
                .dopKrTitle(row.getCell(5).getStringCellValue())
                .kvr(row.getCell(6).getStringCellValue())
                .kvrTitle(row.getCell(7).getStringCellValue())
                .kosgu(row.getCell(8).getStringCellValue())
                .kosguTitle(row.getCell(9).getStringCellValue())
                .kvsr(row.getCell(10).getStringCellValue())
                .kvsrTitle(row.getCell(11).getStringCellValue())
                .dopFk(row.getCell(12).getStringCellValue())
                .dopFrTitle(row.getCell(13).getStringCellValue())
                .dopEk(row.getCell(14).getStringCellValue())
                .dopEkTitle(row.getCell(15).getStringCellValue())
                .purpose(row.getCell(16).getStringCellValue())
                .purposeTitle(row.getCell(17).getStringCellValue())
                .assignTotal(row.getCell(18).getNumericCellValue())
                .assignFederal(row.getCell(19).getNumericCellValue())
                .assignRegional(row.getCell(20).getNumericCellValue())
                .financeTotal(row.getCell(21).getNumericCellValue())
                .requested(row.getCell(22).getNumericCellValue())
                .m01Amt(row.getCell(23).getNumericCellValue())
                .m02Amt(row.getCell(24).getNumericCellValue())
                .m03Amt(row.getCell(25).getNumericCellValue())
                .m04Amt(row.getCell(26).getNumericCellValue())
                .m05Amt(row.getCell(27).getNumericCellValue())
                .m06Amt(row.getCell(28).getNumericCellValue())
                .m07Amt(row.getCell(29).getNumericCellValue())
                .m08Amt(row.getCell(30).getNumericCellValue())
                .m09Amt(row.getCell(31).getNumericCellValue())
                .m10Amt(row.getCell(32).getNumericCellValue())
                .m11Amt(row.getCell(33).getNumericCellValue())
                .m12Amt(row.getCell(34).getNumericCellValue())
                .financeFederal(row.getCell(35).getNumericCellValue())
                .financeRegional(row.getCell(36).getNumericCellValue())
                .build();
    }

    public CofinancingLevelExcelRow toCofinancingLevelRow(Row row) {
        return CofinancingLevelExcelRow.builder()
                .kcsr(row.getCell(4).getStringCellValue())
                .dopKr(row.getCell(6).getStringCellValue())
                .kosgu(row.getCell(10).getStringCellValue())
                .obCoeff(row.getCell(22).getNumericCellValue())
                .fbCoeff(row.getCell(23).getNumericCellValue())
                .build();
    }
}
