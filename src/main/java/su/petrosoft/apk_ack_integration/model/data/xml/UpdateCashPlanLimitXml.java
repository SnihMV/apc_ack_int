package su.petrosoft.apk_ack_integration.model.data.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import su.petrosoft.apk_ack_integration.model.data.xml.rpl.PlDirectionLine;

import java.math.BigDecimal;
import java.time.LocalDate;

@JacksonXmlRootElement(localName = "CHANGE_EXPCASHPLANDOC")
public record UpdateCashPlanLimitXml(
        @JacksonXmlProperty(isAttribute = true, localName = "ID") Long id,
        @JacksonXmlProperty(isAttribute = true, localName = "ESTIMATE_ID") Long estimateId,
        @JacksonXmlProperty(isAttribute = true, localName = "RECIPIENT_ID") Long recipientId,
        @JacksonXmlProperty(isAttribute = true, localName = "LAWACT_ID") String lawactId,

        @JacksonXmlProperty(isAttribute = true, localName = "AMOUNT") BigDecimal amount,

        @JacksonXmlProperty(isAttribute = true, localName = "M_01_AMT") BigDecimal janLimit,
        @JacksonXmlProperty(isAttribute = true, localName = "M_02_AMT") BigDecimal febLimit,
        @JacksonXmlProperty(isAttribute = true, localName = "M_03_AMT") BigDecimal marLimit,
        @JacksonXmlProperty(isAttribute = true, localName = "M_04_AMT") BigDecimal aprLimit,
        @JacksonXmlProperty(isAttribute = true, localName = "M_05_AMT") BigDecimal mayLimit,
        @JacksonXmlProperty(isAttribute = true, localName = "M_06_AMT") BigDecimal junLimit,
        @JacksonXmlProperty(isAttribute = true, localName = "M_07_AMT") BigDecimal julLimit,
        @JacksonXmlProperty(isAttribute = true, localName = "M_08_AMT") BigDecimal augLimit,
        @JacksonXmlProperty(isAttribute = true, localName = "M_09_AMT") BigDecimal sepLimit,
        @JacksonXmlProperty(isAttribute = true, localName = "M_10_AMT") BigDecimal octLimit,
        @JacksonXmlProperty(isAttribute = true, localName = "M_11_AMT") BigDecimal novLimit,
        @JacksonXmlProperty(isAttribute = true, localName = "M_12_AMT") BigDecimal decLimit,

        @JacksonXmlProperty(isAttribute = true, localName = "ESTIMATE_CAPTION") String estimateCaption,
        @JacksonXmlProperty(isAttribute = true, localName = "RECIPIENT_CAPTION") String recipientCaption,

        @JacksonXmlProperty(isAttribute = true, localName = "M_FEDERAL_01_AMT") BigDecimal mFederal01Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_FEDERAL_02_AMT") BigDecimal mFederal02Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_FEDERAL_03_AMT") BigDecimal mFederal03Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_FEDERAL_04_AMT") BigDecimal mFederal04Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_FEDERAL_05_AMT") BigDecimal mFederal05Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_FEDERAL_06_AMT") BigDecimal mFederal06Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_FEDERAL_07_AMT") BigDecimal mFederal07Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_FEDERAL_08_AMT") BigDecimal mFederal08Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_FEDERAL_09_AMT") BigDecimal mFederal09Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_FEDERAL_10_AMT") BigDecimal mFederal10Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_FEDERAL_11_AMT") BigDecimal mFederal11Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_FEDERAL_12_AMT") BigDecimal mFederal12Amt,

        @JacksonXmlProperty(isAttribute = true, localName = "M_REGIONAL_01_AMT") BigDecimal mRegional01Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_REGIONAL_02_AMT") BigDecimal mRegional02Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_REGIONAL_03_AMT") BigDecimal mRegional03Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_REGIONAL_04_AMT") BigDecimal mRegional04Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_REGIONAL_05_AMT") BigDecimal mRegional05Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_REGIONAL_06_AMT") BigDecimal mRegional06Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_REGIONAL_07_AMT") BigDecimal mRegional07Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_REGIONAL_08_AMT") BigDecimal mRegional08Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_REGIONAL_09_AMT") BigDecimal mRegional09Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_REGIONAL_10_AMT") BigDecimal mRegional10Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_REGIONAL_11_AMT") BigDecimal mRegional11Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_REGIONAL_12_AMT") BigDecimal mRegional12Amt,

        @JacksonXmlProperty(isAttribute = true, localName = "M_MUNICIPAL_01_AMT") BigDecimal mMunicipal01Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_MUNICIPAL_02_AMT") BigDecimal mMunicipal02Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_MUNICIPAL_03_AMT") BigDecimal mMunicipal03Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_MUNICIPAL_04_AMT") BigDecimal mMunicipal04Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_MUNICIPAL_05_AMT") BigDecimal mMunicipal05Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_MUNICIPAL_06_AMT") BigDecimal mMunicipal06Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_MUNICIPAL_07_AMT") BigDecimal mMunicipal07Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_MUNICIPAL_08_AMT") BigDecimal mMunicipal08Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_MUNICIPAL_09_AMT") BigDecimal mMunicipal09Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_MUNICIPAL_10_AMT") BigDecimal mMunicipal10Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_MUNICIPAL_11_AMT") BigDecimal mMunicipal11Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_MUNICIPAL_12_AMT") BigDecimal mMunicipal12Amt,

        @JacksonXmlProperty(isAttribute = true, localName = "M_SETTLEMENT_01_AMT") BigDecimal mSettlement01Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_SETTLEMENT_02_AMT") BigDecimal mSettlement02Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_SETTLEMENT_03_AMT") BigDecimal mSettlement03Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_SETTLEMENT_04_AMT") BigDecimal mSettlement04Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_SETTLEMENT_05_AMT") BigDecimal mSettlement05Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_SETTLEMENT_06_AMT") BigDecimal mSettlement06Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_SETTLEMENT_07_AMT") BigDecimal mSettlement07Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_SETTLEMENT_08_AMT") BigDecimal mSettlement08Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_SETTLEMENT_09_AMT") BigDecimal mSettlement09Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_SETTLEMENT_10_AMT") BigDecimal mSettlement10Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_SETTLEMENT_11_AMT") BigDecimal mSettlement11Amt,
        @JacksonXmlProperty(isAttribute = true, localName = "M_SETTLEMENT_12_AMT") BigDecimal mSettlement12Amt,

        @JacksonXmlProperty(isAttribute = true, localName = "PURCHASE_SAVING") BigDecimal purchaseSaving,
        @JacksonXmlProperty(isAttribute = true, localName = "GRBS_INTERNAL_SHIFTING") BigDecimal grbsInternalShifting,
        @JacksonXmlProperty(isAttribute = true, localName = "MARGINAL_FUNDING_SHIFTING") BigDecimal marginalFundingShifting,
        @JacksonXmlProperty(isAttribute = true, localName = "BALANCE_TRANSFER") BigDecimal balanceTransfer,

        @JacksonXmlProperty(isAttribute = true, localName = "BEGIN_AGREEMENT_DATE") LocalDate beginAgreementDate,
        @JacksonXmlProperty(isAttribute = true, localName = "DOC_DATE") LocalDate docDate,
        @JacksonXmlProperty(isAttribute = true, localName = "CARRY_DATE") LocalDate carryDate,

        @JacksonXmlProperty(isAttribute = true, localName = "TERRITORY_RECIPIENT") String territoryRecipient,
        @JacksonXmlProperty(isAttribute = true, localName = "DESCRIPTION") String description,
        @JacksonXmlProperty(isAttribute = true, localName = "REMARK") String remark,

        @JacksonXmlProperty(isAttribute = true, localName = "KFSR_CODE") String kfsr,
        @JacksonXmlProperty(isAttribute = true, localName = "KCSR_CODE") String kcsr,
        @JacksonXmlProperty(isAttribute = true, localName = "KVR_CODE") String kvr,
        @JacksonXmlProperty(isAttribute = true, localName = "KESR_CODE") String kosgu,
        @JacksonXmlProperty(isAttribute = true, localName = "KADMR_CODE") String kvsr,
        @JacksonXmlProperty(isAttribute = true, localName = "KDF_CODE") String dopKf,
        @JacksonXmlProperty(isAttribute = true, localName = "KDE_CODE") String dopEk,
        @JacksonXmlProperty(isAttribute = true, localName = "KDR_CODE") String dopKr,
        @JacksonXmlProperty(isAttribute = true, localName = "PURPOSEFULGRANT_CODE") String purpose,
        @JacksonXmlProperty(isAttribute = true, localName = "RECIPIENT_INN") String recipientInn,
        @JacksonXmlProperty(isAttribute = true, localName = "RECIPIENT_KPP") String recipientKpp,

        @JacksonXmlProperty(isAttribute = true, localName = "PURPOSEFULGRANT_ID") Long purposefulgrantId,
        @JacksonXmlProperty(isAttribute = true, localName = "FSR_ID") Integer fsrId,
        @JacksonXmlProperty(isAttribute = true, localName = "OPERTYPE_ID") Integer operTypeId,
        @JacksonXmlProperty(isAttribute = true, localName = "DOCUMENT_ID") Long documentId,
        @JacksonXmlProperty(isAttribute = true, localName = "BUDGETCODESREF_DOC_ID") String budgetCodeSrefDocId,
        @JacksonXmlProperty(isAttribute = true, localName = "COPY_DOCUMENT_ID") Long copyDocumentId,

        @JacksonXmlProperty(isAttribute = true, localName = "DOC_NUMBER") Integer docNumber,
        @JacksonXmlProperty(isAttribute = true, localName = "PERIOD") Integer period,
        @JacksonXmlProperty(isAttribute = true, localName = "VERSION") Integer version,

        @JacksonXmlProperty(isAttribute = true, localName = "num_gen_type") String numGenType,

        @JacksonXmlProperty(localName = "LINES") Lines lines,
        @JacksonXmlProperty(localName = "EXPCASHPLANDOC") ExpCashPlanDoc expCashPlanDoc,
        @JacksonXmlProperty(localName = "EXPCASHPLANBOLINES") ExpCashPlanBoLines expCashPlanBoLines,
        @JacksonXmlProperty(localName = "EXPCASHPLANORDERDOC") ExpCashPlanOrderDoc expCashPlanOrderDoc,
        @JacksonXmlProperty(localName = "PL_DIRECTIONLINE") PlDirectionLineWrapper plDirectionLineWrapper
) {

    public record Lines() {
    }

    public record ExpCashPlanDoc() {
    }

    public record ExpCashPlanBoLines() {
    }

    public record ExpCashPlanOrderDoc() {
    }

    public record PlDirectionLineWrapper(
            @JacksonXmlProperty(localName = "PL_DIRECTIONLINE")
            PlDirectionLine plDirectionLine
    ) {}
}

