package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Category;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Item;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.DiscountType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.ItemType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxRate;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.Unit;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CategoryRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CompanyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.ItemRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@Service
public class ImportProcessingService {

    private final JobLauncher jobLauncher;
    private final Job importExcelDataJob;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private static final Logger log = LoggerFactory.getLogger(ImportProcessingService.class);

    @Autowired
    public ImportProcessingService(JobLauncher jobLauncher, Job importExcelDataJob) {
        this.jobLauncher = jobLauncher;
        this.importExcelDataJob = importExcelDataJob;
    }


    public String startImportJob(MultipartFile file, Long companyId, String importType) throws Exception {
        // Create upload directory if not exists
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.info("Created upload directory: " + uploadPath);
        }

        // Save the ZIP file with unique name
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("File name is null");
        }
        String fileName = System.currentTimeMillis() + "_" + originalFilename;
        Path zipFilePath = uploadPath.resolve(fileName);

        // Save the file
        file.transferTo(zipFilePath.toFile());

        String archiveType = originalFilename.toLowerCase().endsWith(".rar") ? "rar" : "zip";

        // Create unique job parameters
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("archiveFilePath", zipFilePath.toString())
                .addString("archiveType", archiveType)  // New parameter
                .addLong("companyId", companyId)
                .addLong("startAt", System.currentTimeMillis())
                .addString("importType",importType)
                .toJobParameters();

        // Launch the batch job (runs asynchronously)
        jobLauncher.run(importExcelDataJob, jobParameters);

        return "File uploaded and import job started! Saved as: " + fileName +
                " (Type: " + archiveType.toUpperCase() + ")";
    }


    @Transactional
    public String importSingleExcel(MultipartFile file, Long companyId, String reportType) throws Exception {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);  // First sheet only
          //  int rowCount = sheet.getLastRowNum() + 1;  // +1 because index starts at 0

//            if (rowCount > 5000) {
//
//            }
                switch (reportType.toUpperCase()) {
                    case "ITEM":
                        validateItemExcelFormat(sheet);
                        return importItems(sheet, company);
//                    case "SALE":
//                        return importSales(sheet, company);
//                    case "PURCHASE":
//                        return importPurchases(sheet, company);
//                    case "PARTY":
//                        return importPartyStatements(sheet, company);
                    default:
                        throw new IllegalArgumentException("Invalid reportType: " + reportType);
                }
            }



    }

    private void validateItemExcelFormat(Sheet sheet) throws IllegalArgumentException {
        // 1. Check sheet name
        String sheetName = sheet.getSheetName().toLowerCase().trim();
        if (!sheetName.contains("items")) {
            throw new IllegalArgumentException(
                    "Wrong Excel format: Sheet name should contain 'Items'. Found: '" + sheet.getSheetName() + "'");
        }

        // 2. Check header row (row index 0 = row 1 in Excel)
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new IllegalArgumentException("Header row is missing (expected at row 1).");
        }

        // 3. Define required columns and their positions
        // Adjust these indexes based on your actual Item Excel template
        Map<Integer, String> requiredHeaders = new LinkedHashMap<>();

        requiredHeaders.put(0, "Item Name");
        requiredHeaders.put(1, "Item Code");
        requiredHeaders.put(2, "Category");
        requiredHeaders.put(3, "HSN");
        requiredHeaders.put(4, "Sale Price");
        requiredHeaders.put(5, "Purchase price");
        requiredHeaders.put(6, "Discount Type");
        requiredHeaders.put(7, "Sale Discount");
        requiredHeaders.put(8, "Current stock quantity");
        requiredHeaders.put(9, "Minimum stock quantity");
        requiredHeaders.put(10, "Item Location");
        requiredHeaders.put(11, "Tax Rate");
        requiredHeaders.put(12, "Inclusive Of Tax");
        requiredHeaders.put(13, "Base Unit");
        requiredHeaders.put(14, "Secondary Unit");
        requiredHeaders.put(15, "Conversion Rate");


        // 4. Check each required header
        for (Map.Entry<Integer, String> entry : requiredHeaders.entrySet()) {
            int colIndex = entry.getKey();
            String expected = entry.getValue();

            Cell cell = headerRow.getCell(colIndex);
            String actual = getCellString(cell);

            if (actual == null || !actual.toLowerCase().contains(expected.toLowerCase())) {
                char columnLetter = (char) ('A' + colIndex);
                throw new IllegalArgumentException(
                        "Wrong format: Column " + columnLetter + " should contain '" + expected +
                                "', but found: '" + actual + "'");
            }
        }

        log.info("Item Excel format validated successfully.");
    }

    // Helper to get string from cell safely
    private String getCellString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> "";
        };
    }

    // Your existing import method
    private String importItems(Sheet sheet, Company company) {
        int processed = 0;
        List<Item> batch = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
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

    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }

        // Check the first 10 cells (adjust number if needed)
        for (int i = 0; i < Math.min(10, row.getLastCellNum()); i++) {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (cell.getCellType() != CellType.BLANK) {
                // If any cell has data (string, number, etc.), row is not empty
                return false;
            }
        }
        return true;  // All checked cells are blank → row is empty
    }


    private Item mapRowToItem(Row row, Company company) {
        Item item = new Item();

        item.setCompany(company);


        // Map columns based on your Item Excel format
        // Adjust column indexes if your Excel is different
        item.setItemName(getCellString(row.getCell(0)));        // Column A - Item Name
        item.setItemCode(getCellString(row.getCell(1)));// Column B - Item Code
        item.setItemHsn(getCellString(row.getCell(3)));         // Column C - HSN
      //  item.setDescription(getCellString(row.getCell(3)));     // Column D - Description

        // Category (example: Column E)
        String categoryName = getCellString(row.getCell(2));
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
        item.setSalePrice(getCellDouble(row.getCell(4)));// Column F - Sale Price
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


    private Double getCellDouble(Cell cell) {
        if (cell == null) return 0.0;
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        }
        try {
            return Double.parseDouble(getCellString(cell));
        } catch (Exception e) {
            return 0.0;
        }
    }

}


