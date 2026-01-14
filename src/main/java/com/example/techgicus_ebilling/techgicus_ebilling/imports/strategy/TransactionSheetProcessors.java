package com.example.techgicus_ebilling.techgicus_ebilling.imports.strategy;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ExcelUtil.getCellString;
import static com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ExcelUtil.isRowEmpty;

@Service
public class TransactionSheetProcessors {

    private final List<TransactionProcessors> processors;

    public TransactionSheetProcessors(List<TransactionProcessors> processors) {
        this.processors = processors;
    }

    public int process(Sheet sheet, Company company, int headerRow,int typeColumnIndex ) {

        int count = 0;

        for (int r = headerRow + 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null || isRowEmpty(row)) continue;

            String type = getCellString(row.getCell(typeColumnIndex))
                    .toLowerCase()
                    .trim();

            TransactionProcessors processor = processors.stream()
                    .filter(p -> p.supports(type))
                    .findFirst()
                    .orElse(null);

            if (processor != null) {
                processor.process(row, company);
                count++;
            }
        }
        return count;
    }
}
