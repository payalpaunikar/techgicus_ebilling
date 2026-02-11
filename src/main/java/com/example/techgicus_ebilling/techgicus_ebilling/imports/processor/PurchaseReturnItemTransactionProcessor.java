package com.example.techgicus_ebilling.techgicus_ebilling.imports.processor;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Item;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseReturn;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseReturnItem;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportContext;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.PurchaseItemRow;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.extractor.PurchaseItemRowExtractor;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper.PurchaseItemEntityMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper.PurchaseReturnItemEntityMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ModelUtill;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CategoryRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.ItemRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PurchaseReturnRepository;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PurchaseReturnItemTransactionProcessor implements TransactionProcessor{

    private final PurchaseItemRowExtractor purchaseItemRowExtractor;
    private final PurchaseReturnRepository purchaseReturnRepository;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final PurchaseReturnItemEntityMapper purchaseReturnItemEntityMapper;

    public PurchaseReturnItemTransactionProcessor(PurchaseItemRowExtractor purchaseItemRowExtractor, PurchaseReturnRepository purchaseReturnRepository, ItemRepository itemRepository, CategoryRepository categoryRepository, PurchaseReturnItemEntityMapper purchaseReturnItemEntityMapper) {
        this.purchaseItemRowExtractor = purchaseItemRowExtractor;
        this.purchaseReturnRepository = purchaseReturnRepository;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.purchaseReturnItemEntityMapper = purchaseReturnItemEntityMapper;
    }

    @Override
    public boolean supports(String transactionType) {
        if (transactionType.toLowerCase().trim().equals("debit-note-item"))return true;
        return false;
    }

    @Override
    public void process(Row row, Company company, ImportContext context) {

        PurchaseItemRow purchaseItemRow = purchaseItemRowExtractor.extract(row);

        Optional<PurchaseReturn> purchaseReturn = purchaseReturnRepository.findByReturnNoAndCompany(purchaseItemRow.getBillNo(),company);

        if (purchaseReturn.isEmpty()){
            if (purchaseItemRow.getBillNo() == null || purchaseItemRow.getBillNo().isEmpty()){
                context.addError(row.getSheet().getSheetName(),row.getRowNum()+1,"Invoice number is missing. Purchase items require an existing Purchase Invoice.");
                return;
            }
            context.addError(row.getSheet().getSheetName(),row.getRowNum()+1,"Purchase Return not found for invoice : "+purchaseItemRow.getBillNo());
            return;
        }

        Item item = ModelUtill.findOrCreateItem(purchaseItemRow,
                company,
                itemRepository,
                categoryRepository);

        if (!context.isInitialized(purchaseReturn.get())) {

            purchaseReturn.get().getPurchaseReturnItems().clear();
            // saleReturn.get().getSalePayments().clear();

            context.markInitialized(purchaseReturn.get());
        }

        PurchaseReturnItem newPurchaseReturnItem = new PurchaseReturnItem();

        newPurchaseReturnItem = purchaseReturnItemEntityMapper.toEntity(
                purchaseReturn.get(),
                purchaseItemRow,
                item,
                newPurchaseReturnItem
        );


        PurchaseReturn existingPurchaseReturn = purchaseReturn.get();
        existingPurchaseReturn.getPurchaseReturnItems().add(newPurchaseReturnItem);

        purchaseReturnRepository.save(existingPurchaseReturn);

    }
}
