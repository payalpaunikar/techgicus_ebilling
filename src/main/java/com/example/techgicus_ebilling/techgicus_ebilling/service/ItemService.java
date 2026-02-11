package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.ItemType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.StockAdjustmentType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.StockTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto.*;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.*;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.ItemMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.StockTransactionMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ItemService {

         private ItemRepository itemRepository;
         private CompanyRepository companyRepository;
         private CategoryRepository categoryRepository;
         private ItemMapper itemMapper;
         private StockTransactionRepository stockTransactionRepository;
         private StockTransactionMapper stockTransactionMapper;
         private PurchaseBatchRepository purchaseBatchRepository;

    public ItemService(ItemRepository itemRepository, CompanyRepository companyRepository, CategoryRepository categoryRepository, ItemMapper itemMapper, StockTransactionRepository stockTransactionRepository, StockTransactionMapper stockTransactionMapper,  PurchaseBatchRepository purchaseBatchRepository) {
        this.itemRepository = itemRepository;
        this.companyRepository = companyRepository;
        this.categoryRepository = categoryRepository;
        this.itemMapper = itemMapper;
        this.stockTransactionRepository = stockTransactionRepository;
        this.stockTransactionMapper = stockTransactionMapper;
        this.purchaseBatchRepository = purchaseBatchRepository;
    }

    @Transactional
    public ItemResponse createdProductItemInCompany(CreatedProductItem createdProductItem, Long companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("company not fount with id : "+companyId));

        List<Category> categories = new ArrayList<>();
        if (createdProductItem.getCategoryIds() != null) {
             categories = categoryRepository.findCategoriesByIds(createdProductItem.getCategoryIds().stream().toList());
        }

        Optional<Item> optionalItem = itemRepository.findByItemNameAndCompany(createdProductItem.getItemName(),company);

       if (optionalItem.isPresent()){
           throw new ItemAlreadyExitException("Item already exit in the company.");
       }

        Item item = itemMapper.convertCreatedProductItemIntoItem(createdProductItem);
        item.setCompany(company);
        item.setCategories(categories.stream().collect(Collectors.toSet()));
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
//        item.setTotalStockIn(item.getStockOpeningQty());
//        item.setAvailableStock(item.getTotalStockIn());
//        item.setStockValue(item.getStockPricePerQty() * item.getStockOpeningQty());

        item.setMinimumStockToMaintain(createdProductItem.getMinimumStockToMaintain());
        item.setOpeningStockLocation(createdProductItem.getOpeningStockLocation());

        Item saveItem = itemRepository.save(item);

        // ===== OPENING STOCK =====
        Double openingQty = createdProductItem.getStockOpeningQty();
        Double openingRate = createdProductItem.getStockPricePerQty();

        if (openingQty != null && openingQty > 0) {

            double rate = openingRate != null ? openingRate : 0.0;
            LocalDate openingDate =
                    createdProductItem.getStockOpeningDate() != null
                            ? createdProductItem.getStockOpeningDate()
                            : LocalDate.now();

            // 1️⃣ FIFO BATCH (MOST IMPORTANT)
            PurchaseBatch batch = new PurchaseBatch();
            batch.setItem(saveItem);
            batch.setQtyPurchased(openingQty);
            batch.setQtyRemaining(openingQty);
            batch.setPricePerQty(BigDecimal.valueOf(openingRate));
            batch.setPurchaseDate(openingDate);
            batch.setPurchaseId(0L); // OPENING reference
            batch.setActive(true);

            purchaseBatchRepository.save(batch);

            // 2️⃣ STOCK TRANSACTION (USER HISTORY)
            StockTransaction tx = new StockTransaction();
            tx.setItem(saveItem);
            tx.setType(StockTransactionType.OPENING);
            tx.setTransactionDate(openingDate);
            tx.setReference("OPENING");
            tx.setQuantity(openingQty);
            tx.setRate(BigDecimal.valueOf(rate));
            tx.setAmount(BigDecimal.valueOf(openingQty).multiply(BigDecimal.valueOf(rate)));

            stockTransactionRepository.save(tx);

            // 3️⃣ Update Item snapshot
            item.setAvailableStock(openingQty);
            item.setStockValue(openingQty * openingRate);
            item.setStockPricePerQty(openingRate);

            itemRepository.save(item);
        }


        ItemResponse itemResponse = itemMapper.convertItemIntoItemResponse(saveItem);


        return itemResponse;
    }



    public ItemResponse createdServiceItemInCompany(Long companyId, CreatedServiceItem createdServiceItem){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("company not fount with id : "+companyId));

        List<Category> categories = categoryRepository.findCategoriesByIds(createdServiceItem.getCategoryIds().stream().toList());

        Optional<Item> optionalItem = itemRepository.findByItemNameAndCompany(createdServiceItem.getItemName(),company);

        if (optionalItem.isPresent()){
            throw new ItemAlreadyExitException("Item already exit in the company.");
        }

        Item item = itemMapper.convertCreatedServiceItemIntoItem(createdServiceItem);
        item.setCompany(company);
        item.setCategories(categories.stream().collect(Collectors.toSet()));
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());

        Item saveItem = itemRepository.save(item);

        ItemResponse itemResponse = itemMapper.convertItemIntoItemResponse(saveItem);

        return itemResponse;
    }


    public ItemResponse getItemById(Long itemId){
        Item item = itemRepository.findById(itemId)
                .orElseThrow(()-> new ResourceNotFoundException("Item not found with id : "+itemId));

        ItemResponse itemResponse = itemMapper.convertItemIntoItemResponse(item);

        return itemResponse;
    }


    public String deleteItemById(Long itemId){
        Item item = itemRepository.findById(itemId)
                .orElseThrow(()-> new ResourceNotFoundException("Item not found with id : "+itemId));

        List<StockTransaction> transactions =
                stockTransactionRepository.findByItem(item);

        boolean hasRealMovements = transactions.stream()
                .anyMatch(tx ->
                        tx.getType() != StockTransactionType.OPENING);


        if (hasRealMovements) {
            throw new ItemDeletionNotAllowedException(
                    "Item cannot be deleted because stock has been used in transactions.");
        }

        stockTransactionRepository.deleteAll(transactions);

        List<PurchaseBatch> batches =
                purchaseBatchRepository.findByItem(item);

        purchaseBatchRepository.deleteAll(batches);

        itemRepository.delete(item);

        return "Item delete successfully.";
    }

    @Transactional
    public ItemResponse updateItemById(Long itemId, UpdateItemRequest updateItem) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Item not found with id : " + itemId));

        Optional<Item> optionalItem =
                itemRepository.findByItemNameAndCompanyAndItemIdNot(
                        updateItem.getItemName(),
                        item.getCompany(),
                        itemId
                );

        if (optionalItem.isPresent()) {
            throw new ItemAlreadyExitException("Item already exists in the company.");
        }

        // ===== BASIC ITEM UPDATE =====
        itemMapper.updateItemFromDto(updateItem, item);



        if (updateItem.getCategoryIds() != null) {
            List<Category> categories =
                    categoryRepository.findCategoriesByIds(updateItem.getCategoryIds());
            item.setCategories(new HashSet<>(categories));
        }

        // 3️⃣ HANDLE OPENING STOCK UPDATE
        // ===============================
        Double newOpeningQty = updateItem.getStockOpeningQty();
        Double newOpeningRate = updateItem.getStockPricePerQty();

        if (newOpeningQty != null) {

            // 🔍 Find existing OPENING batch
            Optional<PurchaseBatch> openingBatchOpt =
                    purchaseBatchRepository.findByItemAndPurchaseId(item, 0L);

            // 🔍 Find OPENING stock transaction
            Optional<StockTransaction> openingTxOpt =
                    stockTransactionRepository.findByItemAndReference(item, "OPENING");


            // ===============================
            // CASE A: NO OPENING STOCK EARLIER
            // ===============================
            if (openingBatchOpt.isEmpty() && newOpeningQty > 0) {

                double rate = newOpeningRate != null ? newOpeningRate : 0.0;
                LocalDate openingDate =
                        updateItem.getStockOpeningDate() != null
                                ? updateItem.getStockOpeningDate()
                                : LocalDate.now();

                // FIFO batch
                PurchaseBatch batch = new PurchaseBatch();
                batch.setItem(item);
                batch.setPurchaseId(0L);
                batch.setQtyPurchased(newOpeningQty);
                batch.setQtyRemaining(newOpeningQty);
                batch.setPricePerQty(BigDecimal.valueOf(rate));
                batch.setPurchaseDate(openingDate);
                batch.setActive(true);

                purchaseBatchRepository.save(batch);


                // Stock transaction
                StockTransaction tx = new StockTransaction();
                tx.setItem(item);
                tx.setType(StockTransactionType.OPENING);
                tx.setReference("OPENING");
                tx.setQuantity(newOpeningQty);
                tx.setRate(BigDecimal.valueOf(rate));
                tx.setAmount(
                        BigDecimal.valueOf(newOpeningQty)
                                .multiply(BigDecimal.valueOf(rate))
                );
                tx.setTransactionDate(openingDate);

                stockTransactionRepository.save(tx);
            }

            // ===============================
            // CASE B: OPENING STOCK EXISTS → UPDATE
            // ===============================
            if (openingBatchOpt.isPresent() && openingTxOpt.isPresent()) {

                PurchaseBatch batch = openingBatchOpt.get();
                StockTransaction tx = openingTxOpt.get();

                double usedQty =
                        batch.getQtyPurchased() - batch.getQtyRemaining();

                if (newOpeningQty < usedQty) {
                    throw new InvalidStockOperationException(
                            "Cannot reduce opening stock below already used quantity (" + usedQty + ")");
                }

                double rate = newOpeningRate != null ? newOpeningRate : 0.0;

                // Update batch
                batch.setQtyPurchased(newOpeningQty);
                batch.setQtyRemaining(newOpeningQty - usedQty);
                batch.setPricePerQty(BigDecimal.valueOf(rate));
                batch.setActive(batch.getQtyRemaining() > 0);

                purchaseBatchRepository.save(batch);

                // Update stock transaction
                tx.setQuantity(newOpeningQty);
                tx.setRate(BigDecimal.valueOf(rate));
                tx.setAmount(
                        BigDecimal.valueOf(newOpeningQty)
                                .multiply(BigDecimal.valueOf(rate))
                );

                stockTransactionRepository.save(tx);
            }
        }

        // ===============================
        // 4️⃣ UPDATE ITEM SNAPSHOT (FINAL)
        // ===============================
        Double availableStock =
                stockTransactionRepository.totalStock(item.getItemId());

        Double stockValue =
                availableStock == null || availableStock <= 0
                        ? 0.0
                        : purchaseBatchRepository.sumRemainingValue(item.getItemId());

        item.setAvailableStock(availableStock != null ? availableStock : 0.0);
        item.setStockValue(stockValue != null ? stockValue : 0.0);
        item.setStockPricePerQty(updateItem.getStockPricePerQty());

        Item savedItem = itemRepository.save(item);

        return itemMapper.convertItemIntoItemResponse(savedItem);
    }



    public List<ItemResponse> getCompanyItems(Long companyId){

        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        List<Item> items = itemRepository.findAllByCompany(company);

        List<ItemResponse> itemResponses = itemMapper.convertItemListIntoItemResponseList(items);

        return itemResponses;
    }


