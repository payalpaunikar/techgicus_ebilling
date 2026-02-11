package com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GstrDto {

    // From main sheet (GSTR1 Report)
    private String period;                  // e.g., "March 2023 - March 2023"
    private String gstin;                   // e.g., "27BJIPD7892R1Z7"
    private String legalName;               // e.g., "Karuna Enterprises"
   // private String tradeName;               // Can be empty
//    private BigDecimal aggregateTurnoverPrevFy;  // Aggregate turnover of preceding FY
//    private BigDecimal aggregateTurnoverAprJun2017;  // Aggregate turnover April to June 2017

    // List of invoices (from main sheet row 11+)
    private List<InvoiceDetailDto> invoices = new ArrayList<>();

    // Totals from main sheet
    private BigDecimal totalInvoiceValue;
    private BigDecimal totalTaxableValue;
    private BigDecimal totalIntegratedTax;
    private BigDecimal totalCentralTax;
    private BigDecimal totalStateUtTax;
    private BigDecimal totalCess;

//    // From sheet "b2b,sez,de" (B2B, SEZ, DE summary)
//    private B2bSummaryDto b2bSummary = new B2bSummaryDto();
//
//    // From sheet "b2cl" (B2CL summary - large B2C interstate)
//    private B2clSummaryDto b2clSummary = new B2clSummaryDto();
//
//    // From sheet "b2cs" (B2CS summary - small B2C)
//    private B2csSummaryDto b2csSummary = new B2csSummaryDto();
//
//    // From sheet "cdnr" (Credit/Debit notes registered)
//    private CdnrSummaryDto cdnrSummary = new CdnrSummaryDto();
//
//    // From sheet "cdnur" (Credit/Debit notes unregistered)
//    private CdnurSummaryDto cdnurSummary = new CdnurSummaryDto();
//
//    // From sheet "exp" (Exports)
//    private ExpSummaryDto expSummary = new ExpSummaryDto();
//
//    // From sheet "at" (Advances received)
//    private AtSummaryDto atSummary = new AtSummaryDto();
//
//    // From sheet "atadj" (Advances adjusted)
//    private AtadjSummaryDto atadjSummary = new AtadjSummaryDto();
//
//    // From sheet "exemp" (Nil/Exempt/Non-GST)
//    private ExempSummaryDto exempSummary = new ExempSummaryDto();
//
//    // From sheet "hsn" (HSN summary)
//    private HsnSummaryDto hsnSummary = new HsnSummaryDto();
//
//    // From sheet "itemSummary" (Detailed item HSN - similar to hsn but with descriptions)
//    private ItemSummaryDto itemSummary = new ItemSummaryDto();
//
//    // From sheet "docs" (Document issue summary)
//    private DocsSummaryDto docsSummary = new DocsSummaryDto();

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

//    public String getTradeName() {
//        return tradeName;
//    }
//
//    public void setTradeName(String tradeName) {
//        this.tradeName = tradeName;
//    }

//    public BigDecimal getAggregateTurnoverPrevFy() {
//        return aggregateTurnoverPrevFy;
//    }
//
//    public void setAggregateTurnoverPrevFy(BigDecimal aggregateTurnoverPrevFy) {
//        this.aggregateTurnoverPrevFy = aggregateTurnoverPrevFy;
//    }
//
//    public BigDecimal getAggregateTurnoverAprJun2017() {
//        return aggregateTurnoverAprJun2017;
//    }
//
//    public void setAggregateTurnoverAprJun2017(BigDecimal aggregateTurnoverAprJun2017) {
//        this.aggregateTurnoverAprJun2017 = aggregateTurnoverAprJun2017;
//    }

    public List<InvoiceDetailDto> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<InvoiceDetailDto> invoices) {
        this.invoices = invoices;
    }

    public BigDecimal getTotalInvoiceValue() {
        return totalInvoiceValue;
    }

    public void setTotalInvoiceValue(BigDecimal totalInvoiceValue) {
        this.totalInvoiceValue = totalInvoiceValue;
    }

    public BigDecimal getTotalTaxableValue() {
        return totalTaxableValue;
    }

    public void setTotalTaxableValue(BigDecimal totalTaxableValue) {
        this.totalTaxableValue = totalTaxableValue;
    }

    public BigDecimal getTotalIntegratedTax() {
        return totalIntegratedTax;
    }

    public void setTotalIntegratedTax(BigDecimal totalIntegratedTax) {
        this.totalIntegratedTax = totalIntegratedTax;
    }

    public BigDecimal getTotalCentralTax() {
        return totalCentralTax;
    }

    public void setTotalCentralTax(BigDecimal totalCentralTax) {
        this.totalCentralTax = totalCentralTax;
    }

    public BigDecimal getTotalStateUtTax() {
        return totalStateUtTax;
    }

    public void setTotalStateUtTax(BigDecimal totalStateUtTax) {
        this.totalStateUtTax = totalStateUtTax;
    }

    public BigDecimal getTotalCess() {
        return totalCess;
    }

    public void setTotalCess(BigDecimal totalCess) {
        this.totalCess = totalCess;
    }

