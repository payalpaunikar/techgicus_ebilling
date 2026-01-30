package com.example.techgicus_ebilling.techgicus_ebilling.imports.processor;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportContext;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.SaleItemRow;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.extractor.SaleItemRowExtractor;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper.SaleItemEntityMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper.SalePaymentEntityMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.service.SaleCalculationService;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ModelUtill;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SaleItemTransactionProcessor implements TransactionProcessor{

    private final SaleItemRowExtractor saleItemRowExtractor;
    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final SaleItemEntityMapper saleItemEntityMapper;
    private final SaleCalculationService saleCalculationService;
    private final SalePaymentEntityMapper salePaymentEntityMapper;
    private final SalePaymentRepository salePaymentRepository;

    private final static Logger log = LoggerFactory.getLogger(SaleItemTransactionProcessor.class);

    public SaleItemTransactionProcessor(SaleItemRowExtractor saleItemRowExtractor, SaleRepository saleRepository, SaleItemRepository saleItemRepository, ItemRepository itemRepository, CategoryRepository categoryRepository, SaleItemEntityMapper saleItemEntityMapper, SaleCalculationService saleCalculationService, SalePaymentEntityMapper salePaymentEntityMapper, SalePaymentRepository salePaymentRepository) {
        this.saleItemRowExtractor = saleItemRowExtractor;
        this.saleRepository = saleRepository;
        this.saleItemRepository = saleItemRepository;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.saleItemEntityMapper = saleItemEntityMapper;
        this.saleCalculationService = saleCalculationService;
        this.salePaymentEntityMapper = salePaymentEntityMapper;
        this.salePaymentRepository = salePaymentRepository;
    }

    @Override
    public boolean supports(String transactionType) {
        log.info("transaction type is : "+transactionType);
        if (transactionType.toLowerCase().trim().equals("sale-item"))return true;
        return false;
    }

    @Override
    public void process(Row row, Company company, ImportContext context) {

        log.info("row number : "+row.getRowNum());

        SaleItemRow saleItemRow = saleItemRowExtractor.extract(row);

        Optional<Sale> sale = saleRepository.findByInvoiceNumberAndCompany(saleItemRow.getInvoiceNo(),company);

        if (sale.isEmpty()){
            context.addError(row.getRowNum()+1,"Sale not found for invoice : "+saleItemRow.getInvoiceNo());
            return;
        }


       Item item = ModelUtill.findOrCreateItem(saleItemRow,
               company,
               itemRepository,
               categoryRepository);



        // 🔥 CLEAR ONLY ONCE PER SALE
        if (!context.isInitialized(sale.get())) {

            log.info("First time processing sale {}, clearing old items & payments",
                    sale.get().getInvoiceNumber());

            sale.get().getSaleItem().clear();
            sale.get().getSalePayments().clear();

            context.markInitialized(sale.get());
        }


        SaleItem newSaleItem = new SaleItem();

       newSaleItem = saleItemEntityMapper.toEntity(
               sale.get(),
               saleItemRow,
               item,
               newSaleItem
       );


      //  saleItemRepository.save(newSaleItem);

        Sale existingSale = sale.get();
        existingSale.getSaleItem().add(newSaleItem);


     //  existingSale =  saleCalculationService.recalculateSaleTotals(existingSale);
        saleRepository.save(existingSale);


//        SalePayment salePayment = new SalePayment();
//        salePayment = salePaymentEntityMapper.toEntity(existingSale,salePayment);
//
//        salePaymentRepository.save(salePayment);
    }
}
