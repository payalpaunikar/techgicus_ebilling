package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyRequestDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyTransactionResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PartyController {

      private PartyService partyService;

    @Autowired
    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }


    @PostMapping("/company/{companyId}/created/party")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PartyResponseDto> createdPartyInCompany(@PathVariable Long companyId,
                                                                  @RequestBody PartyRequestDto partyRequestDto){
       return ResponseEntity.ok(partyService.createdPartyInCompany(companyId,partyRequestDto));
    }


    @GetMapping("/company/{companyId}/parties")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<PartyResponseDto>> getCompanyParties(@PathVariable Long companyId){
        return ResponseEntity.ok(partyService.getCompanyParties(companyId));
    }


    @GetMapping("/party/{partyId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PartyResponseDto> getPartyById(@PathVariable Long partyId){
       return ResponseEntity.ok(partyService.getPartyById(partyId));
    }


    @PutMapping("/party/{partyId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PartyResponseDto> updatePartyById(@PathVariable Long partyId,@RequestBody PartyRequestDto partyRequestDto){
        return ResponseEntity.ok(partyService.updatePartyId(partyId,partyRequestDto));
    }


    @DeleteMapping("/party/{partyId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deletePartyById(@PathVariable Long partyId){
        return ResponseEntity.ok(partyService.deletePartyById(partyId));
    }


    @GetMapping("/party/{partyId}/transaction/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<PartyTransactionResponse> getPartyAllTransactions(@PathVariable Long partyId,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size){
        return partyService.getPartyAllTransactions(partyId,page,size);
    }






}
