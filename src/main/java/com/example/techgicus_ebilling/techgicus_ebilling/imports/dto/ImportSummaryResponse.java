package com.example.techgicus_ebilling.techgicus_ebilling.imports.dto;

import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportError;

import java.util.List;

public class ImportSummaryResponse {
    private String module;          // SALE / PURCHASE
    private int documentsProcessed;
    private int itemsProcessed;
    private boolean success;
    private List<ImportError> errors;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public int getDocumentsProcessed() {
        return documentsProcessed;
    }

    public void setDocumentsProcessed(int documentsProcessed) {
        this.documentsProcessed = documentsProcessed;
    }

    public int getItemsProcessed() {
        return itemsProcessed;
    }

    public void setItemsProcessed(int itemsProcessed) {
        this.itemsProcessed = itemsProcessed;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ImportError> getErrors() {
        return errors;
    }

    public void setErrors(List<ImportError> errors) {
        this.errors = errors;
    }
}
