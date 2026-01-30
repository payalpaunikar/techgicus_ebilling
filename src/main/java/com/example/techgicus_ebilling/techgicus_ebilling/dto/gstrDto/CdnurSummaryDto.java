package com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CdnurSummaryDto {
    private int noOfNotes;
    private BigDecimal totalNoteValue;
    private BigDecimal totalTaxableValue;
    private BigDecimal totalCess;

    // Detailed list
    private List<CdnurNoteDto> notes = new ArrayList<>();

    public int getNoOfNotes() {
        return noOfNotes;
    }

    public void setNoOfNotes(int noOfNotes) {
        this.noOfNotes = noOfNotes;
    }

    public BigDecimal getTotalNoteValue() {
        return totalNoteValue;
    }

    public void setTotalNoteValue(BigDecimal totalNoteValue) {
        this.totalNoteValue = totalNoteValue;
    }

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

    public List<CdnurNoteDto> getNotes() {
        return notes;
    }

    public void setNotes(List<CdnurNoteDto> notes) {
        this.notes = notes;
    }
}
