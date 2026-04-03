package su.petrosoft.apk_ack_integration.model.data.xml.rpl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.math.BigDecimal;

public record PlDirectionLine(
        @JacksonXmlProperty(isAttribute = true, localName = "ID") Long id,
        @JacksonXmlProperty(isAttribute = true, localName = "VERSION") Integer version,
        @JacksonXmlProperty(isAttribute = true, localName = "DOCPART_KEY") Long docpartKey,
        @JacksonXmlProperty(isAttribute = true, localName = "DIRECTION_ID") Long directionId,
        @JacksonXmlProperty(isAttribute = true, localName = "DIRECTION_DATA") String directionData,

        @JacksonXmlProperty(isAttribute = true, localName = "ASSIGN_AMT1") BigDecimal assignAmt1,
        @JacksonXmlProperty(isAttribute = true, localName = "ASSIGN_AMT2") BigDecimal assignAmt2,
        @JacksonXmlProperty(isAttribute = true, localName = "ASSIGN_AMT3") BigDecimal assignAmt3,

        @JacksonXmlProperty(isAttribute = true, localName = "LIMIT_AMT1") BigDecimal limitAmt1,
        @JacksonXmlProperty(isAttribute = true, localName = "LIMIT_AMT2") BigDecimal limitAmt2,
        @JacksonXmlProperty(isAttribute = true, localName = "LIMIT_AMT3") BigDecimal limitAmt3,

        @JacksonXmlProperty(isAttribute = true, localName = "ASSIGN_FEDERAL_AMT1") BigDecimal assignFederalAmt1,
        @JacksonXmlProperty(isAttribute = true, localName = "ASSIGN_FEDERAL_AMT2") BigDecimal assignFederalAmt2,
        @JacksonXmlProperty(isAttribute = true, localName = "ASSIGN_FEDERAL_AMT3") BigDecimal assignFederalAmt3,

        @JacksonXmlProperty(isAttribute = true, localName = "ASSIGN_REGIONAL_AMT1") BigDecimal assignRegionalAmt1,
        @JacksonXmlProperty(isAttribute = true, localName = "ASSIGN_REGIONAL_AMT2") BigDecimal assignRegionalAmt2,
        @JacksonXmlProperty(isAttribute = true, localName = "ASSIGN_REGIONAL_AMT3") BigDecimal assignRegionalAmt3,

        @JacksonXmlProperty(isAttribute = true, localName = "ASSIGN_MUNICIPAL_AMT1") BigDecimal assignMunicipalAmt1,
        @JacksonXmlProperty(isAttribute = true, localName = "ASSIGN_MUNICIPAL_AMT2") BigDecimal assignMunicipalAmt2,
        @JacksonXmlProperty(isAttribute = true, localName = "ASSIGN_MUNICIPAL_AMT3") BigDecimal assignMunicipalAmt3,

        @JacksonXmlProperty(isAttribute = true, localName = "ASSIGN_SETTLEMENT_AMT1") BigDecimal assignSettlementAmt1,
        @JacksonXmlProperty(isAttribute = true, localName = "ASSIGN_SETTLEMENT_AMT2") BigDecimal assignSettlementAmt2,
        @JacksonXmlProperty(isAttribute = true, localName = "ASSIGN_SETTLEMENT_AMT3") BigDecimal assignSettlementAmt3,

        @JacksonXmlProperty(isAttribute = true, localName = "LIMIT_FEDERAL_AMT1") BigDecimal limitFederalAmt1,
        @JacksonXmlProperty(isAttribute = true, localName = "LIMIT_FEDERAL_AMT2") BigDecimal limitFederalAmt2,
        @JacksonXmlProperty(isAttribute = true, localName = "LIMIT_FEDERAL_AMT3") BigDecimal limitFederalAmt3,

        @JacksonXmlProperty(isAttribute = true, localName = "LIMIT_REGIONAL_AMT1") BigDecimal limitRegionalAmt1,
        @JacksonXmlProperty(isAttribute = true, localName = "LIMIT_REGIONAL_AMT2") BigDecimal limitRegionalAmt2,
        @JacksonXmlProperty(isAttribute = true, localName = "LIMIT_REGIONAL_AMT3") BigDecimal limitRegionalAmt3,

        @JacksonXmlProperty(isAttribute = true, localName = "LIMIT_MUNICIPAL_AMT1") BigDecimal limitMunicipalAmt1,
        @JacksonXmlProperty(isAttribute = true, localName = "LIMIT_MUNICIPAL_AMT2") BigDecimal limitMunicipalAmt2,
        @JacksonXmlProperty(isAttribute = true, localName = "LIMIT_MUNICIPAL_AMT3") BigDecimal limitMunicipalAmt3,

        @JacksonXmlProperty(isAttribute = true, localName = "LIMIT_SETTLEMENT_AMT1") BigDecimal limitSettlementAmt1,
        @JacksonXmlProperty(isAttribute = true, localName = "LIMIT_SETTLEMENT_AMT2") BigDecimal limitSettlementAmt2,
        @JacksonXmlProperty(isAttribute = true, localName = "LIMIT_SETTLEMENT_AMT3") BigDecimal limitSettlementAmt3,

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
        @JacksonXmlProperty(isAttribute = true, localName = "M_SETTLEMENT_12_AMT") BigDecimal mSettlement12Amt
) {
}