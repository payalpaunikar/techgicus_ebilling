package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.DocumentType;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CompanyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.service.DocumentNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocumentNumberController {

    private final DocumentNumberService documentNumberService;
    private final CompanyRepository companyRepository;

    public DocumentNumberController(DocumentNumberService documentNumberService, CompanyRepository companyRepository) {
        this.documentNumberService = documentNumberService;
        this.companyRepository = companyRepository;
    }

    @GetMapping("/company/{companyId}/current/document-number")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> getNextNumber(@PathVariable Long companyId,
                                                @RequestParam DocumentType documentType){

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Company not found"));

        return ResponseEntity.ok(documentNumberService.generate(documentType,company));
    }
}
