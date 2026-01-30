package com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto;

public class DocTypeDetailDto {
    private String natureOfDocument;    // e.g., "Invoices for outward supply"
    private String srNoFrom;            // e.g., "315"
    private String srNoTo;              // e.g., "319"
    private int totalNumber;            // e.g., 5
    private int cancelled;              // e.g., 0

    public String getNatureOfDocument() {
        return natureOfDocument;
    }

    public void setNatureOfDocument(String natureOfDocument) {
        this.natureOfDocument = natureOfDocument;
    }

    public String getSrNoFrom() {
        return srNoFrom;
    }

    public void setSrNoFrom(String srNoFrom) {
        this.srNoFrom = srNoFrom;
    }

    public String getSrNoTo() {
        return srNoTo;
    }

    public void setSrNoTo(String srNoTo) {
        this.srNoTo = srNoTo;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getCancelled() {
        return cancelled;
    }

    public void setCancelled(int cancelled) {
        this.cancelled = cancelled;
    }
}
