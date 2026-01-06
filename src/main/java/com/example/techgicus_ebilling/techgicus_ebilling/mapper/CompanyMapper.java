package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.companyDto.CompanyResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.companyDto.UpdateCompanyRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    @Mapping(target = "businessDescription",source = "businessDescription")
    @Mapping(target = "phoneNo",source = "phoneNo")
    @Mapping(target = "bankName",source = "bankName")
    @Mapping(target = "accountNo",source = "accountNo")
    @Mapping(target = "ifscCode",source = "ifscCode")
    @Mapping(target = "accountHolderName",source = "accountHolderName")
    @Mapping(target = "upiId",source = "upiId")
    CompanyResponse convertCompanyIntoCompanyResponse(Company company);

    List<CompanyResponse> convertCompaniesIntoCompanyRsponses(List<Company> companies);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCompanyFromDto(UpdateCompanyRequest updateCompanyRequest,@MappingTarget Company company);
}
