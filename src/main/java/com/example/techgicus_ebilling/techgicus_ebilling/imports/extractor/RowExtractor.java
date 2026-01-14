package com.example.techgicus_ebilling.techgicus_ebilling.imports.extractor;

import org.apache.poi.ss.usermodel.Row;

public interface RowExtractor<T> {
    /**
     * Convert Excel row into structured data object
     */
    T extract(Row row);
}
