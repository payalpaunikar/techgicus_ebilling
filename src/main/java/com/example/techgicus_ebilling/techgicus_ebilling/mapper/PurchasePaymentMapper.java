package com.example.techgicus_ebilling.techgicus_ebilling.mapper;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchasePayment;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto.PurchasePaymentRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto.PurchasePaymentResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")

public interface PurchasePaymentMapper {

    PurchasePayment convertPurchasePaymentRequestIntoPurchasePayment(PurchasePaymentRequest purchasePaymentRequest);

    PurchasePaymentResponse convertPurchasePaymentIntoPurchasePaymentResponse(PurchasePayment purchasePayment);

    List<PurchasePaymentResponse> convertPurchasePaymentListIntoPurchasePaymentResponseList(List<PurchasePayment> purchasePayments);
}
