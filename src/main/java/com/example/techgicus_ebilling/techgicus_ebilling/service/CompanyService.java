package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.User;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.BussinessType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.companyDto.CompanyResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.companyDto.UpdateCompanyRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.BadRequestException;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.CompanyMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CompanyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompanyService {

    private UserRepository userRepository;
    private CompanyRepository companyRepository;
    private FileService fileService;
    private CompanyMapper companyMapper;


    @Autowired
    public CompanyService(UserRepository userRepository, CompanyRepository companyRepository, FileService fileService, CompanyMapper companyMapper) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.fileService = fileService;
        this.companyMapper = companyMapper;
    }

    public CompanyResponse createCompany(String bussinessName,
                                         String bussinessDescription, String phoneNo, String emailId,
                                         String address, BussinessType bussinessType, String gstin,
                                         String bussinessCategory, State state,
                                         MultipartFile logo, MultipartFile signature,
                                         Long userId, String bankName, String accountNo,
                                         String ifscCode, String accountHolderName,
                                         String upiId, String city){

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id : "+userId));

        String logoUrl = null;
        String signatureUrl = null;
        try {
            // ✅ Only upload if file is not null and not empty
            if (logo != null && !logo.isEmpty()) {
                logoUrl = fileService.uploadFile(logo);
            }

            if (signature != null && !signature.isEmpty()) {
                signatureUrl = fileService.uploadFile(signature);
            }

            Company newCompany = new Company();
            newCompany.setCompanyOwner(user);
            newCompany.setAddress(address);
            newCompany.setBussinessName(bussinessName);
            newCompany.setBusinessDescription(bussinessDescription);
            newCompany.setBussinessCategory(bussinessCategory);
            newCompany.setPhoneNo(phoneNo);
            newCompany.setEmailId(emailId);
            newCompany.setState(state);
            newCompany.setLogoUrl(logoUrl);
            newCompany.setSignatureUrl(signatureUrl);
            newCompany.setBussinessType(bussinessType);
            newCompany.setGstin(gstin);
            newCompany.setCreatedAt(LocalDateTime.now());
            newCompany.setUpdateAt(LocalDateTime.now());
            newCompany.setBankName(bankName);
            newCompany.setAccountNo(accountNo);
            newCompany.setIfscCode(ifscCode);
            newCompany.setAccountHolderName(accountHolderName);
            newCompany.setUpiId(upiId);
            newCompany.setCity(city);

            Company saveCompany = companyRepository.save(newCompany);

            CompanyResponse companyResponse = companyMapper.convertCompanyIntoCompanyResponse(saveCompany);

            return companyResponse;

        }
        catch (IOException exception){
            // Handle the exception — you can log and return an error response
            exception.printStackTrace(); // or use a logger
            throw new RuntimeException("Failed to upload file(s): " + exception.getMessage());
        }

    }


    public List<CompanyResponse> getCompaniesByOwnerId(Long ownerId){

        User owner = userRepository.findById(ownerId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id : "+ownerId));

        List<Company> companies = companyRepository.findAllByCompanyOwner(owner);

        List<CompanyResponse> companyResponses = companyMapper.convertCompaniesIntoCompanyRsponses(companies);

        return companyResponses;
    }


    public CompanyResponse updateCompanyByCompanyId(Long companyId,String bussinessName, String bussinessDescription, String phoneNo, String emailId,
                                                    String address, BussinessType bussinessType, String gstin,
                                                    String bussinessCategory, State state,
                                                    MultipartFile logo, MultipartFile signature,String bankName,String accountNo,
                                                    String ifscCode,String accountHolderName,
                                                    String upiId,String city){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        String logoUrl = null;
        String signatureUrl = null;
        try {
            // ✅ Only upload if file is not null and not empty
            if (logo != null && !logo.isEmpty()) {
                logoUrl = fileService.uploadFile(logo);
            }

            if (signature != null && !signature.isEmpty()) {
                signatureUrl = fileService.uploadFile(signature);
            }

            company.setGstin(gstin);
            company.setBussinessName(bussinessName);
            company.setBussinessType(bussinessType);
            company.setBussinessCategory(bussinessCategory);
            company.setState(state);
            company.setBusinessDescription(bussinessDescription);
            company.setLogoUrl(logoUrl);
            company.setSignatureUrl(signatureUrl);
            company.setEmailId(emailId);
            company.setPhoneNo(phoneNo);
            company.setAddress(address);
            company.setUpdateAt(LocalDateTime.now());
            company.setBankName(bankName);
            company.setAccountNo(accountNo);
            company.setIfscCode(ifscCode);
            company.setAccountHolderName(accountHolderName);
            company.setUpiId(upiId);
            company.setCity(city);

            Company saveCompany =  companyRepository.save(company);

            CompanyResponse companyResponse = companyMapper.convertCompanyIntoCompanyResponse(saveCompany);

            return companyResponse;

        }catch (IOException exception){
            // Handle the exception — you can log and return an error response
            exception.printStackTrace(); // or use a logger
            throw new RuntimeException("Failed to upload file(s): " + exception.getMessage());
        }

    }


    public String deleteCompanyById(Long companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        try {
            companyRepository.delete(company);
        }
        catch (DataIntegrityViolationException exception){
            throw new BadRequestException("Cannot delete company because it contain the transaction, please delete all transaction of before delete the company.");
        }

        return "Company Delete succefully.";
    }


    public CompanyResponse getCompanyById(Long companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        CompanyResponse companyResponse = companyMapper.convertCompanyIntoCompanyResponse(company);

        return companyResponse;
    }

    public Boolean isAnyCompanyCreatedByOwner(Long ownerId){

        Long companyCountOfOwner = companyRepository.countByCompanyOwner_UserId(ownerId);

        if (companyCountOfOwner <= 0){
           return false;
        }
        return true;
    }
}
