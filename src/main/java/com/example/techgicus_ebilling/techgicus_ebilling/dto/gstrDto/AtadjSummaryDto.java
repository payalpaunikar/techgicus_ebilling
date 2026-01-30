package com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AtadjSummaryDto {
    private BigDecimal totalAdvanceAdjusted;
    private BigDecimal totalCess;

    // Detailed list
    private List<AtadjDetailDto> details = new ArrayList<>();

    public BigDecimal getTotalAdvanceAdjusted() {
        return totalAdvanceAdjusted;
    }

    public void setTotalAdvanceAdjusted(BigDecimal totalAdvanceAdjusted) {
        this.totalAdvanceAdjusted = totalAdvanceAdjusted;
    }

    public BigDecimal getTotalCess() {
        return totalCess;
    }

    public void setTotalCess(BigDecimal totalCess) {
        this.totalCess = totalCess;
    }

    public List<AtadjDetailDto> getDetails() {
        return details;
    }

    public void setDetails(List<AtadjDetailDto> details) {
        this.details = details;
    }
}
