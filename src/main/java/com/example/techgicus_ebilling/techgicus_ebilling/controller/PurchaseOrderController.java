package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.OrderType;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto.PurchaseRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto.PurchaseResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseOrderDto.PurchaseOrderRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseOrderDto.PurchaseOrderResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleOrderDto.SaleOrderRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleOrderDto.SaleOrderResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PurchaseOrderController {

       private PurchaseOrderService purchaseOrderService;

    @Autowired
    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }


    @PostMapping("/company/{companyId}/created/purchase-order")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PurchaseOrderResponse> createdPurchaseOrder(@PathVariable Long companyId, @RequestBody PurchaseOrderRequest purchaseOrderRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseOrderService.createdPurchaseOrder(companyId,purchaseOrderRequest));
    }

    @GetMapping("/company/{companyId}/purchase-order/list/according")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<PurchaseOrderResponse> getPurchaseOrderListByCompanyIdAndOrderType(@PathVariable Long companyId,
                                                                           @RequestParam(required = false) OrderType orderType){
        return purchaseOrderService.getAllPurchaseOrderByCompanyIdAndOrderType(companyId,orderType);
    }


    @GetMapping("/purchase-order/{purchaseOrderId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PurchaseOrderResponse> getPurchaseOrderById(@PathVariable Long purchaseOrderId){
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrderById(purchaseOrderId));
    }


    @PutMapping("/purchase-order/{purchaseOrderId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PurchaseOrderResponse> updatePurchaseOrderById(@PathVariable Long purchaseOrderId,@RequestBody PurchaseOrderRequest purchaseOrderRequest){
        return ResponseEntity.ok(purchaseOrderService.updatePurchaseOrderById(purchaseOrderId,purchaseOrderRequest));

    }


    @DeleteMapping("/purchase-order/{purchaseOrderId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deletePurchaseOrderById(@PathVariable Long purchaseOrderId){
        return ResponseEntity.ok(purchaseOrderService.deletePurchaseOrderById(purchaseOrderId));
    }


    @PostMapping("/purchase-order/{purchaseOrderId}/convert/to/sale")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PurchaseResponse> convertPurchaseOrderToPurchase(@PathVariable Long purchaseOrderId,
                                                                           @RequestBody PurchaseRequest purchaseRequest){
        return ResponseEntity.ok(purchaseOrderService.convertPurchaseOrderToPurchase(purchaseOrderId,purchaseRequest));
    }
}
