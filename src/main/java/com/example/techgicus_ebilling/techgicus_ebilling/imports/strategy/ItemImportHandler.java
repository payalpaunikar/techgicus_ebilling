package com.example.techgicus_ebilling.techgicus_ebilling.imports.strategy;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Category;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Item;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.DiscountType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.ItemType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxRate;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.Unit;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.validator.ItemExcelValidator;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ExcelUtil;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CategoryRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.ItemRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import static com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ExcelUtil.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ItemImportHandler {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemExcelValidator itemExcelValidator;

    public ItemImportHandler(ItemRepository itemRepository, CategoryRepository categoryRepository, ItemExcelValidator itemExcelValidator) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.itemExcelValidator = itemExcelValidator;
    }

    public String importItems(Sheet sheet, Company company) {
        int processed = 0;
        List<Item> batch = new ArrayList<>();

        int startRow = itemExcelValidator.getHeaderRowNumber()+1;

        for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || isRowEmpty(row)) continue;

            Item item = mapRowToItem(row, company);
            batch.add(item);
            processed++;

            if (batch.size() >= 100) {  // Save in batches
                itemRepository.saveAll(batch);
                batch.clear();
            }
        }

        if (!batch.isEmpty()) {
            itemRepository.saveAll(batch);
        }

        return "Items imported successfully: " + processed;

    }

    private Item mapRowToItem(Row row, Company company) {
        Item item = new Item();

        item.setCompany(company);


        // Map columns based on your Item Excel format
        // Adjust column indexes if your Excel is different
        item.setItemName(ExcelUtil.getCellString(row.getCell(0)));        // Column A - Item Name
        item.setItemCode(ExcelUtil.getCellString(row.getCell(1)));// Column B - Item Code
        item.setItemHsn(ExcelUtil.getCellString(row.getCell(3)));         // Column C - HSN
        //  item.setDescription(getCellString(row.getCell(3)));     // Column D - Description

        // Category (example: Column E)
        String categoryName = ExcelUtil.getCellString(row.getCell(2));
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            Category category = categoryRepository.findByCategoryNameAndCompany(categoryName.trim(), company);
            if (category == null) {
                category = new Category();
                category.setCategoryName(categoryName.trim());
                category.setCompany(company);
                category = categoryRepository.save(category);
            }
            item.setCategories(Set.of(category));
        }

        // Price, Tax, Stock
        item.setSalePrice(ExcelUtil.getCellDouble(row.getCell(4)));// Column F - Sale Price
        item.setPurchasePrice(getCellDouble(row.getCell(5)));

        if (getCellString(row.getCell(6)).contains("%")) {
            item.setSaleDiscountType(DiscountType.PERCENTAGE);
        }
        else{
            item.setSaleDiscountType(DiscountType.AMOUNT);
        }

        item.setSaleDiscountPrice(getCellDouble(row.getCell(7)));// Column G - Tax Rate %
        item.setAvailableStock(getCellDouble(row.getCell(8)));  // Column H - Stock
        item.setMinimumStockToMaintain(getCellDouble(row.getCell(9))); // Column I
        item.setOpeningStockLocation(getCellString(row.getCell(10)));

        item.setTaxRate(TaxRate.covertStringToTaxRate(getCellString(row.getCell(11))));

        // Set defaults
        item.setItemType(ItemType.PRODUCT);

        if (getCellString(row.getCell(13)) !=null && !getCellString(row.getCell(13)).isBlank()){
            item.setBaseUnit(Unit.valueOf(getCellString(row.getCell(13))));
        }
        if (getCellString(row.getCell(14)) !=null && !getCellString(row.getCell(14)).isBlank()){
            item.setSecondaryUnit(Unit.valueOf(getCellString(row.getCell(14))));
        }

        Double conversion = getCellDouble(row.getCell(15));
        if (conversion != null) {
            item.setBaseUnitToSecondaryUnit(conversion);
        }
        // item.setBaseUnit(Unit.valueOf(getCellString(row.getCell(13))));  // or your default
        //item.setSecondaryUnit(Unit.valueOf(getCellString(row.getCell(14))));
        //  item.setBaseUnitToSecondaryUnit(getCellDouble(row.getCell(15)));

        return item;
    }


}
