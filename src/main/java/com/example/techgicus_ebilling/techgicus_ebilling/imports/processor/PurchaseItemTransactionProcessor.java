package com.example.techgicus_ebilling.techgicus_ebilling.imports.processor;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Item;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Purchase;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseItem;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportContext;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.PurchaseItemRow;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.extractor.PurchaseItemRowExtractor;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper.PurchaseItemEntityMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ModelUtill;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PurchaseItemTransactionProcessor implements TransactionProcessor{

    private final PurchaseItemRowExtractor purchaseItemRowExtractor;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final PurchaseItemEntityMapper purchaseItemEntityMapper;


    public PurchaseItemTransactionProcessor(PurchaseItemRowExtractor purchaseItemRowExtractor, PurchaseRepository purchaseRepository, PurchaseItemRepository purchaseItemRepository, ItemRepository itemRepository, CategoryRepository categoryRepository, PurchaseItemEntityMapper purchaseItemEntityMapper) {
        this.purchaseItemRowExtractor = purchaseItemRowExtractor;
        this.purchaseRepository = purchaseRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.purchaseItemEntityMapper = purchaseItemEntityMapper;
    }

    @Override
    public boolean supports(String transactionType) {
        if (transactionType.toLowerCase().trim().equals("purchase-item"))return true;
        return false;
    }

    @Override
    public void process(Row row, Company company, ImportContext context) {

        PurchaseItemRow purchaseItemRow = purchaseItemRowExtractor.extract(row);

        Optional<Purchase> purchase = purchaseRepository.findByBillNumberAndCompany(purchaseItemRow.getBillNo(),company);

        if (purchase.isEmpty()){
            context.addError(row.getRowNum()+1,"Purchase not found for invoice : "+purchaseItemRow.getBillNo());
            return;
        }

        Item item = ModelUtill.findOrCreateItem(purchaseItemRow,
                company,
                itemRepository,
                categoryRepository);


        // 🔥 CLEAR ONLY ONCE PER SALE
        if (!context.isInitialized(purchase.get())) {

            purchase.get().getPurchaseItems().clear();
            purchase.get().getPurchasePayments().clear();

            context.markInitialized(purchase.get());
        }

        PurchaseItem newPurchaseItem = new PurchaseItem();

        newPurchaseItem = purchaseItemEntityMapper.toEntity(
                purchase.get(),
                purchaseItemRow,
                item,
                newPurchaseItem
        );

        Purchase existingPurchase = purchase.get();
        existingPurchase.getPurchaseItems().add(newPurchaseItem);

        purchaseRepository.save(existingPurchase);
    }
}
