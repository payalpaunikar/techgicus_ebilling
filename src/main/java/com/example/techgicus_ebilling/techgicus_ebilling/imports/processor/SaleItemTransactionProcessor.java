package com.example.techgicus_ebilling.techgicus_ebilling.imports.processor;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportContext;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.SaleItemRow;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.extractor.SaleItemRowExtractor;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper.SaleItemEntityMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.service.SaleCalculationService;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.SaleItemMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CategoryRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.ItemRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.SaleItemRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.SaleRepository;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SaleItemTransactionProcessor implements TransactionProcessor{

    private final SaleItemRowExtractor saleItemRowExtractor;
    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final SaleItemEntityMapper saleItemEntityMapper;
    private final SaleCalculationService saleCalculationService;

    private final static Logger log = LoggerFactory.getLogger(SaleTransactionProcessor.class);

    public SaleItemTransactionProcessor(SaleItemRowExtractor saleItemRowExtractor, SaleRepository saleRepository, SaleItemRepository saleItemRepository, ItemRepository itemRepository, CategoryRepository categoryRepository, SaleItemEntityMapper saleItemEntityMapper, SaleCalculationService saleCalculationService) {
        this.saleItemRowExtractor = saleItemRowExtractor;
        this.saleRepository = saleRepository;
        this.saleItemRepository = saleItemRepository;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.saleItemEntityMapper = saleItemEntityMapper;
        this.saleCalculationService = saleCalculationService;
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


        Item item = itemRepository.findByItemNameAndCompany(
                saleItemRow.getItemName(),
                company
        ).orElseGet(()->{
            Item newItem = new Item();
            newItem.setItemName(saleItemRow.getItemName());
            newItem.setItemCode(saleItemRow.getItemCode());
            newItem.setItemHsn(saleItemRow.getHsn());
            newItem.setCompany(company);
            newItem.setCreatedAt(LocalDateTime.now());
            newItem.setUpdatedAt(LocalDateTime.now());


            if (saleItemRow.getCategory() !=null) {
                Category  newCategory = categoryRepository.findByCategoryNameAndCompany(
                        saleItemRow.getCategory(), company);

                 if (newCategory == null){
                     newCategory.setCategoryName(saleItemRow.getCategory());
                     newCategory.setCompany(company);
                     newCategory.setCraetedAt(LocalDateTime.now());
                     newCategory.setUpdatedAt(LocalDateTime.now());

                     newCategory = categoryRepository.save(newCategory);
                     Set categorySet = new HashSet<>();
                     categorySet.add(newCategory);
                     newItem.setCategories(categorySet);
                 }

            }

            return newItem;

        });


       SaleItem newSaleItem = new SaleItem();

       newSaleItem = saleItemEntityMapper.toEntity(
               sale.get(),
               saleItemRow,
               item,
               newSaleItem
       );


        saleItemRepository.save(newSaleItem);

        saleCalculationService.recalculateSaleTotals(sale.get());
        saleRepository.save(sale.get());
    }
}
