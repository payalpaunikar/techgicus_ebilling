package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseReturn;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseReturnItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseReturnDto.PurchaseReturnItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseReturnDto.PurchaseReturnRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseReturnDto.PurchaseReturnResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.PartyMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.PurchaseReturnItemMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.PurchaseReturnMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CompanyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PartyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PurchaseReturnItemRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PurchaseReturnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PurchaseReturnService {

     private PurchaseReturnRepository purchaseReturnRepository;
     private PurchaseReturnItemRepository purchaseReturnItemRepository;
     private CompanyRepository companyRepository;
     private PartyRepository partyRepository;
     private PartyMapper partyMapper;
     private PurchaseReturnMapper purchaseReturnMapper;
     private PurchaseReturnItemMapper purchaseReturnItemMapper;

     @Autowired
    public PurchaseReturnService(PurchaseReturnRepository purchaseReturnRepository, PurchaseReturnItemRepository purchaseReturnItemRepository, CompanyRepository companyRepository, PartyRepository partyRepository, PartyMapper partyMapper, PurchaseReturnMapper purchaseReturnMapper, PurchaseReturnItemMapper purchaseReturnItemMapper) {
        this.purchaseReturnRepository = purchaseReturnRepository;
        this.purchaseReturnItemRepository = purchaseReturnItemRepository;
        this.companyRepository = companyRepository;
        this.partyRepository = partyRepository;
        this.partyMapper = partyMapper;
        this.purchaseReturnMapper = purchaseReturnMapper;
        this.purchaseReturnItemMapper = purchaseReturnItemMapper;
    }


    public PurchaseReturnResponse createPurchaseReturn(Long companyId, PurchaseReturnRequest purchaseReturnRequest){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Compnay not found with id : "+companyId));

        Party party = partyRepository.findById(purchaseReturnRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+purchaseReturnRequest.getPartyId()));


        PurchaseReturn purchaseReturn = purchaseReturnMapper.convertRequestToEntity(purchaseReturnRequest);
        purchaseReturn.setParty(party);
        purchaseReturn.setCompany(company);

        List<PurchaseReturnItem> purchaseReturnItems = purchaseReturnRequest.getPurchaseReturnItemRequests()
                .stream()
                .map(purchaseReturnItemRequest -> {
                    PurchaseReturnItem purchaseReturnItem = purchaseReturnItemMapper.convertRequestToEntity(purchaseReturnItemRequest);
                    purchaseReturnItem.setPurchaseReturn(purchaseReturn);
                    return purchaseReturnItem;
                }).toList();


        purchaseReturn.setPurchaseReturnItems(purchaseReturnItems);

        purchaseReturnRepository.save(purchaseReturn);

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(purchaseReturn.getParty());
        List<PurchaseReturnItemResponse> purchaseReturnItemResponses = purchaseReturnItemMapper.convertEntityListToResponseList(purchaseReturn.getPurchaseReturnItems());
        PurchaseReturnResponse purchaseReturnResponse = purchaseReturnMapper.convertEntityToResponse(purchaseReturn);
        purchaseReturnResponse.setPartyResponseDto(partyResponseDto);
        purchaseReturnResponse.setPurchaseReturnItemResponses(purchaseReturnItemResponses);

        return purchaseReturnResponse;
    }


    public List<PurchaseReturnResponse> getPurchaseReturnListByFilter(Long companyId, LocalDate startDate,LocalDate endDate){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Compnay not found with id : "+companyId));

        List<PurchaseReturn> purchaseReturnList = purchaseReturnRepository.findAllByCompanyAndReturnDateBetween(company,startDate,endDate);

        List<PurchaseReturnResponse> purchaseReturnResponseList = purchaseReturnList.stream()
                .map(purchaseReturn -> {
                    PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(purchaseReturn.getParty());
                    List<PurchaseReturnItemResponse> purchaseReturnItemResponses = purchaseReturnItemMapper.convertEntityListToResponseList(purchaseReturn.getPurchaseReturnItems());
                    PurchaseReturnResponse purchaseReturnResponse = purchaseReturnMapper.convertEntityToResponse(purchaseReturn);
                    purchaseReturnResponse.setPartyResponseDto(partyResponseDto);
                    purchaseReturnResponse.setPurchaseReturnItemResponses(purchaseReturnItemResponses);
                    return purchaseReturnResponse;
                }).toList();

        return purchaseReturnResponseList;
    }



    public PurchaseReturnResponse getPurchaseReturnById(Long purchaseReturnId){
         PurchaseReturn purchaseReturn = purchaseReturnRepository.findById(purchaseReturnId)
                 .orElseThrow(()-> new ResourceNotFoundException("Purchase Return not found with id : "+purchaseReturnId));

         PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(purchaseReturn.getParty());
         List<PurchaseReturnItemResponse> purchaseReturnItemResponses = purchaseReturnItemMapper.convertEntityListToResponseList(purchaseReturn.getPurchaseReturnItems());
         PurchaseReturnResponse purchaseReturnResponse = purchaseReturnMapper.convertEntityToResponse(purchaseReturn);
         purchaseReturnResponse.setPartyResponseDto(partyResponseDto);
         purchaseReturnResponse.setPurchaseReturnItemResponses(purchaseReturnItemResponses);

         return purchaseReturnResponse;
    }


    public PurchaseReturnResponse updatePurchaseReturnById(Long purchaseReturnId,PurchaseReturnRequest purchaseReturnRequest){
        PurchaseReturn purchaseReturn = purchaseReturnRepository.findById(purchaseReturnId)
                .orElseThrow(()-> new ResourceNotFoundException("Purchase Return not found with id : "+purchaseReturnId));

        Party party = partyRepository.findById(purchaseReturnRequest.getPartyId())
                        .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+purchaseReturnRequest.getPartyId()));

        purchaseReturnMapper.updatePurchaseReturn(purchaseReturnRequest,purchaseReturn);
        purchaseReturn.setParty(party);
        purchaseReturn.getPurchaseReturnItems().clear();

        List<PurchaseReturnItem> purchaseReturnItems = purchaseReturnRequest.getPurchaseReturnItemRequests()
                .stream()
                .map(purchaseReturnItemRequest -> {
                    PurchaseReturnItem purchaseReturnItem = purchaseReturnItemMapper.convertRequestToEntity(purchaseReturnItemRequest);
                    purchaseReturnItem.setPurchaseReturn(purchaseReturn);
                    return purchaseReturnItem;
                }).toList();


        purchaseReturn.getPurchaseReturnItems().addAll(purchaseReturnItems);

        purchaseReturnRepository.save(purchaseReturn);

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(purchaseReturn.getParty());
        List<PurchaseReturnItemResponse> purchaseReturnItemResponses = purchaseReturnItemMapper.convertEntityListToResponseList(purchaseReturn.getPurchaseReturnItems());

        PurchaseReturnResponse purchaseReturnResponse = purchaseReturnMapper.convertEntityToResponse(purchaseReturn);
        purchaseReturnResponse.setPartyResponseDto(partyResponseDto);
        purchaseReturnResponse.setPurchaseReturnItemResponses(purchaseReturnItemResponses);

        return purchaseReturnResponse;
    }

    public String deletePurchaseReturnById(Long purchaseReturnId){
        PurchaseReturn purchaseReturn = purchaseReturnRepository.findById(purchaseReturnId)
                .orElseThrow(()-> new ResourceNotFoundException("Purchase Return not found with id : "+purchaseReturnId));

       purchaseReturnRepository.delete(purchaseReturn);

       return "Purchase Return delete succefully.";
    }
}
