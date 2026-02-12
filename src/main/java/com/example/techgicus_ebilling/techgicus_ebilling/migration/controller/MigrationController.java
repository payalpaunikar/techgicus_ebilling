package com.example.techgicus_ebilling.techgicus_ebilling.migration.controller;

import com.example.techgicus_ebilling.techgicus_ebilling.migration.service.StockMigrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/migration")
public class MigrationController {
    private final StockMigrationService stockMigrationService;

    public MigrationController(StockMigrationService stockMigrationService) {
        this.stockMigrationService = stockMigrationService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/rebuild-stock")
    public ResponseEntity<String> rebuildStock() {
        stockMigrationService.rebuildStockFromScratch();
        return ResponseEntity.ok("✅ Stock migration completed");
    }
}
