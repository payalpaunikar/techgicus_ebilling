package com.example.techgicus_ebilling.techgicus_ebilling.imports.extractor;


import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.PurchaseItemRow;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import static com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ExcelUtil.*;

@Component
public class PurchaseItemRowExtractor implements RowExtractor<PurchaseItemRow>{

    @Override
    public PurchaseItemRow extract(Row row) {

        PurchaseItemRow data = new PurchaseItemRow();
        data.setBillDate(getCellLocalDate(row.getCell(0)));
        data.setBillNo(getCellString(row.getCell(1)));
        data.setPartyName(getCellString(row.getCell(2)));
        data.setItemName(getCellString(row.getCell(3)));
        data.setItemCode(getCellString(row.getCell(4)));
        data.setHsn(getCellString(row.getCell(5)));
        data.setCategory(getCellString(row.getCell(6)));
        data.setOrderNo(getCellString(row.getCell(7)));
        data.setQuantity(getCellDouble(row.getCell(8)));
        data.setUnit(parseUnit(row.getCell(9)));
        data.setUnitPrice(getCellDouble(row.getCell(10)));
        data.setDiscountPercentage(getCellInteger(row.getCell(11)));
        data.setDiscountPrice(getCellDouble(row.getCell(12)));
        data.setTaxPercentage(parseTaxRate(row.getCell(13)));
        data.setTaxPrice(getCellDouble(row.getCell(14)));
        data.setTransactionType(getCellString(row.getCell(15)));
        data.setAmount(getCellDouble(row.getCell(16)));

        return data;
    }
}
