package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Category;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Item;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto.*;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.ItemMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CategoryRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CompanyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ItemService {

         private ItemRepository itemRepository;
         private CompanyRepository companyRepository;
         private CategoryRepository categoryRepository;
         private ItemMapper itemMapper;

    @Autowired
    public ItemService(ItemRepository itemRepository, CompanyRepository companyRepository, CategoryRepository categoryRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.companyRepository = companyRepository;
        this.categoryRepository = categoryRepository;
        this.itemMapper = itemMapper;
    }


    public ItemResponse createdProductItemInCompany(CreatedProductItem createdProductItem,Long companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("company not fount with id : "+companyId));

        List<Category> categories = categoryRepository.findCategoriesByIds(createdProductItem.getCategoryIds().stream().toList());

        Item item = itemMapper.convertCreatedProductItemIntoItem(createdProductItem);
        item.setCompany(company);
        item.setCategories(categories.stream().collect(Collectors.toSet()));
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());

        Item saveItem = itemRepository.save(item);

        ItemResponse itemResponse = itemMapper.convertItemIntoItemResponse(saveItem);

        return itemResponse;
    }



    public ItemResponse createdServiceItemInCompany(Long companyId, CreatedServiceItem createdServiceItem){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("company not fount with id : "+companyId));

        List<Category> categories = categoryRepository.findCategoriesByIds(createdServiceItem.getCategoryIds().stream().toList());

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

        return "Item delete succefully";
    }

    public ItemResponse updateItemById(Long itemId, UpdateItemRequest updateItem){
        Item item = itemRepository.findById(itemId)
                .orElseThrow(()-> new ResourceNotFoundException("Item not found with id : "+itemId));

         itemMapper.updateItemFromDto(updateItem,item);

        List<Category> categories = categoryRepository.findCategoriesByIds(updateItem.getCategoryIds().stream().toList());

        item.getCategories().clear();
        item.setCategories((Set<Category>) categories);

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
}
