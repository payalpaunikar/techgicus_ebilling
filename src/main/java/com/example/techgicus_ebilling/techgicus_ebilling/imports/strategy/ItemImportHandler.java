package com.example.techgicus_ebilling.techgicus_ebilling.imports.strategy;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.*;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportContext;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportError;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.ImportSummaryResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.enumeration.ImportReportType;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.handler.ImportHandler;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.validator.ItemExcelValidator;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ExcelUtil;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CategoryRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.ItemRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PurchaseBatchRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.StockTransactionRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import static com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ExcelUtil.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ItemImportHandler {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemExcelValidator itemExcelValidator;
    private final PurchaseBatchRepository purchaseBatchRepository;
    private final StockTransactionRepository stockTransactionRepository;

    public ItemImportHandler(ItemRepository itemRepository, CategoryRepository categoryRepository, ItemExcelValidator itemExcelValidator, PurchaseBatchRepository purchaseBatchRepository, StockTransactionRepository stockTransactionRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.itemExcelValidator = itemExcelValidator;
        this.purchaseBatchRepository = purchaseBatchRepository;
        this.stockTransactionRepository = stockTransactionRepository;
    }

    public ImportSummaryResponse importItems(Sheet sheet, Company company) {
        int processed = 0;
        List<Item> batch = new ArrayList<>();
        ImportContext context = new ImportContext();

        int startRow = itemExcelValidator.getHeaderRowNumber()+1;

        for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || isRowEmpty(row)) continue;

            try {
                Item item = mapRowToItem(row, company);
                batch.add(item);
                processed++;

                if (batch.size() >= 100) {  // Save in batches
                    // itemRepository.saveAll(batch);
                    saveItemsWithOpeningStock(batch);
                    batch.clear();
                }
            } catch (Exception e) {
                context.addError(
                        sheet.getSheetName(),
                        i + 1,
                        "Invalid item data: " + e.getMessage()
                );
            }
        }

        if (!batch.isEmpty()) {
            saveItemsWithOpeningStock(batch);
          //  itemRepository.saveAll(batch);
        }

      //  return "Items imported successfully: " + processed;

        return buildImportSummary("ITEM", processed, processed, context);

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
        item.setStockOpeningQty(getCellDouble(row.getCell(8)));
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


    private void saveItemsWithOpeningStock(List<Item> items) {

        List<Item> savedItems = itemRepository.saveAll(items);

        for (Item item : savedItems) {

            Double openingQty = item.getStockOpeningQty();
            Double rate = item.getPurchasePrice(); // Using purchase price as cost

            if (openingQty == null || openingQty <= 0) {
                item.setAvailableStock(0.0);
                item.setStockValue(0.0);
                itemRepository.save(item);
                continue;
            }

            double stockRate = rate != null ? rate : 0.0;
            LocalDate openingDate = LocalDate.now(); // No column → today

            // ==================================================
            // 1️⃣ FIFO BATCH (CRITICAL)
            // ==================================================

            PurchaseBatch batch = new PurchaseBatch();
            batch.setItem(item);
            batch.setQtyPurchased(openingQty);
            batch.setQtyRemaining(openingQty);
            batch.setPricePerQty(BigDecimal.valueOf(stockRate));
            batch.setPurchaseDate(openingDate);
            batch.setPurchaseId(0L); // OPENING reference
            batch.setActive(true);

            purchaseBatchRepository.save(batch);

            // ==================================================
            // 2️⃣ STOCK TRANSACTION (HISTORY)
            // ==================================================

            StockTransaction tx = new StockTransaction();
            tx.setItem(item);
            tx.setType(StockTransactionType.OPENING);
            tx.setTransactionDate(openingDate);
            tx.setReference("OPENING");
            tx.setQuantity(openingQty);
            tx.setRate(BigDecimal.valueOf(stockRate));
            tx.setAmount(
                    BigDecimal.valueOf(openingQty)
                            .multiply(BigDecimal.valueOf(stockRate))
            );

            stockTransactionRepository.save(tx);

            // ==================================================
            // 3️⃣ SNAPSHOT UPDATE (SAFE METHOD)
            // ==================================================

            Double availableStock =
                    stockTransactionRepository.totalStock(item.getItemId());

            Double stockValue =
                    availableStock == null || availableStock <= 0
                            ? 0.0
                            : purchaseBatchRepository.sumRemainingValue(item.getItemId());

            item.setAvailableStock(
                    availableStock != null ? availableStock : 0.0);

            item.setStockValue(Math.max(stockValue, 0));

            item.setStockPricePerQty(stockRate);

            itemRepository.save(item);
        }
    }


    public ImportSummaryResponse buildImportSummary(
            String moduleName,
            int documentCount,
            int itemCount,
            ImportContext context
    ) {
        ImportSummaryResponse response = new ImportSummaryResponse();

        response.setModule(moduleName);
        response.setDocumentsProcessed(documentCount);
        response.setItemsProcessed(itemCount);
        response.setSuccess(!context.hasErrors());

        List<ImportError> errorList = context.getErrors()
                .stream()
                .map(error -> {
                    ImportError err = new ImportError(error.getSheetName(),error.getRowNumber(),error.getMessage());
//                    err.setSheetName(error.getSheetName());
//                    err.setRowNumber(error.getRowNumber());
//                    err.setMessage(error.getMessage());
                    return err;
                })
                .toList();

        response.setErrors(errorList);

        return response;
    }

}
