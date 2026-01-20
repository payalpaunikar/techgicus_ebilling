package com.example.techgicus_ebilling.techgicus_ebilling.imports.sheet;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportContext;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.processor.TransactionProcessor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ExcelUtil.getCellString;
import static com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ExcelUtil.isRowEmpty;

@Service
public class TransactionSheetProcessor {

    private static final Logger log = LoggerFactory.getLogger(TransactionSheetProcessor.class);
    // A list of all available TransactionProcessor implementations.
// Each processor handles a specific transaction type (SALE, REFUND, etc.).
// At runtime, we pick the correct processor from this list based on the type of transaction.
// Using a list allows easy extension: to add a new transaction type, just add a new processor class.
    // Holds all transaction processors. The right processor is selected at runtime based on transaction type.
    private final List<TransactionProcessor> processors;

    public TransactionSheetProcessor(List<TransactionProcessor> processors) {
        this.processors = processors;
    }

    public int process(
            Sheet sheet,
            Company company,
            int headerRow,
            int transactionTypeColumnIndex,
            ImportContext context
    ) {

        int count = 0;

        String sheetName =  sheet.getSheetName().toLowerCase().trim();

        for (int r = headerRow + 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null || isRowEmpty(row)) continue;

            String cellType = getCellString(row.getCell(transactionTypeColumnIndex));
            if (cellType == null || cellType.trim().isEmpty()) continue;

            String transactionKind = sheetName.toLowerCase().contains("items details")
                    ? "SALE-ITEM"
                    : "SALE";

            log.info("Processing row {} | sheet '{}' | cellType '{}' → kind '{}'",
                    r, sheetName, cellType, transactionKind);

            TransactionProcessor processor = processors.stream()
                    .filter(p -> p.supports(transactionKind))
                    .findFirst()
                    .orElse(null);

            if (processor != null) {
                processor.process(row, company, context);
                count++;
            } else {
                log.warn("No processor for kind '{}' on row {}", transactionKind, r);
                context.addError(r + 1, "No processor found for transaction kind: " + transactionKind);
            }
        }
        return count+headerRow+1;
    }
}

