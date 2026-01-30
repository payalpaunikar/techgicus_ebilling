package com.example.techgicus_ebilling.techgicus_ebilling.imports.processor;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportContext;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.SaleItemRow;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.extractor.SaleItemRowExtractor;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper.SaleReturnItemEntityMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.service.SaleCalculationService;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ModelUtill;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class SaleReturnItemTransactionProcessor implements TransactionProcessor{

    private final SaleItemRowExtractor saleItemRowExtractor;
    private final SaleReturnRepository saleReturnRepository;
    private final SaleReturnItemRepository saleReturnItemRepository;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final SaleReturnItemEntityMapper saleReturnItemEntityMapper;
    private final SaleCalculationService saleCalculationService;

    public SaleReturnItemTransactionProcessor(SaleItemRowExtractor saleItemRowExtractor, SaleReturnRepository saleReturnRepository, SaleReturnItemRepository saleReturnItemRepository, ItemRepository itemRepository, CategoryRepository categoryRepository, SaleReturnItemEntityMapper saleReturnItemEntityMapper, SaleCalculationService saleCalculationService) {
        this.saleItemRowExtractor = saleItemRowExtractor;
        this.saleReturnRepository = saleReturnRepository;
        this.saleReturnItemRepository = saleReturnItemRepository;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.saleReturnItemEntityMapper = saleReturnItemEntityMapper;
        this.saleCalculationService = saleCalculationService;
    }

    private final static Logger log = LoggerFactory.getLogger(SaleReturnItemTransactionProcessor.class);


    @Override
    public boolean supports(String transactionType) {
        log.info("transaction type is : "+transactionType);
        if (transactionType.toLowerCase().trim().equals("credit-note-item"))return true;
        return false;
    }

    @Override
    public void process(Row row, Company company, ImportContext context) {

        log.info("row number : "+row.getRowNum());

        SaleItemRow saleItemRow = saleItemRowExtractor.extract(row);

        log.info("Excel quantity = {}", saleItemRow.getQuantity());

        Optional<SaleReturn> saleReturn = saleReturnRepository.findByReturnNoAndCompany(saleItemRow.getInvoiceNo(),company);

        if (saleReturn.isEmpty()){
            context.addError(row.getRowNum()+1,"Sale Return not found for invoice : "+saleItemRow.getInvoiceNo());
            return;
        }

        Item item = ModelUtill.findOrCreateItem(saleItemRow,
                company,
                itemRepository,
                categoryRepository);

        // 🔥 CLEAR ONLY ONCE PER SALE
        if (!context.isInitialized(saleReturn.get())) {

            log.info("First time processing sale {}, clearing old items & payments",
                    saleReturn.get().getReturnNo());

            saleReturn.get().getSaleReturnItems().clear();
           // saleReturn.get().getSalePayments().clear();

            context.markInitialized(saleReturn.get());
        }


        SaleReturnItem newSaleReturnItem = new SaleReturnItem();

        newSaleReturnItem = saleReturnItemEntityMapper.toEntity(
                saleReturn.get(),
                saleItemRow,
                item,
                newSaleReturnItem
        );

        log.info("Entity quantity = {}", newSaleReturnItem.getQuantity());


        // saleReturnItemRepository.save(newSaleReturnItem);

        SaleReturn existingSaleReturn = saleReturn.get();
        existingSaleReturn.getSaleReturnItems().add(newSaleReturnItem);

      //  existingSaleReturn = saleCalculationService.recalculateSaleTotals(existingSaleReturn);

        saleReturnRepository.save(existingSaleReturn);

    }
}
