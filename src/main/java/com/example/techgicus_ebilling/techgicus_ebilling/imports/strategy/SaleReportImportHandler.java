package com.example.techgicus_ebilling.techgicus_ebilling.imports.strategy;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.*;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.SaleRowData;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.validator.SaleItemDetailsExcelValidator;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.validator.SaleReportExcelValidator;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ModelUtill;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.SaleRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.SaleReturnRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.service.PartyActivityService;
import com.example.techgicus_ebilling.techgicus_ebilling.service.PartyLedgerService;
import com.example.techgicus_ebilling.techgicus_ebilling.service.SaleService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ExcelUtil.*;


@Service
public class SaleReportImportHandler {

    private static final Logger log = LoggerFactory.getLogger(SaleReportImportHandler.class);
    private final ModelUtill modelUtill;
   private final SaleRepository saleRepository;
   private final SaleReturnRepository saleReturnRepository;
   private final SaleReportExcelValidator saleReportExcelValidator;
   private final PartyLedgerService partyLedgerService;
   private final PartyActivityService partyActivityService;
   private final SaleService saleService;
   private final SaleItemDetailsExcelValidator saleItemDetailsExcelValidator;

    private static final int BATCH_SIZE = 100;


    public SaleReportImportHandler(ModelUtill modelUtill, SaleRepository saleRepository, SaleReturnRepository saleReturnRepository, SaleReportExcelValidator saleReportExcelValidator, PartyLedgerService partyLedgerService, PartyActivityService partyActivityService, SaleService saleService, SaleItemDetailsExcelValidator saleItemDetailsExcelValidator) {
        this.modelUtill = modelUtill;
        this.saleRepository = saleRepository;
        this.saleReturnRepository = saleReturnRepository;
        this.saleReportExcelValidator = saleReportExcelValidator;
        this.partyLedgerService = partyLedgerService;
        this.partyActivityService = partyActivityService;
        this.saleService = saleService;
        this.saleItemDetailsExcelValidator = saleItemDetailsExcelValidator;
    }

    @Transactional
    public String importSaleReport(Workbook workbook, Company company) {

        try {
            Sheet saleSheet = workbook.getSheetAt(0);
            Sheet saleItemSheet = workbook.getSheetAt(1);

            if (saleSheet == null) {
                throw new IllegalArgumentException("Sale summary sheet not found");
            }
            if (saleItemSheet == null) {
                throw new IllegalArgumentException("Sale items sheet not found");
            }

            saleReportExcelValidator.validateExcelFormat(saleSheet);
            saleItemDetailsExcelValidator.validateExcelFormat(saleItemSheet);

            // Process sales & returns first (they must exist before items can link)
            int salesProcessed = processSales(saleSheet, company);

            return String.format(
                    "Import completed successfully\n" +
                            "• Sales & Credit Notes: %d records processed\n" +
                            "• Sale Items: %d lines imported",
                    salesProcessed
            );

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Import failed: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error during sale report import", e);
            throw new RuntimeException("Failed to import sale report: " + e.getMessage(), e);
        }



    }


