package com.example.techgicus_ebilling.techgicus_ebilling.imports.processor;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportContext;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.SaleItemRow;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.extractor.SaleItemRowExtractor;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper.SaleReturnItemEntityMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SaleReturnItemTransactionProcessor implements TransactionProcessor{

    private final SaleItemRowExtractor saleItemRowExtractor;
    private final SaleReturnRepository saleReturnRepository;
    private final SaleReturnItemRepository saleReturnItemRepository;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final SaleReturnItemEntityMapper saleReturnItemEntityMapper;

    public SaleReturnItemTransactionProcessor(SaleItemRowExtractor saleItemRowExtractor, SaleReturnRepository saleReturnRepository, SaleReturnItemRepository saleReturnItemRepository, ItemRepository itemRepository, CategoryRepository categoryRepository, SaleReturnItemEntityMapper saleReturnItemEntityMapper) {
        this.saleItemRowExtractor = saleItemRowExtractor;
        this.saleReturnRepository = saleReturnRepository;
        this.saleReturnItemRepository = saleReturnItemRepository;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.saleReturnItemEntityMapper = saleReturnItemEntityMapper;
    }

    private final static Logger log = LoggerFactory.getLogger(SaleTransactionProcessor.class);


    @Override
    public boolean supports(String transactionType) {
        log.info("transaction type is : "+transactionType);
        if (transactionType.toLowerCase().trim().equals("credit note"))return true;
        return false;
    }

    @Override
    public void process(Row row, Company company, ImportContext context) {

        log.info("row number : "+row.getRowNum());

        SaleItemRow saleItemRow = saleItemRowExtractor.extract(row);

        Optional<SaleReturn> saleReturn = saleReturnRepository.findByReturnNoAndCompany(saleItemRow.getInvoiceNo(),company);

        if (saleReturn.isEmpty()){
            context.addError(row.getRowNum()+1,"Sale Return not found for invoice : "+saleItemRow.getInvoiceNo());
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
                Category newCategory = categoryRepository.findByCategoryNameAndCompany(
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


        SaleReturnItem newSaleReturnItem = new SaleReturnItem();

        newSaleReturnItem = saleReturnItemEntityMapper.toEntity(
                saleReturn.get(),
                saleItemRow,
                item,
                newSaleReturnItem
        );

        saleReturnItemRepository.save(newSaleReturnItem);



    }
}
