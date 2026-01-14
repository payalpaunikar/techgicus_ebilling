package com.example.techgicus_ebilling.techgicus_ebilling.imports.sheet;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.processor.TransactionProcessor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ExcelUtil.getCellString;
import static com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ExcelUtil.isRowEmpty;

@Service
public class TransactionSheetProcessor {

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
            int typeColumnIndex
    ) {

        int count = 0;

        for (int r = headerRow + 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null || isRowEmpty(row)) continue;

            String type = getCellString(row.getCell(typeColumnIndex));
            if (type == null) continue;


            // what here we do is that upcasting we created the refernce variable of the
            // parent and created object of child
            // using this we called child method with parent refrence vaiable
            // here we pick the child class object according type of the transaction
            TransactionProcessor processor = processors.stream()
                    .filter(p -> p.supports(type)) // returns true/false
                    .findFirst()//returns the child object itself,
                    .orElse(null); // or null if none matches

            // check if type is null then process is null
            // if not null then we called method.
            //so, here  Polymorphism occurs when you call a method on the parent reference and the child’s implementation executes at runtime.
            if (processor != null) {
                processor.process(row, company);
                count++;
            }
        }
        return count;
    }
}

