package com.example.techgicus_ebilling.techgicus_ebilling.batch.processor;



import com.example.techgicus_ebilling.techgicus_ebilling.batch.dto.ExcelRowData;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.*;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto.PartyStatement;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PartyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.service.PartyActivityService;
import com.example.techgicus_ebilling.techgicus_ebilling.service.PartyLedgerService;
import jakarta.annotation.PostConstruct;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;


@Component
@StepScope
public class ExcelItemProcessor implements ItemProcessor<ExcelRowData, Object> {

    @Value("#{jobParameters['companyId']}")
    private Long companyId;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private StockTransactionRepository stockTransactionRepository;

    @Autowired
    private PartyLedgerService partyLedgerService;

    @Autowired
    private PartyActivityService partyActivityService;


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SaleItemRepository saleItemRepository;


    private Company company;  // Cache the company


    private static final Logger log = LoggerFactory.getLogger(ExcelItemProcessor.class);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @PostConstruct
    public void init() {
        company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + companyId));
    }

    @Override
    public Object process(ExcelRowData data) throws Exception {
         Row row;
        try {
             row = data.getRow();
        } catch (Exception e) {
            log.error("Error processing row " + data.getRow().getRowNum() + ": " + e.getMessage());
            e.printStackTrace();
            return null;  // skip bad row
        }

       // log.info("company id : "+company.getCompanyId()+" company name : "+company.getBussinessName());

       // Row row = data.getRow();
        String fileType = data.getFileType();
//        if (!fileType.equals("sale-main")){
//            log.info("Process row : "+row.getRowNum()+" |file type: "+fileType);
//        }

        if (row == null || row.getRowNum() < 3) return null;  // Skip headers

        if ("sale-main".equals(fileType)) {
            // Create Sale like in your createdSale
            Sale sale = new Sale();

            String dateStr = getString(row.getCell(0));
            LocalDate date = dateStr.isEmpty() ? null : LocalDate.parse(dateStr, DATE_FORMATTER);
            sale.setInvoceDate(date);

          //  sale.setInvoceDate(LocalDate.parse(getString(row.getCell(0))));  // Date
           // sale.setOrderNo(getString(row.getCell(1)));  // Order No
            sale.setInvoiceNumber(getString(row.getCell(2)));  // Invoice No

            Party party = findOrCreateParty(getString(row.getCell(3)), getString(row.getCell(4)));
            sale.setParty(party);

           // sale.setSaleType(SaleType.SALE);  // Assume regular sale
            sale.setPaymentType(PaymentType.fromString(getString(row.getCell(7))));  // Payment Type
            sale.setTotalAmount(getDouble(row.getCell(6)));  // Total Amount
            sale.setReceivedAmount(getDouble(row.getCell(8)));  // Received Amount

            // Calculate balance like in your code
            double totalAmount = sale.getTotalAmount() != null ? sale.getTotalAmount() : 0.0;
            double receivedAmount = sale.getReceivedAmount() != null ? sale.getReceivedAmount() : 0.0;
            double balance = totalAmount - receivedAmount;

            sale.setBalance(balance);

            // Set isPaid
            boolean isPaid = (balance <= 0);

            sale.setPaid(isPaid);

            // Set dueDate and isOverdue
            String dueDateStr = getString(row.getCell(0));
            LocalDate dueDate = dateStr.isEmpty() ? null : LocalDate.parse(dateStr, DATE_FORMATTER);
            //LocalDate dueDate = LocalDate.parse(getString(row.getCell(10)));
            sale.setDueDate(dueDate);
            boolean isOverDue = (dueDate.isBefore(LocalDate.now()) && balance > 0);
            sale.setOverdue(isOverDue);

            sale.setPaymentDescription(getString(row.getCell(12)));  // Description
            sale.setCompany(company);

            // Save sale (without items yet)
            saleRepository.save(sale);

           // log.info("sale is save : "+sale.getSaleId());

            // Add ledger and activity like in your code
            partyLedgerService.addLedgerEntry(
                    sale.getParty(),
                    sale.getCompany(),
                    sale.getInvoceDate(),
                    PartyTransactionType.SALE,
                    sale.getSaleId(),
                    sale.getInvoiceNumber(),
                    sale.getTotalAmount(),
                    sale.getReceivedAmount(),
                    sale.getBalance(),
                    sale.getPaymentDescription()
            );

          //  log.info("part ledger save ");

            partyActivityService.addActivity(
                    sale.getParty(),
                    sale.getCompany(),
                    sale.getSaleId(),
                    sale.getInvoiceNumber(),
                    sale.getInvoceDate(),
                    PartyTransactionType.SALE,
                    sale.getTotalAmount(),
                    sale.getBalance(),
                    true,  // isFinancial
                    sale.getPaymentDescription()
            );

          //  log.info("part activity save ");

            return sale;  // Return for writer to save (though already saved)
        } else if ("sale-items".equals(fileType)) {

            Party party = findOrCreateParty(getString(row.getCell(2)));

            SaleItem saleItem = new SaleItem();

            String invoiceNo = getString(row.getCell(1));  // Invoice No to link
            Sale sale = saleRepository.findByInvoiceNumberAndParty(invoiceNo,party);
            if (sale == null) return null;  // Skip if sale not found

            saleItem.setSale(sale);

            Item item = findOrCreateItem(getString(row.getCell(3)), getString(row.getCell(4)), getString(row.getCell(5)), getString(row.getCell(6)),company);
            saleItem.setItem(item);

            saleItem.setQuantity(getDouble(row.getCell(8)));  // Quantity
           // saleItem.setUnit(getString(row.getCell(9)));  // Unit
            saleItem.setPricePerUnit(getDouble(row.getCell(10)));  // UnitPrice
           // saleItem.setDiscountPercent(getDouble(row.getCell(11)));  // Discount Percent
           // saleItem.setDiscount(getDouble(row.getCell(12)));  // Discount
            saleItem.setTaxRate(TaxRate.fromValue(getDouble(row.getCell(13))));  // Tax Percent
            saleItem.setTaxAmount(getDouble(row.getCell(14)));  // Tax
            saleItem.setTotalAmount(getDouble(row.getCell(16)));  // Amount

            // Update stock if product
            if (item.getItemType().equals(ItemType.PRODUCT)) {
                item = updateStockAfterSale(item, saleItem);  // Your method
                itemRepository.save(item);
            }

            Double saleQty = saleItem.getQuantity();

// FIFO cost calculation
            Double fifoCost = 0.0;
            if (item.getItemType().equals(ItemType.PRODUCT)) {
                //  fifoCost = fifoService.calculateFifoCost(item, saleQty);


//                StockTransaction stockTransaction = stockTransactionService.addStockTransaction(
//                        item,
//                        StockTransactionType.SALE,
//                        saleItem.getTotalAmount(),      // sale amount
//                        sale.getInvoceDate(),
//                        sale.getInvoiceNumber(),
//                        saleQty,
//                        saleItem.getPricePerUnit()
//                );


//            if (item.getItemType().equals(ItemType.PRODUCT)) {
//                stockTransaction.setQuantity(saleItem.getQuantity());
//            }

              //  stockTransactionRepository.save(stockTransaction);
            }

            saleItemRepository.save(saleItem);

            return saleItem;
        }

        return null;  // For other types
    }

    // ... getString, getDouble, findOrCreateParty, findOrCreateItem as before
    private Party findOrCreateParty(String name, String phone) {
        if (name == null || name.trim().isEmpty()) return null;

        Optional<Party> existing = partyRepository.findByNameAndPhoneNo(
                name.trim(),
                phone != null ? phone.trim() : null
        );

        if (existing.isPresent()) {
            return existing.get();
        }

        // create new
        Party party = new Party();
        party.setName(name.trim());
        party.setCompany(company);
        if (phone != null) party.setPhoneNo(phone.trim());
        return partyRepository.save(party);
    }

    private Party findOrCreateParty(String name) {
        if (name == null || name.trim().isEmpty()) return null;

        Optional<Party> existing = partyRepository.findByName(
                name.trim()
               // phone != null ? phone.trim() : null
        );

        if (existing.isPresent()) {
            return existing.get();
        }

        // create new
        Party party = new Party();
        party.setName(name.trim());
        party.setCompany(company);
       // if (phone != null) party.setPhoneNo(phone.trim());
        return partyRepository.save(party);
    }

    // Helper to safely get a String from an Excel cell
    private String getString(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    // If it's a date, format it as string (e.g., "07/06/2021")
                    yield cell.getDateCellValue().toString();
                } else {
                    // Convert number to string without decimals if whole number
                    double num = cell.getNumericCellValue();
                    if (num == Math.floor(num)) {
                        yield String.valueOf((long) num);
                    } else {
                        yield String.valueOf(num);
                    }
                }
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();  // fallback
            default -> "";
        };
    }

    // Helper to safely get a Double from an Excel cell
    private Double getDouble(Cell cell) {
        if (cell == null) {
            return 0.0;
        }
        return switch (cell.getCellType()) {
            case NUMERIC -> cell.getNumericCellValue();
            case STRING -> {
                String str = cell.getStringCellValue().trim();
                try {
                    yield str.isEmpty() ? 0.0 : Double.parseDouble(str);
                } catch (NumberFormatException e) {
                    yield 0.0;
                }
            }
            case FORMULA -> {
                // Try to get numeric value from formula result
                try {
                    yield cell.getNumericCellValue();
                } catch (Exception e) {
                    yield 0.0;
                }
            }
            default -> 0.0;
        };
    }

    // Helper to find or create an Item during migration
    private Item findOrCreateItem(String itemName, String itemCode, String hsn, String categoryName, Company company) {
        if (company == null) {
            throw new IllegalArgumentException("Company cannot be null during item migration");
        }

        Optional<Item> existingItem = null;

//        // First, try to find by itemCode + company (most accurate)
//        if (itemName != null && !itemName.trim().isEmpty()) {
//            existingItem = itemRepository.findByItemNameAndCompany(itemName.trim(), company);
//        }

        // If not found by code, fallback to itemName + company
        if (existingItem == null && itemName != null && !itemName.trim().isEmpty()) {
            existingItem = itemRepository.findByItemNameAndCompany(itemName.trim(), company);
        }

        Item newItem = new Item();
        // If still not found, create new
        if (existingItem.isEmpty()) {
           // existingItem = new Item();
            newItem.setItemName(itemName != null ? itemName.trim() : "Unknown Item");
            newItem.setItemCode(itemCode != null ? itemCode.trim() : "");
            newItem.setItemHsn(hsn != null ? hsn.trim() : "");
            newItem.setCompany(company);  // ← Critical: link to the correct company

            // Default values for migration
            newItem.setItemType(ItemType.PRODUCT);
            newItem.setTaxRate(TaxRate.GST18);  // or detect from Excel if possible
            newItem.setBaseUnit(Unit.NUMBERS);     // common default
            newItem.setAvailableStock(0.0);
           // newItem.setTotalStockIn(0.0);
            newItem.setStockValue(0.0);

            // Handle Category
            if (categoryName != null && !categoryName.trim().isEmpty()) {
                Category category = findOrCreateCategory(categoryName.trim(), company);
                newItem.setCategories(Set.of(category));  // Set with one category
            }

            newItem = itemRepository.save(newItem);

            log.info("Created new Item: " + newItem.getItemName() +
                    " (Code: " + newItem.getItemCode() + ") for Company ID: " + company.getCompanyId());
        }

        return newItem;
    }

    private Category findOrCreateCategory(String categoryName, Company company) {
        Category category = categoryRepository.findByCategoryNameAndCompany(categoryName, company);

        if (category == null) {
            category = new Category();
            category.setCategoryName(categoryName);
            category.setCompany(company);
            category = categoryRepository.save(category);

            log.info("Created new Category: " + categoryName + " for Company ID: " + company.getCompanyId());
        }

        return category;
    }


    // This method updates the stock quantity after a sale (same as in your createdSale)
    private Item updateStockAfterSale(Item item, SaleItem saleItem) {
//        if (item.getCurrentStock() == null) {
//            item.setCurrentStock(0.0);
//        }
//
//        if (item.getTotalStockIn() == null) {
//            item.setTotalStockIn(0.0);
//        }

//        // Subtract the sold quantity from stock
//        double newStock = item.getTotalStockIn() - saleItem.getQuantity();
//
//       // item.setCurrentStock(newStock);
//
//        item.setTotalStockIn(newStock);


        // Optional: set low stock flag or other logic if you have it
        // if (newStock < item.getMinStockLevel()) { ... }

        return item;
    }
}
