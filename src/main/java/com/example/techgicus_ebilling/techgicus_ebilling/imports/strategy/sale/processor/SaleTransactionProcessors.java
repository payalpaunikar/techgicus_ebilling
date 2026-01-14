package com.example.techgicus_ebilling.techgicus_ebilling.imports.strategy.sale.processor;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.SaleRowData;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.strategy.TransactionProcessors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import static com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ExcelUtil.*;
import static com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ExcelUtil.getCellString;

@Service
public class SaleTransactionProcessors implements TransactionProcessors {
    @Override
    public boolean supports(String transactionType) {
        return false;
    }

    @Override
    public void process(Row row, Company company) {

        SaleRowData data = extractRowData(row);
    }


    private SaleRowData extractRowData(Row row) {

        SaleRowData data = new SaleRowData();

        data.setInvoiceDate(getCellLocalDate(row.getCell(0)));
        data.setInvoiceNo(getCellString(row.getCell(2)));
        data.setTotalAmount(getCellDouble(row.getCell(6)));
        data.setPaymentType(parsePaymentType(row.getCell(7)));
        data.setReceivedAmount(getCellDouble(row.getCell(8)));
        data.setBalance(getCellDouble(row.getCell(9)));
        data.setDueDate(getCellLocalDate(row.getCell(10)));
        data.setDescription(getCellString(row.getCell(12)));

        String paidValue = getCellString(row.getCell(11));
        data.setPaid("paid".equalsIgnoreCase(paidValue));

        return data;
    }

    private PaymentType parsePaymentType(Cell cell) {
        try {
            return PaymentType.valueOf(getCellString(cell).trim().toUpperCase());
        } catch (Exception e) {
            return PaymentType.CASH; // default or handle as needed
        }
    }
}
