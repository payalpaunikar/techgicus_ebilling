package com.example.techgicus_ebilling.techgicus_ebilling.imports.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.handler.PurchaseReportImportHandler;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.handler.SaleReportImportHandler;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.validator.ItemExcelValidator;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.strategy.ItemImportHandler;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class ImportProcessingService {

    private static final Logger log = LoggerFactory.getLogger(ImportProcessingService.class);


    private final JobLauncher jobLauncher;
    private final Job importExcelDataJob;
    private final CompanyRepository companyRepository;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemExcelValidator itemExcelValidator;
    private final ItemImportHandler itemImportHandler;
    private final SaleReportImportHandler saleReportImportHandler;
    private final PurchaseReportImportHandler purchaseReportImportHandler;


    public ImportProcessingService(JobLauncher jobLauncher, Job importExcelDataJob, CompanyRepository companyRepository, ItemRepository itemRepository, CategoryRepository categoryRepository, ItemExcelValidator itemExcelValidator, ItemImportHandler itemImportHandler, SaleReportImportHandler saleReportImportHandler, PurchaseReportImportHandler purchaseReportImportHandler) {
        this.jobLauncher = jobLauncher;
        this.importExcelDataJob = importExcelDataJob;
        this.companyRepository = companyRepository;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.itemExcelValidator = itemExcelValidator;
        this.itemImportHandler = itemImportHandler;
        this.saleReportImportHandler = saleReportImportHandler;
        this.purchaseReportImportHandler = purchaseReportImportHandler;
    }

    @Value("${app.upload.dir}")
    private String uploadDir;


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
                        itemExcelValidator.validateExcelFormat(sheet);
                        return itemImportHandler.importItems(sheet, company);
                    case "SALE":
                        return saleReportImportHandler.importReport(workbook, company);
                    case "PURCHASE":
                        return purchaseReportImportHandler.importReport(workbook,company);
//                    case "PARTY":
//                        return importPartyStatements(sheet, company);
                    default:
                        throw new IllegalArgumentException("Invalid reportType: " + reportType);
                }
            }



    }

}


