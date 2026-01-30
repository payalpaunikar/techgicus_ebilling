package com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class B2csSummaryDto {

    private BigDecimal totalTaxableValue;
    private BigDecimal totalCess;

    // Detailed summaries
    private List<B2csDetailDto> details = new ArrayList<>();

    public BigDecimal getTotalTaxableValue() {
        return totalTaxableValue;
    }

    public void setTotalTaxableValue(BigDecimal totalTaxableValue) {
        this.totalTaxableValue = totalTaxableValue;
    }

    public BigDecimal getTotalCess() {
        return totalCess;
    }

    public void setTotalCess(BigDecimal totalCess) {
        this.totalCess = totalCess;
    }

    public List<B2csDetailDto> getDetails() {
        return details;
    }

    public void setDetails(List<B2csDetailDto> details) {
        this.details = details;
    }
}
