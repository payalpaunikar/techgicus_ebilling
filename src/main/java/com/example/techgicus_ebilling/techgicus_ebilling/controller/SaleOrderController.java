package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.OrderType;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleOrderDto.SaleOrderRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleOrderDto.SaleOrderResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SaleOrderController {

       private SaleOrderService saleOrderService;

    @Autowired
    public SaleOrderController(SaleOrderService saleOrderService) {
        this.saleOrderService = saleOrderService;
    }


    @PostMapping("/company/{companyId}/create/sale-order")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SaleOrderResponse> createSaleOrder(@PathVariable Long companyId,@RequestBody SaleOrderRequest saleOrderRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(saleOrderService.createSaleOrder(companyId,saleOrderRequest));
    }


    @GetMapping("/company/{companyId}/sale-order/list/according")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<SaleOrderResponse> getSaleOrderListByCompanyIdAndOrderType(@PathVariable Long companyId,
                                                                           @RequestParam(required = false)OrderType orderType){
        return saleOrderService.getAllSaleOrderByCompanyIdAndOrderType(companyId,orderType);
    }


    @GetMapping("/sale-order/{saleOrderId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SaleOrderResponse> getSaleOrderById(@PathVariable Long saleOrderId){
        return ResponseEntity.ok(saleOrderService.getSaleOrderById(saleOrderId));
    }


    @PutMapping("/sale-order/{saleOrderId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SaleOrderResponse> updateSaleOrderById(@PathVariable Long saleOrderId,@RequestBody SaleOrderRequest saleOrderRequest){
        return ResponseEntity.ok(saleOrderService.updateSaleOrderById(saleOrderId,saleOrderRequest));

    }


    @DeleteMapping("/sale-order/{saleOrderId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteSaleOrderById(@PathVariable Long saleOrderId){
        return ResponseEntity.ok(saleOrderService.deleteSaleOrderById(saleOrderId));
    }


    @PostMapping("/sale-order/{saleOrderId}/convert/to/sale")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SaleResponse> convertSaleOrderToSale(@PathVariable Long saleOrderId,
                                                               @RequestBody SaleRequest saleRequest){
        return ResponseEntity.ok(saleOrderService.convertSaleOrderToSale(saleOrderId,saleRequest));
    }
}
