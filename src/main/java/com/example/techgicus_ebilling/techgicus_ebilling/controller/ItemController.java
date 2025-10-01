package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto.CreatedProductItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto.CreatedServiceItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto.ItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto.UpdateItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ItemController {

    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/company/{companyId}/create/product-item")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ItemResponse> createdProductItemInCompany(@PathVariable Long companyId,
                                                                    @RequestBody CreatedProductItem createdProductItem){
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.createdProductItemInCompany(createdProductItem,companyId));
    }

    @PostMapping("/company/{companyId}/create/service-item")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ItemResponse> createdServiceItemInCompany(@PathVariable Long companyId,
                                                                    @RequestBody CreatedServiceItem createdServiceItem){
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.createdServiceItemInCompany(companyId,createdServiceItem));
    }


    @GetMapping("/item/{itemId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long itemId){
        return ResponseEntity.ok(itemService.getItemById(itemId));
    }


    @PutMapping("/item/{itemId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ItemResponse> updateItemById(@PathVariable Long itemId,
                                                       @RequestBody UpdateItemRequest updateItemRequest){
        return ResponseEntity.ok(itemService.updateItemById(itemId,updateItemRequest));
    }

    @DeleteMapping("/item/{itemId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteItemById(@PathVariable Long itemId){
        return ResponseEntity.ok(itemService.deleteItemById(itemId));
    }


    @GetMapping("/company/{companyId}/items")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<ItemResponse> getCompanyItems(@PathVariable Long companyId){
        return itemService.getCompanyItems(companyId);
    }
}
