package com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto;

import java.util.ArrayList;
import java.util.List;

public class DocsSummaryDto {
    private int totalNumber;
    private int totalCancelled;

    // Detailed document types
    private List<DocTypeDetailDto> docTypes = new ArrayList<>();

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getTotalCancelled() {
        return totalCancelled;
    }

    public void setTotalCancelled(int totalCancelled) {
        this.totalCancelled = totalCancelled;
    }

    public List<DocTypeDetailDto> getDocTypes() {
        return docTypes;
    }

    public void setDocTypes(List<DocTypeDetailDto> docTypes) {
        this.docTypes = docTypes;
    }
}