//    public B2bSummaryDto getB2bSummary() {
//        return b2bSummary;
//    }
//
//    public void setB2bSummary(B2bSummaryDto b2bSummary) {
//        this.b2bSummary = b2bSummary;
//    }
//
//    public B2clSummaryDto getB2clSummary() {
//        return b2clSummary;
//    }
//
//    public void setB2clSummary(B2clSummaryDto b2clSummary) {
//        this.b2clSummary = b2clSummary;
//    }
//
//    public B2csSummaryDto getB2csSummary() {
//        return b2csSummary;
//    }
//
//    public void setB2csSummary(B2csSummaryDto b2csSummary) {
//        this.b2csSummary = b2csSummary;
//    }
//
//    public CdnrSummaryDto getCdnrSummary() {
//        return cdnrSummary;
//    }
//
//    public void setCdnrSummary(CdnrSummaryDto cdnrSummary) {
//        this.cdnrSummary = cdnrSummary;
//    }
//
//    public CdnurSummaryDto getCdnurSummary() {
//        return cdnurSummary;
//    }
//
//    public void setCdnurSummary(CdnurSummaryDto cdnurSummary) {
//        this.cdnurSummary = cdnurSummary;
//    }
//
//    public ExpSummaryDto getExpSummary() {
//        return expSummary;
//    }
//
//    public void setExpSummary(ExpSummaryDto expSummary) {
//        this.expSummary = expSummary;
//    }
//
//    public AtSummaryDto getAtSummary() {
//        return atSummary;
//    }
//
//    public void setAtSummary(AtSummaryDto atSummary) {
//        this.atSummary = atSummary;
//    }
//
//    public AtadjSummaryDto getAtadjSummary() {
//        return atadjSummary;
//    }
//
//    public void setAtadjSummary(AtadjSummaryDto atadjSummary) {
//        this.atadjSummary = atadjSummary;
//    }
//
//    public ExempSummaryDto getExempSummary() {
//        return exempSummary;
//    }
//
//    public void setExempSummary(ExempSummaryDto exempSummary) {
//        this.exempSummary = exempSummary;
//    }
//
//    public HsnSummaryDto getHsnSummary() {
//        return hsnSummary;
//    }
//
//    public void setHsnSummary(HsnSummaryDto hsnSummary) {
//        this.hsnSummary = hsnSummary;
//    }
//
//    public ItemSummaryDto getItemSummary() {
//        return itemSummary;
//    }
//
//    public void setItemSummary(ItemSummaryDto itemSummary) {
//        this.itemSummary = itemSummary;
//    }
//
//    public DocsSummaryDto getDocsSummary() {
//        return docsSummary;
//    }
//
//    public void setDocsSummary(DocsSummaryDto docsSummary) {
//        this.docsSummary = docsSummary;
//    }
}