    private int processSales(Sheet sheet, Company company) {
        int count = 0;
        int startRow = saleReportExcelValidator.getHeaderRowNumber() + 1;

        for (int r = startRow; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null || isRowEmpty(row)) continue;

            String type = getCellString(row.getCell(5)).toLowerCase().trim();

            if (type.contains("sale")) {
                Sale sale = mapRowToSale(row, company);
                if (sale != null) {
                    saveOrUpdateSaleAndRelated(sale);  // your existing logic
                    count++;
                }
            } else if (type.contains("credit") || type.contains("return")) {
                SaleReturn ret = mapRowToSaleReturn(row, company);
                if (ret != null) {
                    saveOrUpdateSaleReturnAndRelated(ret);
                    count++;
                }
            }
        }
        return count;
    }


    /**
     * Saves or updates a Sale entity along with all its related entities in one logical operation.
     * Handles both create and update cases.
     */
    private void saveOrUpdateSaleAndRelated(Sale sale) {
        // 1. Decide if this is new or existing
        boolean isNew = sale.getSaleId() == null;

        if (isNew) {
            // if sale is new so, we marked first payment as
            // sale payment
                    SalePayment salePayment = saleService.setSalePaymentFields(
                            sale.getPaymentDescription(),
                            sale.getPaymentType(),
                            sale.getInvoceDate(),
                            sale, sale.getReceivedAmount(),
                            LocalDateTime.now(), LocalDateTime.now(),
                            null,
                            null);

                  sale.setSalePayments(List.of(salePayment));

            sale = saleRepository.save(sale);

            // Create ledger & activity entries (new entries)
            partyLedgerService.addLedgerEntry(
                    sale.getParty(),
                    sale.getCompany(),
                    sale.getInvoceDate(),
                    PartyTransactionType.SALE,
                    sale.getSaleId(),
                    sale.getInvoiceNumber(),
                    sale.getTotalAmount(),
                    0.0,
                    sale.getBalance(),
                    sale.getPaymentDescription()
            );

            partyActivityService.addActivity(
                    sale.getParty(),
                    sale.getCompany(),
                    sale.getSaleId(),
                    sale.getInvoiceNumber(),
                    sale.getInvoceDate(),
                    PartyTransactionType.SALE,
                    sale.getTotalAmount(),
                    sale.getBalance(),
                    true,
                    sale.getPaymentDescription()
            );

        } else {

            SalePayment firstPayment = null;

            if (!sale.getSalePayments().isEmpty()){
                firstPayment = sale.getSalePayments().get(0);
                saleService.updateSalePayment(firstPayment,
                                sale.getPaymentDescription(),
                                sale.getPaymentType(),
                                sale.getInvoceDate(),
                                sale.getReceivedAmount(),
                                LocalDateTime.now(),
                                null,
                                null);

            }
            else{
                firstPayment = saleService.setSalePaymentFields(
                        sale.getPaymentDescription(),
                        sale.getPaymentType(),
                        sale.getInvoceDate(),
                        sale, sale.getReceivedAmount(),
                        LocalDateTime.now(), LocalDateTime.now(),
                        null,
                        null);

                sale.setSalePayments(List.of(firstPayment));

            }

            // Save updated sale
            saleRepository.save(sale);

            // Update ledger & activity (usually update existing records)
            partyLedgerService.updatePartyLedger(
                    sale.getParty(),
                    sale.getCompany(),
                    sale.getInvoceDate(),
                    PartyTransactionType.SALE,
                    sale.getSaleId(),
                    sale.getInvoiceNumber(),
                    sale.getTotalAmount(),
                    0.0,
                    sale.getBalance(),
                    sale.getPaymentDescription()
            );

            partyActivityService.updatePartyActivity(
                    sale.getParty(),
                    sale.getCompany(),
                    sale.getSaleId(),
                    sale.getInvoiceNumber(),
                    sale.getInvoceDate(),
                    PartyTransactionType.SALE,
                    sale.getTotalAmount(),
                    sale.getBalance(),
                    true,
                    sale.getPaymentDescription()
            );
        }

        log.info("{} Sale {} successfully: ID={}, Invoice={}",
                isNew ? "Created" : "Updated",
                sale.getSaleId(),
                sale.getSaleId(),
                sale.getInvoiceNumber());
    }


    /**
     * Saves or updates a SaleReturn entity and handles all related side-effects:
     * - persist the SaleReturn
     * - create/update corresponding PartyLedger entry
     * - create/update corresponding PartyActivity entry
     */
    private void saveOrUpdateSaleReturnAndRelated(SaleReturn ret) {
        if (ret == null) {
            log.warn("Attempted to save null SaleReturn");
            return;
        }

        boolean isNew = ret.getSaleReturnId() == null;

        SaleReturn savedReturn;

        if (isNew) {
            // ─── CREATE NEW ────────────────────────────────────────────────
            savedReturn = saleReturnRepository.save(ret);

            partyLedgerService.addLedgerEntry(
                    savedReturn.getParty(),
                    savedReturn.getCompany(),
                    savedReturn.getReturnDate(),
                    PartyTransactionType.SALE_RETURN,
                    savedReturn.getSaleReturnId(),
                    savedReturn.getReturnNo(),
                    0.0,                           // debit (outflow) = 0
                    savedReturn.getTotalAmount(),  // credit (inflow to party) = return amount
                    savedReturn.getBalanceAmount(),
                    savedReturn.getDescription()
            );

            partyActivityService.addActivity(
                    savedReturn.getParty(),
                    savedReturn.getCompany(),
                    savedReturn.getSaleReturnId(),
                    savedReturn.getReturnNo(),
                    savedReturn.getReturnDate(),
                    PartyTransactionType.SALE_RETURN,
                    savedReturn.getTotalAmount(),
                    savedReturn.getBalanceAmount(),
                    true,   // returns are usually considered settled/paid
                    savedReturn.getDescription()
            );

            log.info("Created new Sale Return: id={}, returnNo={}",
                    savedReturn.getSaleReturnId(), savedReturn.getReturnNo());
        }
        else {

            partyLedgerService.updatePartyLedger(
                    ret.getParty(),
                    ret.getCompany(),
                    ret.getReturnDate(),
                    PartyTransactionType.SALE_RETURN,
                    ret.getSaleReturnId(),
                    ret.getReturnNo(),
                    0.0,
                    ret.getTotalAmount(),
                    ret.getBalanceAmount(),
                    ret.getDescription()
            );

            partyActivityService.updatePartyActivity(
                    ret.getParty(),
                    ret.getCompany(),
                    ret.getSaleReturnId(),
                    ret.getReturnNo(),
                    ret.getReturnDate(),
                    PartyTransactionType.SALE_RETURN,
                    ret.getTotalAmount(),
                    ret.getBalanceAmount(),
                    true,
                    ret.getDescription()
            );

            log.info("Updated existing Sale Return: id={}, returnNo={}",
                    ret.getSaleReturnId(), ret.getReturnNo());
        }
    }


    private Sale mapRowToSale(Row row, Company company) {

        Party party = modelUtill.findOrCreateParty(getCellString(row.getCell(3)),getCellString(row.getCell(4)),company);
        if (party == null) return  null;

        SaleRowData saleRowData = extractRowData(row);

        Optional<Sale> sale = saleRepository.findByInvoiceNumberAndCompany(saleRowData.getInvoiceNo(),company);

        if (sale.isPresent()){

            Sale existingSale = sale.get();

            existingSale = setSaleFields(
                    party,company,
                    existingSale,
                    saleRowData);

            return existingSale;

        }


        Sale newSale = new Sale();
        newSale = setSaleFields(
                party,company,
                newSale,
                saleRowData);


        return newSale;

    }


    private SaleReturn mapRowToSaleReturn(Row row, Company company) {
        Party party = modelUtill.findOrCreateParty(getCellString(row.getCell(3)),getCellString(row.getCell(4)),company);
        if (party == null) return  null;
        SaleRowData saleRowData = extractRowData(row);
        Optional<SaleReturn> saleReturn = saleReturnRepository.findByReturnNoAndCompany(saleRowData.getInvoiceNo(),company);

        if (saleReturn.isPresent()){
            SaleReturn existingSaleReturn = saleReturn.get();

            existingSaleReturn = setSaleReturnFields(party,company,existingSaleReturn,saleRowData);

            return existingSaleReturn;
        }

        SaleReturn newSaleReturn = new SaleReturn();
        newSaleReturn = setSaleReturnFields(party,company,newSaleReturn,saleRowData);

        return newSaleReturn;
    }


    private Sale setSaleFields(Party party,
                               Company company,
                               Sale sale,
                               SaleRowData data){
        sale.setInvoceDate(data.getInvoiceDate());
        sale.setInvoiceNumber(data.getInvoiceNo());
        sale.setTotalAmount(data.getTotalAmount());
        sale.setPaymentType(data.getPaymentType());
        sale.setReceivedAmount(data.getReceivedAmount());
        sale.setBalance(data.getBalance());
        sale.setDueDate(data.getDueDate());
        sale.setPaymentDescription(data.getDescription());
        sale.setParty(party);
        sale.setCompany(company);
        sale.setPaid(data.isPaid());

        if (!data.isPaid()){
            sale.setOverdue(true);
        }

        return sale;
    }

    private SaleReturn setSaleReturnFields(
                               Party party,
                               Company company,
                               SaleReturn saleReturn,
                               SaleRowData data){
        saleReturn.setReturnDate(data.getInvoiceDate());
        saleReturn.setReturnNo(data.getInvoiceNo());
        saleReturn.setTotalAmount(data.getTotalAmount());
        saleReturn.setPaymentType(data.getPaymentType());
        saleReturn.setPaidAmount(data.getReceivedAmount());
        saleReturn.setBalanceAmount(data.getBalance());
        saleReturn.setDescription(data.getDescription());
        saleReturn.setParty(party);
        saleReturn.setCompany(company);

        return saleReturn;
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


    private int processSalesItems(Sheet sheet, Company company) {
        int count = 0;
        int startRow = saleItemDetailsExcelValidator.getHeaderRowNumber() + 1;

        for (int r = startRow; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null || isRowEmpty(row)) continue;

            String type = getCellString(row.getCell(5)).toLowerCase().trim();


        }
        return count;
    }


}
