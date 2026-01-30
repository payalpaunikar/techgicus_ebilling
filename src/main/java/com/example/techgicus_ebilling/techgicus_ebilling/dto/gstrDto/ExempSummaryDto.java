package com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto;

import java.math.BigDecimal;

public class ExempSummaryDto {
    private BigDecimal totalNilRatedSupplies;
    private BigDecimal totalExemptedSupplies;
    private BigDecimal totalNonGstSupplies;

    // Detailed breakdowns
    private BigDecimal interStateRegisteredNil;
    private BigDecimal interStateRegisteredExempt;
    private BigDecimal interStateRegisteredNonGst;

    private BigDecimal intraStateRegisteredNil;
    private BigDecimal intraStateRegisteredExempt;
    private BigDecimal intraStateRegisteredNonGst;

    private BigDecimal interStateUnregisteredNil;
    private BigDecimal interStateUnregisteredExempt;
    private BigDecimal interStateUnregisteredNonGst;

    private BigDecimal intraStateUnregisteredNil;
    private BigDecimal intraStateUnregisteredExempt;
    private BigDecimal intraStateUnregisteredNonGst;

    public BigDecimal getTotalNilRatedSupplies() {
        return totalNilRatedSupplies;
    }

    public void setTotalNilRatedSupplies(BigDecimal totalNilRatedSupplies) {
        this.totalNilRatedSupplies = totalNilRatedSupplies;
    }

    public BigDecimal getTotalExemptedSupplies() {
        return totalExemptedSupplies;
    }

    public void setTotalExemptedSupplies(BigDecimal totalExemptedSupplies) {
        this.totalExemptedSupplies = totalExemptedSupplies;
    }

    public BigDecimal getTotalNonGstSupplies() {
        return totalNonGstSupplies;
    }

    public void setTotalNonGstSupplies(BigDecimal totalNonGstSupplies) {
        this.totalNonGstSupplies = totalNonGstSupplies;
    }

    public BigDecimal getInterStateRegisteredNil() {
        return interStateRegisteredNil;
    }

    public void setInterStateRegisteredNil(BigDecimal interStateRegisteredNil) {
        this.interStateRegisteredNil = interStateRegisteredNil;
    }

    public BigDecimal getInterStateRegisteredExempt() {
        return interStateRegisteredExempt;
    }

    public void setInterStateRegisteredExempt(BigDecimal interStateRegisteredExempt) {
        this.interStateRegisteredExempt = interStateRegisteredExempt;
    }

    public BigDecimal getInterStateRegisteredNonGst() {
        return interStateRegisteredNonGst;
    }

    public void setInterStateRegisteredNonGst(BigDecimal interStateRegisteredNonGst) {
        this.interStateRegisteredNonGst = interStateRegisteredNonGst;
    }

    public BigDecimal getIntraStateRegisteredNil() {
        return intraStateRegisteredNil;
    }

    public void setIntraStateRegisteredNil(BigDecimal intraStateRegisteredNil) {
        this.intraStateRegisteredNil = intraStateRegisteredNil;
    }

    public BigDecimal getIntraStateRegisteredExempt() {
        return intraStateRegisteredExempt;
    }

    public void setIntraStateRegisteredExempt(BigDecimal intraStateRegisteredExempt) {
        this.intraStateRegisteredExempt = intraStateRegisteredExempt;
    }

    public BigDecimal getIntraStateRegisteredNonGst() {
        return intraStateRegisteredNonGst;
    }

    public void setIntraStateRegisteredNonGst(BigDecimal intraStateRegisteredNonGst) {
        this.intraStateRegisteredNonGst = intraStateRegisteredNonGst;
    }

    public BigDecimal getInterStateUnregisteredNil() {
        return interStateUnregisteredNil;
    }

    public void setInterStateUnregisteredNil(BigDecimal interStateUnregisteredNil) {
        this.interStateUnregisteredNil = interStateUnregisteredNil;
    }

    public BigDecimal getInterStateUnregisteredExempt() {
        return interStateUnregisteredExempt;
    }

    public void setInterStateUnregisteredExempt(BigDecimal interStateUnregisteredExempt) {
        this.interStateUnregisteredExempt = interStateUnregisteredExempt;
    }

    public BigDecimal getInterStateUnregisteredNonGst() {
        return interStateUnregisteredNonGst;
    }

    public void setInterStateUnregisteredNonGst(BigDecimal interStateUnregisteredNonGst) {
        this.interStateUnregisteredNonGst = interStateUnregisteredNonGst;
    }

    public BigDecimal getIntraStateUnregisteredNil() {
        return intraStateUnregisteredNil;
    }

    public void setIntraStateUnregisteredNil(BigDecimal intraStateUnregisteredNil) {
        this.intraStateUnregisteredNil = intraStateUnregisteredNil;
    }

    public BigDecimal getIntraStateUnregisteredExempt() {
        return intraStateUnregisteredExempt;
    }

    public void setIntraStateUnregisteredExempt(BigDecimal intraStateUnregisteredExempt) {
        this.intraStateUnregisteredExempt = intraStateUnregisteredExempt;
    }

    public BigDecimal getIntraStateUnregisteredNonGst() {
        return intraStateUnregisteredNonGst;
    }

    public void setIntraStateUnregisteredNonGst(BigDecimal intraStateUnregisteredNonGst) {
        this.intraStateUnregisteredNonGst = intraStateUnregisteredNonGst;
    }
}
