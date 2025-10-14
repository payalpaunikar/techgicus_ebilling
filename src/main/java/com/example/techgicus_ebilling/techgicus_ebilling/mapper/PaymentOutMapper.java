package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PaymentIn;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PaymentOut;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.paymentOutDto.PaymentOutRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.paymentOutDto.PaymentOutResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PaymentOutMapper {

    PaymentOut convertRequestToEntity(PaymentOutRequest paymentOutRequest);
    PaymentOutResponse convertEntityToResponse(PaymentOut paymentOut);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(PaymentOutRequest paymentOutRequest, @MappingTarget PaymentOut paymentOut);
}
