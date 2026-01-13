package com.example.techgicus_ebilling.techgicus_ebilling.imports.strategy.sale;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.enumeration.ImportReportType;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.strategy.ImportHandler;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

@Service
public class SaleReportImportHandler implements ImportHandler {


    @Override
    public boolean supports(ImportReportType reportType) {
        return reportType == ImportReportType.SALE;
    }

    @Override
    public String importReport(Workbook workbook, Company company) {
        return "";
    }
}
