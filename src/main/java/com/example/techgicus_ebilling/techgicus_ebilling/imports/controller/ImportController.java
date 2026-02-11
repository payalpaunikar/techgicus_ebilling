package com.example.techgicus_ebilling.techgicus_ebilling.imports.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.ImportSummaryResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.service.ImportProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImportController {

      private final ImportProcessingService importService;

    @Autowired
    public ImportController(ImportProcessingService importService) {
        this.importService = importService;
    }


    // import type can be PartyStatement,CompanyOverAllData
    @PostMapping("/company/{companyId}/upload/zip")
    @PreAuthorize("hasAuthority('ADMIN')")
      public ResponseEntity<?> uploadZipFile(
            @RequestParam("file")MultipartFile file,
            @PathVariable Long companyId,
            @RequestParam(value = "importTpe") String importType){
          if (file.isEmpty()){
              return ResponseEntity.badRequest().body("Please select a ZIP file to upload.");
          }

        String fileName = file.getOriginalFilename().toLowerCase();
          if (!(fileName.endsWith(".zip") || fileName.endsWith(".rar"))) {
              return ResponseEntity.badRequest().body("Only ZIP files are allowed.");
          }

          try {
              String jobMessage = importService.startImportJob(file,companyId,importType);
              return ResponseEntity.ok(jobMessage);
          } catch (Exception e) {
              return ResponseEntity.internalServerError()
                      .body("Import failed: " + e.getMessage());
          }

      }


      // report type be :- ITEM (for import item), SALE, PURCHASE, PARTY
      @PostMapping("/company/{companyId}/upload/excel")
      @PreAuthorize("hasAuthority('ADMIN')")
     public ResponseEntity<ImportSummaryResponse> uploadExcelSheet(@PathVariable Long companyId,
                                                                   @RequestParam("file") MultipartFile file,
                                                                   @RequestParam("reportType")String reportType) throws Exception {
        return ResponseEntity.ok(importService.importSingleExcel(file,companyId,reportType));
     }


}
