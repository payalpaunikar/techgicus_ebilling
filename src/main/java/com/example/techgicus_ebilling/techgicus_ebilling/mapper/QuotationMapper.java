package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Quotation;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.quotationDto.QuotationRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.quotationDto.QuotationResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface QuotationMapper {

    Quotation convertQuotationRequestIntoQuotation(QuotationRequest quotationRequest);

//    QuotationResponse convertQuotationIntoQuotationResponse(Quotation quotation);

//    QuotationResponse convertQuotationIntoQuotationResponse(Quotation quotation);

    QuotationResponse convertQuotationIntoQuotationResponse(Quotation quotation);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateQuotationByDto(QuotationRequest quotationRequest, @MappingTarget Quotation quotation);
}
