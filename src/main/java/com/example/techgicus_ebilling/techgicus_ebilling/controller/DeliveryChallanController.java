package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.ChallanType;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.deliveryChallanDto.DeliveryChallanRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.deliveryChallanDto.DeliveryChallanResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.service.DeliveryChallanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DeliveryChallanController {

      private DeliveryChallanService deliveryChallanService;

    @Autowired
    public DeliveryChallanController(DeliveryChallanService deliveryChallanService) {
        this.deliveryChallanService = deliveryChallanService;
    }


    @PostMapping("/company/{companyId}/create/delivery-challan")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DeliveryChallanResponse> createDeliveryChallan(@PathVariable Long companyId, @RequestBody DeliveryChallanRequest deliveryChallanRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryChallanService.createDeliveryChallan(companyId,deliveryChallanRequest));
    }


    @GetMapping("/company/{companyId}/get/delivery-challan/list/by")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<DeliveryChallanResponse> getDeliveryChallanListByFilter(@PathVariable Long companyId,
                                                                        @RequestParam(required = false)ChallanType challanType){
        return deliveryChallanService.getDeliveryChallanListByFilter(companyId,challanType);
    }


    @GetMapping("/delivery-challan/{deliveryChallanId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DeliveryChallanResponse getDeliveryChallanById(@PathVariable Long deliveryChallanId){
        return deliveryChallanService.getDeliveryChallanById(deliveryChallanId);
    }


    @PutMapping("/delivery-challan/{deliveryChallanId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DeliveryChallanResponse updateDeliveryChallanById(@PathVariable Long deliveryChallanId,@RequestBody DeliveryChallanRequest deliveryChallanRequest){
        return deliveryChallanService.updateDeliveryChallanById(deliveryChallanId,deliveryChallanRequest);
    }

    @DeleteMapping("/delivery-challan/{deliveryChallanId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteDeliveryChallanById(@PathVariable Long deliveryChallanId){
        return ResponseEntity.ok(deliveryChallanService.deleteDeliveryChallanById(deliveryChallanId));
    }


    @PostMapping("/delivery-challan/{deliveryChallanId}/convert/to/sale")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SaleResponse convertDeliveryChallanToSale(@PathVariable Long deliveryChallanId, @RequestBody SaleRequest saleRequest){
        return deliveryChallanService.convertDeliveryChallanToSale(deliveryChallanId,saleRequest);
    }
}
