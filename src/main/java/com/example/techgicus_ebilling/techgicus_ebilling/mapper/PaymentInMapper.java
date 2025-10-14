package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PaymentIn;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.paymentInDto.PaymentInRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.paymentInDto.PaymentInResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PaymentInMapper {

    PaymentIn convertRequestIntoEntity(PaymentInRequest paymentInRequest);

    PaymentInResponse convertEntityToResponse(PaymentIn paymentIn);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePaymentIn(PaymentInRequest paymentInRequest, @MappingTarget PaymentIn paymentIn);
}
