package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto.*;
import com.example.techgicus_ebilling.techgicus_ebilling.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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



    @Operation(
            summary = "Update stock for an item",
            description = """
                    This API allows adding or reducing stock for a specific item.
                    
                    **ADD STOCK**
                    - Increases item quantity
                    - Updates stock value
                   
                    
                    **REDUCE STOCK**
                    - Decreases item quantity
                    - Prevents stock from going negative
                    
                    The StockTransactionType must be either **ADD_STOCK** or **REDUCE_STOCK**.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Stock updated successfully",
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or insufficient stock",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/item/{itemId}/stock/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateStock(@RequestBody StockUpdateRequest request,@PathVariable Long itemId){
        return ResponseEntity.ok(itemService.updateStock(request,itemId));
    }


    @GetMapping("/item/{itemId}/stock-transaction")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<StockTransactionDto> getItemStockTransactionList(@PathVariable Long itemId){
      return itemService.getStockTrasactioList(itemId);
    }


}
