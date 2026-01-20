package com.example.techgicus_ebilling.techgicus_ebilling.imports.context;

import java.util.ArrayList;
import java.util.List;

public class ImportContext {

    private final List<ImportError> errors = new ArrayList<>();

    public void addError(int rowNumber, String message) {
        errors.add(new ImportError(rowNumber, message));
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<ImportError> getErrors() {
        return errors;
    }
}

