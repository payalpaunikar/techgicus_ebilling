package com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AtSummaryDto {
    private BigDecimal totalAdvanceReceived;
    private BigDecimal totalCess;

    // Detailed list
    private List<AtDetailDto> details = new ArrayList<>();

    public BigDecimal getTotalAdvanceReceived() {
        return totalAdvanceReceived;
    }

    public void setTotalAdvanceReceived(BigDecimal totalAdvanceReceived) {
        this.totalAdvanceReceived = totalAdvanceReceived;
    }

    public BigDecimal getTotalCess() {
        return totalCess;
    }

    public void setTotalCess(BigDecimal totalCess) {
        this.totalCess = totalCess;
    }

    public List<AtDetailDto> getDetails() {
        return details;
    }

    public void setDetails(List<AtDetailDto> details) {
        this.details = details;
    }
}
