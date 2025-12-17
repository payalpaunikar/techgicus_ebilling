package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PartyActivity;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PartyLedger;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyRequestDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyTransactionResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.PartyMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CompanyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PartyActivityRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PartyLedgerRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PartyRepository;
import jakarta.servlet.http.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PartyService {

        private PartyRepository partyRepository;
        private CompanyRepository companyRepository;
        private PartyMapper partyMapper;
        private PartyLedgerRepository partyLedgerRepository;
        private PartyActivityRepository partyActivityRepository;

    @Autowired
    public PartyService(PartyRepository partyRepository, CompanyRepository companyRepository, PartyMapper partyMapper, PartyLedgerRepository partyLedgerRepository, PartyActivityRepository partyActivityRepository) {
        this.partyRepository = partyRepository;
        this.companyRepository = companyRepository;
        this.partyMapper = partyMapper;
        this.partyLedgerRepository = partyLedgerRepository;
        this.partyActivityRepository = partyActivityRepository;
    }

    public PartyResponseDto createdPartyInCompany(Long companyId, PartyRequestDto partyRequestDto){

        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

    Party party = partyMapper.convertRequestIntoEntity(partyRequestDto);
             party.setCompany(company);
             party.setCreatedAt(LocalDateTime.now());
             party.setUpdatedAt(LocalDateTime.now());

    Party saveParty = partyRepository.save(party);


        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(saveParty);

        return partyResponseDto;
    }



    public List<PartyResponseDto> getCompanyParties(Long companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        List<Party> parties = partyRepository.findByCompany(company);

        List<PartyResponseDto> partyResponseDtos = partyMapper.convertPartiesIntoPartyResponses(parties);

        return partyResponseDtos;
    }


    public PartyResponseDto getPartyById(Long partyId){
        Party party = partyRepository.findById(partyId)
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+partyId));

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(party);

        return partyResponseDto;
    }

    public PartyResponseDto updatePartyId(Long partyId,PartyRequestDto partyRequestDto){
        Party party = partyRepository.findById(partyId)
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+partyId));

        partyMapper.updatePartyFromRequest(partyRequestDto,party);
        party.setUpdatedAt(LocalDateTime.now());

        Party saveParty = partyRepository.save(party);

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(saveParty);

        return partyResponseDto;
    }


    public String deletePartyById(Long partyId){
        Party party = partyRepository.findById(partyId)
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+partyId));

        partyRepository.delete(party);

        return "Party delete succefully";
    }


    public Page<PartyTransactionResponse> getPartyAllTransactions(Long partyId,int page,int size) {

        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new ResourceNotFoundException("Party not found with id: " + partyId));

        List<PartyTransactionResponse> transactions = new ArrayList<>();

        Pageable pageable = PageRequest.of(page,size);

        Page<PartyActivity> activityList = partyActivityRepository.findByPartyPartyIdOrderByActivityDateDesc(partyId,pageable);

        return activityList.map(this::convertPartyActivityIntoPartyTransactionResponse);

    }


    private PartyTransactionResponse convertPartyActivityIntoPartyTransactionResponse(PartyActivity partyActivity){


        PartyTransactionResponse partyTransactionResponse = new PartyTransactionResponse();
        partyTransactionResponse.setId(partyActivity.getId());
        partyTransactionResponse.setTransactionType(partyActivity.getType());
        partyTransactionResponse.setActivityDate(partyActivity.getActivityDate());
        partyTransactionResponse.setReferenceId(partyActivity.getReferenceId());
        partyTransactionResponse.setReferenceNumber(partyActivity.getReferenceNumber());
        partyTransactionResponse.setAmount(partyActivity.getAmount());
        partyTransactionResponse.setRunningBalance(partyActivity.getRunningBalance());

        return partyTransactionResponse;
    }

}
