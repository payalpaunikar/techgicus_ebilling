package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Category;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Item;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.StockTransaction;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.ItemType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.StockTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto.*;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ItemAlreadyExitException;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.ItemMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.StockTransactionMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CategoryRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CompanyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.ItemRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.StockTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ItemService(ItemRepository itemRepository, CompanyRepository companyRepository, CategoryRepository categoryRepository, ItemMapper itemMapper, StockTransactionRepository stockTransactionRepository, StockTransactionMapper stockTransactionMapper) {
        this.itemRepository = itemRepository;
        this.companyRepository = companyRepository;
        this.categoryRepository = categoryRepository;
        this.itemMapper = itemMapper;
        this.stockTransactionRepository = stockTransactionRepository;
        this.stockTransactionMapper = stockTransactionMapper;
    }

    @Transactional
    public ItemResponse createdProductItemInCompany(CreatedProductItem createdProductItem, Long companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("company not fount with id : "+companyId));

        List<Category> categories = categoryRepository.findCategoriesByIds(createdProductItem.getCategoryIds().stream().toList());

        Optional<Item> optionalItem = itemRepository.findByItemNameAndCompany(createdProductItem.getItemName(),company);

       if (optionalItem.isPresent()){
           throw new ItemAlreadyExitException("Item already exit in the company.");
       }

        Item item = itemMapper.convertCreatedProductItemIntoItem(createdProductItem);
        item.setCompany(company);
        item.setCategories(categories.stream().collect(Collectors.toSet()));
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        item.setTotalStockIn(item.getStockOpeningQty());
        item.setAvailableStock(item.getTotalStockIn());
        item.setStockValue(item.getStockPricePerQty() * item.getStockOpeningQty());

        Item saveItem = itemRepository.save(item);

        StockTransaction stockTransaction = new StockTransaction();
        stockTransaction.setItem(item);
        stockTransaction.setTransactionDate(item.getStockOpeningDate());
        stockTransaction.setQuantity(item.getStockOpeningQty() == null ? 0.0:item.getStockOpeningQty());
        stockTransaction.setTransactionType(StockTransactionType.OPENING_STOCK);
        stockTransaction.setTotalAmount(item.getStockPricePerQty() * item.getStockOpeningQty());

        stockTransactionRepository.save(stockTransaction);

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

        itemRepository.delete(item);

        return "Item delete successfully.";
    }

    public ItemResponse updateItemById(Long itemId, UpdateItemRequest updateItem){
        Item item = itemRepository.findById(itemId)
                .orElseThrow(()-> new ResourceNotFoundException("Item not found with id : "+itemId));

        Optional<Item> optionalItem = itemRepository.findByItemNameAndCompanyAndItemIdNot(updateItem.getItemName(),item.getCompany(),itemId);

        if (optionalItem.isPresent()){
            throw new ItemAlreadyExitException("Item already exit in the company.");
        }

         itemMapper.updateItemFromDto(updateItem,item);

        List<Category> categories = categoryRepository.findCategoriesByIds(updateItem.getCategoryIds().stream().toList());

        item.getCategories().clear();

        item.setCategories(new HashSet<>(categories));

         Item saveItem = itemRepository.save(item);

         ItemResponse itemResponse = itemMapper.convertItemIntoItemResponse(saveItem);

         return itemResponse;
    }


    public List<ItemResponse> getCompanyItems(Long companyId){

        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        List<Item> items = itemRepository.findAllByCompany(company);

        List<ItemResponse> itemResponses = itemMapper.convertItemListIntoItemResponseList(items);

        return itemResponses;
    }


    @Transactional
    public String updateStock(StockUpdateRequest request,Long itemId) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        Double qty = request.getQuantity();
        Double price = request.getPricePerUnit();
        LocalDate date = request.getTransactionDate();

        if (qty <= 0) throw new IllegalArgumentException("Quantity must be positive");
        if (price < 0) throw new IllegalArgumentException("Price must be 0 or positive");

        double oldQty = item.getTotalStockIn();
        double oldValue = item.getStockValue();

        // ----------- ADD STOCK ------------
        if (request.getOperationType() == StockTransactionType.ADD_STOCK) {

            double addedValue = qty * price;


            item.setTotalStockIn((item.getTotalStockIn() == null ? 0 : oldQty) + qty);
            item.setAvailableStock(item.getTotalStockIn());
            item.setStockValue((item.getStockValue() == null ? 0 : oldValue) + addedValue);

            saveStockTransaction(item, qty, addedValue, StockTransactionType.ADD_STOCK, date, null);

        }

        // ----------- REDUCE STOCK ------------
        else if (request.getOperationType() == StockTransactionType.REDUCE_STOCK) {

            if (item.getAvailableStock() == null || item.getAvailableStock() < qty) {
                throw new IllegalArgumentException("Insufficient stock to reduce");
            }

            double reducedValue = qty * price;

            item.setTotalStockIn(oldQty - qty);
            item.setAvailableStock(item.getTotalStockIn());
            item.setStockValue(oldValue - reducedValue);

            saveStockTransaction(item, qty, reducedValue, StockTransactionType.REDUCE_STOCK, date, "MANUAL_REDUCE_STOCK");
        }

        itemRepository.save(item);

        return "Stock updated successfully";
    }

    private void saveStockTransaction(Item item, Double qty, Double amount,
                                      StockTransactionType type, LocalDate date,
                                      String reference) {

        StockTransaction tx = new StockTransaction();
        tx.setItem(item);
        tx.setTransactionDate(date);
        tx.setQuantity(qty);
        tx.setTotalAmount(amount);
        tx.setTransactionType(type);
        tx.setReferenceNumber(reference);// manual operation
        stockTransactionRepository.save(tx);

    }



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
                     .orElseThrow(()-> new ResourceNotFoundException("Item not found with id : "+itemId));

             if (item.getItemType().equals(ItemType.PRODUCT)) {
                 List<StockTransaction> stockTransactionList = stockTransactionRepository.findByItem(item);

                 List<StockTransactionDto> stockTransactionDtos = stockTransactionList.stream()
                         .map(stockTransaction -> stockTransactionMapper.toDto(stockTransaction))
                         .toList();

                 return stockTransactionDtos;
             }

             else{
                 return new ArrayList<>();
             }
        }




}
