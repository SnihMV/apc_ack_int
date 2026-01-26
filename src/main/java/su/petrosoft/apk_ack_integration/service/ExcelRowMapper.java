package su.petrosoft.apk_ack_integration.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;
import su.petrosoft.apk_ack_integration.model.data.DescriptedBudgetItemData;
import su.petrosoft.apk_ack_integration.model.data.excel.CofinancingLevelExcelRow;
import su.petrosoft.apk_ack_integration.model.data.excel.RosterKbkItemExcelRow;
import su.petrosoft.apk_ack_integration.model.data.excel.UniBudget2026ClarifiedItemExcelRow;
import su.petrosoft.apk_ack_integration.model.data.excel.UniBudgetCodedItemExcelRow;
import su.petrosoft.apk_ack_integration.model.data.excel.UniBudgetItemExcelRow;

@Slf4j
@Component
public class ExcelRowMapper {

    public UniBudget2026ClarifiedItemExcelRow parseToUniBudget2026ClarifiedRow(Row row) {
        return new UniBudget2026ClarifiedItemExcelRow(
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
            row.getCell(22).getStringCellValue(),
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
            row.getCell(40).getNumericCellValue(),
            row.getCell(41).getNumericCellValue()
        );
    }

    public UniBudgetCodedItemExcelRow parseToUniBudgetCodedRow(Row row) {
        return new UniBudgetCodedItemExcelRow(
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

    public RosterKbkItemExcelRow parseToRosterKbkRow(Row row) {
        return RosterKbkItemExcelRow.builder()
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
                .janLimit(row.getCell(15).getNumericCellValue())
                .febLimit(row.getCell(16).getNumericCellValue())
                .marLimit(row.getCell(17).getNumericCellValue())
                .aprLimit(row.getCell(18).getNumericCellValue())
                .mayLimit(row.getCell(19).getNumericCellValue())
                .junLimit(row.getCell(20).getNumericCellValue())
                .julLimit(row.getCell(21).getNumericCellValue())
                .augLimit(row.getCell(22).getNumericCellValue())
                .sepLimit(row.getCell(23).getNumericCellValue())
                .octLimit(row.getCell(24).getNumericCellValue())
                .novLimit(row.getCell(25).getNumericCellValue())
                .decLimit(row.getCell(26).getNumericCellValue())
                .build();
    }

    public UniBudgetItemExcelRow parseToUniBudgetRow(Row row) {
        return UniBudgetItemExcelRow.builder()
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
                .dopFkTitle(row.getCell(13).getStringCellValue())
                .dopEk(row.getCell(14).getStringCellValue())
                .dopEkTitle(row.getCell(15).getStringCellValue())
                .purpose(row.getCell(16).getStringCellValue())
                .purposeTitle(row.getCell(17).getStringCellValue())
                .assignTotal(row.getCell(18).getNumericCellValue())
                .assignFederal(row.getCell(19).getNumericCellValue())
                .assignRegional(row.getCell(20).getNumericCellValue())
                .financeTotal(row.getCell(21).getNumericCellValue())
                .requested(row.getCell(22).getNumericCellValue())
                .janLimit(row.getCell(23).getNumericCellValue())
                .febLimit(row.getCell(24).getNumericCellValue())
                .marLimit(row.getCell(25).getNumericCellValue())
                .aprLimit(row.getCell(26).getNumericCellValue())
                .mayLimit(row.getCell(27).getNumericCellValue())
                .junLimit(row.getCell(28).getNumericCellValue())
                .julLimit(row.getCell(29).getNumericCellValue())
                .augLimit(row.getCell(30).getNumericCellValue())
                .sepLimit(row.getCell(31).getNumericCellValue())
                .octLimit(row.getCell(32).getNumericCellValue())
                .novLimit(row.getCell(33).getNumericCellValue())
                .decLimit(row.getCell(34).getNumericCellValue())
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
