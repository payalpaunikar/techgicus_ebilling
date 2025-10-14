package com.example.techgicus_ebilling.techgicus_ebilling.mapper;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SalePayment;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SalePaymentRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SalePaymentResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SalePaymentMapper {

    SalePayment convertSalePaymentRequestIntoSalePayment(SalePaymentRequest salePaymentRequest);
    SalePaymentResponse convertSalePaymentIntoSalePaymentResponse(SalePayment salePayment);

    List<SalePaymentResponse> convertSalePaymentListIntoSalePaymentResponseList(List<SalePayment> salePayments);

}
