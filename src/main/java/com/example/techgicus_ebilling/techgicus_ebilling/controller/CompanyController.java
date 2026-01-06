package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.BussinessType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.companyDto.CompanyResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.companyDto.UpdateCompanyRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class CompanyController {

     private CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }


    @PostMapping("/create/company/by/owner/{ownerId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CompanyResponse> createdCompany(
            @RequestPart(value = "bussinessName",required = true)String bussinessName,
            @RequestPart(value = "businessDescription",required = false) String businessDescription,
            @RequestPart(value = "phoneNo",required = true) String phoneNo,
            @RequestPart(value = "emailId",required = false) String emailId,
            @RequestPart(value = "address",required = true) String address,
            @RequestPart(value = "bussinessType",required = false) String bussinessType,
            @RequestPart(value = "gstin",required = true)  String gstin,
            @RequestPart(value = "bussinessCategory",required = false) String bussinessCategory,
            @RequestPart(value = "state",required = true) String state,
            @RequestPart(value = "logo",required = false) MultipartFile logo,
            @RequestPart(value = "signature",required = false)  MultipartFile signature,
            @RequestPart(value = "bankName",required = true) String bankName,
            @RequestPart(value = "accountNo",required = true) String accountNo,
            @RequestPart(value = "ifscCode",required = true) String ifscCode,
            @RequestPart(value = "accountHolderName",required = true) String accountHolderName,
            @RequestPart(value = "upiId",required = true) String upiId,
            @PathVariable Long ownerId

    ){
        return ResponseEntity.ok(companyService.createCompany(bussinessName,businessDescription,phoneNo,emailId,address,BussinessType.valueOf(bussinessType.toUpperCase()),
                gstin,bussinessCategory,State.valueOf(state.toUpperCase()),logo,signature,ownerId,bankName,accountNo,
                ifscCode,accountHolderName,upiId));
    }



    @GetMapping("/owner/{ownerId}/companies")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<CompanyResponse> getCompaniesByOwnerId(@PathVariable Long ownerId){
        return companyService.getCompaniesByOwnerId(ownerId);
    }


    @PutMapping("/update/company/{companyId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CompanyResponse> updateCompanyByCompanyId(@PathVariable Long companyId, @RequestPart(value = "bussinessName",required = true)String bussinessName,
                                                                    @RequestPart(value = "businessDescription",required = false) String businessDescription,
                                                                    @RequestPart(value = "phoneNo",required = true) String phoneNo,
                                                                    @RequestPart(value = "emailId",required = false) String emailId,
                                                                    @RequestPart(value = "address",required = true) String address,
                                                                    @RequestPart(value = "bussinessType",required = false) String bussinessType,
                                                                    @RequestPart(value = "gstin",required = true)  String gstin,
                                                                    @RequestPart(value = "bussinessCategory",required = false) String bussinessCategory,
                                                                    @RequestPart(value = "state",required = true) String state,
                                                                    @RequestPart(value = "logo",required = false) MultipartFile logo,
                                                                    @RequestPart(value = "signature",required = false)  MultipartFile signature,
                                                                    @RequestPart(value = "bankName",required = true) String bankName,
                                                                    @RequestPart(value = "accountNo",required = true) String accountNo,
                                                                    @RequestPart(value = "ifscCode",required = true) String ifscCode,
                                                                    @RequestPart(value = "accountHolderName",required = true) String accountHolderName,
                                                                    @RequestPart(value = "upiId",required = false) String upiId){
       return ResponseEntity.ok(companyService.updateCompanyByCompanyId(companyId,bussinessName,
               businessDescription,phoneNo,emailId,
               address,BussinessType.valueOf(bussinessType.toUpperCase()),gstin,bussinessCategory,State.valueOf(state.toUpperCase()),logo,signature,bankName,accountNo,
               ifscCode,accountHolderName,upiId));
    }




    @DeleteMapping("/delete/company/{companyId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteCompanyById(@PathVariable Long companyId){
        return ResponseEntity.ok(companyService.deleteCompanyById(companyId));
    }


    @GetMapping("/get/company/{companyId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long companyId){
        return ResponseEntity.ok(companyService.getCompanyById(companyId));
    }

    @GetMapping("/owner/{ownerId}/isCreated/company")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Boolean> isAnyCompanyCreatedByOwner(@PathVariable Long ownerId){
        // if owner created 1 or more than one company the output will be -> true.
        // else output will be -> false.
       return ResponseEntity.ok(companyService.isAnyCompanyCreatedByOwner(ownerId));
    }

}
