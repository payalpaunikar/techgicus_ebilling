package com.example.techgicus_ebilling.techgicus_ebilling.imports.extractor;

import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.SaleRowData;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import static com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ExcelUtil.*;

@Component
public class SaleRowExtractor implements RowExtractor<SaleRowData> {

    @Override
    public SaleRowData extract(Row row) {
        SaleRowData data = new SaleRowData();

        data.setInvoiceDate(getCellLocalDate(row.getCell(0)));
        data.setInvoiceNo(getCellString(row.getCell(2)));
        data.setPartyName(getCellString(row.getCell(3)));
        data.setPartyPhone(getCellString(row.getCell(4)));
        data.setTotalAmount(getCellDouble(row.getCell(6)));
        data.setPaymentType(parsePaymentType(row.getCell(7)));
        data.setReceivedAmount(getCellDouble(row.getCell(8)));
        data.setBalance(getCellDouble(row.getCell(9)));
        data.setDueDate(getCellLocalDate(row.getCell(10)));
        data.setDescription(getCellString(row.getCell(12)));

        String status = getCellString(row.getCell(11));
        data.setPaid("paid".equalsIgnoreCase(status));

        return data;
    }
}