//    @Transactional
//    public String updateStock(StockUpdateRequest request,Long itemId) {
//
//        Item item = itemRepository.findById(itemId)
//                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
//
//        Double qty = request.getQuantity();
//        Double price = request.getPricePerUnit();
//        LocalDate date = request.getTransactionDate();
//
//        if (qty <= 0) throw new IllegalArgumentException("Quantity must be positive");
//        if (price < 0) throw new IllegalArgumentException("Price must be 0 or positive");
//
//        double oldQty = item.getTotalStockIn();
//        double oldValue = item.getStockValue();
//
//        // ----------- ADD STOCK ------------
//        if (request.getOperationType() == StockTransactionType.ADD_STOCK) {
//
//            double addedValue = qty * price;
//
//
//            item.setTotalStockIn((item.getTotalStockIn() == null ? 0 : oldQty) + qty);
//            item.setAvailableStock(item.getTotalStockIn());
//            item.setStockValue((item.getStockValue() == null ? 0 : oldValue) + addedValue);
//
//            saveStockTransaction(item, qty, addedValue, StockTransactionType.ADD_STOCK, date, null);
//
//        }
//
//        // ----------- REDUCE STOCK ------------
//        else if (request.getOperationType() == StockTransactionType.REDUCE_STOCK) {
//
//            if (item.getAvailableStock() == null || item.getAvailableStock() < qty) {
//                throw new IllegalArgumentException("Insufficient stock to reduce");
//            }
//
//            double reducedValue = qty * price;
//
//            item.setTotalStockIn(oldQty - qty);
//            item.setAvailableStock(item.getTotalStockIn());
//            item.setStockValue(oldValue - reducedValue);
//
//            saveStockTransaction(item, qty, reducedValue, StockTransactionType.REDUCE_STOCK, date, "MANUAL_REDUCE_STOCK");
//        }
//
//        itemRepository.save(item);
//
//        return "Stock updated successfully";
//    }


    @Async
    public CompletableFuture<List<ItemSaleSummaryDto>> getItemSaleSummary(Long companyId){
        List<ItemSaleSummaryInterface> itemSaleSummaryInterfaces = itemRepository.findAllIteSaleSummary(companyId);

        List<ItemSaleSummaryDto> itemSaleSummaryDtos = itemSaleSummaryInterfaces.stream()
                .map(itemSaleSummaryInterface -> {
                    ItemSaleSummaryDto itemSaleSummaryDto = new ItemSaleSummaryDto();
                    itemSaleSummaryDto.setItemId(itemSaleSummaryInterface.getItemId());
                    itemSaleSummaryDto.setItemName(itemSaleSummaryInterface.getItemName());
                    itemSaleSummaryDto.setTotalSaleCount(itemSaleSummaryInterface.getTotalSaleCount());
                    return itemSaleSummaryDto;
                }).toList();

        return CompletableFuture.completedFuture(itemSaleSummaryDtos);
    }


    
        public List<StockTransactionDto> getStockTrasactioList(Long itemId){
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Item not found with id : " + itemId));

            List<StockTransaction> transactions =
                    stockTransactionRepository.findByItem(item);

            List<StockTransactionDto> stockTransactionDtos = transactions.stream()
                    .map(this::mapToDto)
                    .toList();

            stockTransactionDtos.sort(Comparator.comparing(StockTransactionDto::getId));

            return stockTransactionDtos;
        }

    private StockTransactionDto mapToDto(StockTransaction st) {

        StockTransactionDto dto = new StockTransactionDto();

        dto.setId(st.getId());
        dto.setTransactionDate(st.getTransactionDate());
        dto.setTransactionType(st.getType());
        dto.setReference(st.getReference());
        dto.setQuantity(st.getQuantity());

        return dto;
    }


    @Transactional
    public void adjustStock(StockAdjustmentRequest request) {

        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Item not found"));

        if (!item.getItemType().equals(ItemType.PRODUCT)) {
            throw new InvalidStockUpdateException("Stock adjustment allowed only for PRODUCT items");
        }

        Double qty = request.getQuantity();
        if (qty == null || qty <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }

        LocalDate date =
                request.getTransactionDate() != null
                        ? request.getTransactionDate()
                        : LocalDate.now();

        if (request.getType() == StockAdjustmentType.ADD) {
            addStock(item, qty, request.getPricePerUnit(), date);
        } else {
            reduceStock(item, qty, date);
        }
    }

    private void addStock(
            Item item,
            Double qty,
            Double rate,
            LocalDate date
//            String reason
    ) {
        if (rate == null || rate <= 0) {
            throw new RuntimeException("Price per unit is required for stock addition");
        }

        // 1️⃣ FIFO Batch
        PurchaseBatch batch = new PurchaseBatch();
        batch.setItem(item);
        batch.setPurchaseId(-1L); // ADJUSTMENT reference
        batch.setQtyPurchased(qty);
        batch.setQtyRemaining(qty);
        batch.setPricePerQty(BigDecimal.valueOf(rate));
        batch.setPurchaseDate(date);
        batch.setActive(true);

        purchaseBatchRepository.save(batch);

        // 2️⃣ Stock Transaction
        StockTransaction tx = new StockTransaction();
        tx.setItem(item);
        tx.setType(StockTransactionType.ADJUSTMENT_IN);
        tx.setTransactionDate(date);
        tx.setQuantity(qty);
        tx.setRate(BigDecimal.valueOf(rate));
        tx.setAmount(BigDecimal.valueOf(qty).multiply(BigDecimal.valueOf(rate)));
        tx.setReference("STOCK-ADD");
//        tx.setRemark(reason);

        stockTransactionRepository.save(tx);

        // 3️⃣ Update Item Snapshot
        Double availableStock =
                stockTransactionRepository.totalStock(item.getItemId());

        Double stockValue =
                purchaseBatchRepository.sumRemainingValue(item.getItemId());

        item.setAvailableStock(availableStock);
        item.setStockValue(stockValue);
        item.setStockPricePerQty(rate);

        itemRepository.save(item);
    }


    private void reduceStock(
            Item item,
            Double qtyToReduce,
            LocalDate date
    ) {
        Double availableStock =
                stockTransactionRepository.totalStock(item.getItemId());

        if (availableStock == null || availableStock < qtyToReduce) {
            throw new RuntimeException(
                    "Insufficient stock. Available: " + availableStock);
        }

        double remainingToReduce = qtyToReduce;

        // 1️⃣ FIFO consume batches
        List<PurchaseBatch> batches =
                purchaseBatchRepository.findAvailableBatchesFIFO(item);

        for (PurchaseBatch batch : batches) {
            if (remainingToReduce <= 0) break;

            double used =
                    Math.min(batch.getQtyRemaining(), remainingToReduce);

            batch.setQtyRemaining(batch.getQtyRemaining() - used);
            remainingToReduce -= used;

            if (batch.getQtyRemaining() == 0) {
                batch.setActive(false);
            }

            purchaseBatchRepository.save(batch);
        }

        // 2️⃣ Stock Transaction
        StockTransaction tx = new StockTransaction();
        tx.setItem(item);
        tx.setType(StockTransactionType.ADJUSTMENT_OUT);
        tx.setTransactionDate(date);
        tx.setQuantity(qtyToReduce);
        tx.setRate(BigDecimal.ZERO); // rate not needed
        tx.setAmount(BigDecimal.ZERO);
        tx.setReference("STOCK-REDUCE");
//        tx.setRemark(reason);

        stockTransactionRepository.save(tx);

        // 3️⃣ Update Item Snapshot
        Double updatedStock =
                stockTransactionRepository.totalStock(item.getItemId());

        Double stockValue =
                updatedStock <= 0
                        ? 0.0
                        : purchaseBatchRepository.sumRemainingValue(item.getItemId());

        item.setAvailableStock(updatedStock);
        item.setStockValue(stockValue);

        itemRepository.save(item);
    }


}
