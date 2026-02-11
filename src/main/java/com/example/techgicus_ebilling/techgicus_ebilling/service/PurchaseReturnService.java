package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PartyTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseReturnDto.PurchaseReturnItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseReturnDto.PurchaseReturnItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseReturnDto.PurchaseReturnRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseReturnDto.PurchaseReturnResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.taxDto.ItemTaxSummaryResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.PartyMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.PurchaseReturnItemMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.PurchaseReturnMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     private PartyLedgerService partyLedgerService;
     private PartyActivityService partyActivityService;
     private ItemRepository itemRepository;
    private final TaxCalculationService taxCalculationService;


    public PurchaseReturnService(TaxCalculationService taxCalculationService, ItemRepository itemRepository, PartyActivityService partyActivityService, PartyLedgerService partyLedgerService, PurchaseReturnItemMapper purchaseReturnItemMapper, PurchaseReturnMapper purchaseReturnMapper, PartyMapper partyMapper, PartyRepository partyRepository, CompanyRepository companyRepository, PurchaseReturnItemRepository purchaseReturnItemRepository, PurchaseReturnRepository purchaseReturnRepository) {
        this.taxCalculationService = taxCalculationService;
        this.itemRepository = itemRepository;
        this.partyActivityService = partyActivityService;
        this.partyLedgerService = partyLedgerService;
        this.purchaseReturnItemMapper = purchaseReturnItemMapper;
        this.purchaseReturnMapper = purchaseReturnMapper;
        this.partyMapper = partyMapper;
        this.partyRepository = partyRepository;
        this.companyRepository = companyRepository;
        this.purchaseReturnItemRepository = purchaseReturnItemRepository;
        this.purchaseReturnRepository = purchaseReturnRepository;
    }

    @Transactional
    public PurchaseReturnResponse createPurchaseReturn(Long companyId, PurchaseReturnRequest purchaseReturnRequest){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Compnay not found with id : "+companyId));

        Party party = partyRepository.findById(purchaseReturnRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+purchaseReturnRequest.getPartyId()));


        PurchaseReturn purchaseReturn = purchaseReturnMapper.convertRequestToEntity(purchaseReturnRequest);
        purchaseReturn.setParty(party);
        purchaseReturn.setCompany(company);

        List<PurchaseReturnItem> purchaseReturnItems = setReturnItemListFields(purchaseReturnRequest.getPurchaseReturnItemRequests(),purchaseReturn);

//        List<PurchaseReturnItem> purchaseReturnItems = purchaseReturnRequest.getPurchaseReturnItemRequests()
//                .stream()
//                .map(purchaseReturnItemRequest -> {
//                    PurchaseReturnItem purchaseReturnItem = purchaseReturnItemMapper.convertRequestToEntity(purchaseReturnItemRequest);
//                    purchaseReturnItem.setPurchaseReturn(purchaseReturn);
//                    return purchaseReturnItem;
//                }).toList();


        purchaseReturn.setPurchaseReturnItems(purchaseReturnItems);

        purchaseReturnRepository.save(purchaseReturn);

        // add party ledger entry in the database
        partyLedgerService.addLedgerEntry(
                purchaseReturn.getParty(),
                purchaseReturn.getCompany(),
                purchaseReturn.getReturnDate(),
                PartyTransactionType.PURCHASE_RETURN,
                purchaseReturn.getPurchaseReturnId(),
                purchaseReturn.getReturnNo(),
                purchaseReturn.getTotalAmount(),
                0.0,
                purchaseReturn.getBalanceAmount(),
                purchaseReturn.getDescription()
        );

        // add party activity entry in the database
        partyActivityService.addActivity(
                purchaseReturn.getParty(),
                purchaseReturn.getCompany(),
                purchaseReturn.getPurchaseReturnId(),
                purchaseReturn.getReturnNo(),
                purchaseReturn.getReturnDate(),
                PartyTransactionType.PURCHASE_RETURN,
                purchaseReturn.getTotalAmount(),
                purchaseReturn.getBalanceAmount(),
                true,
                purchaseReturn.getDescription());

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(purchaseReturn.getParty());
        List<PurchaseReturnItemResponse> purchaseReturnItemResponses = setReturnItemResponseListFields(purchaseReturn.getPurchaseReturnItems());
      //  List<PurchaseReturnItemResponse> purchaseReturnItemResponses = purchaseReturnItemMapper.convertEntityListToResponseList(purchaseReturn.getPurchaseReturnItems());
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
                    List<PurchaseReturnItemResponse> purchaseReturnItemResponses = setReturnItemResponseListFields(purchaseReturn.getPurchaseReturnItems());
                   // List<PurchaseReturnItemResponse> purchaseReturnItemResponses = purchaseReturnItemMapper.convertEntityListToResponseList(purchaseReturn.getPurchaseReturnItems());
                    List<ItemTaxSummaryResponse> taxSummaryResponses = taxCalculationService.calculateTaxSummary(purchaseReturnItemResponses);

                    PurchaseReturnResponse purchaseReturnResponse = purchaseReturnMapper.convertEntityToResponse(purchaseReturn);
                    purchaseReturnResponse.setPartyResponseDto(partyResponseDto);
                    purchaseReturnResponse.setPurchaseReturnItemResponses(purchaseReturnItemResponses);
                    purchaseReturnResponse.setTaxSummary(taxSummaryResponses);
                    return purchaseReturnResponse;
                }).toList();

        return purchaseReturnResponseList;
    }



    public PurchaseReturnResponse getPurchaseReturnById(Long purchaseReturnId){
         PurchaseReturn purchaseReturn = purchaseReturnRepository.findById(purchaseReturnId)
                 .orElseThrow(()-> new ResourceNotFoundException("Purchase Return not found with id : "+purchaseReturnId));

         PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(purchaseReturn.getParty());
         List<PurchaseReturnItemResponse> purchaseReturnItemResponses = setReturnItemResponseListFields(purchaseReturn.getPurchaseReturnItems());
        // List<PurchaseReturnItemResponse> purchaseReturnItemResponses = purchaseReturnItemMapper.convertEntityListToResponseList(purchaseReturn.getPurchaseReturnItems());

        List<ItemTaxSummaryResponse> taxSummaryResponses = taxCalculationService.calculateTaxSummary(purchaseReturnItemResponses);

        PurchaseReturnResponse purchaseReturnResponse = purchaseReturnMapper.convertEntityToResponse(purchaseReturn);
         purchaseReturnResponse.setPartyResponseDto(partyResponseDto);
         purchaseReturnResponse.setPurchaseReturnItemResponses(purchaseReturnItemResponses);
         purchaseReturnResponse.setTaxSummary(taxSummaryResponses);
         return purchaseReturnResponse;
    }


    @Transactional
    public PurchaseReturnResponse updatePurchaseReturnById(Long purchaseReturnId,PurchaseReturnRequest purchaseReturnRequest){
        PurchaseReturn purchaseReturn = purchaseReturnRepository.findById(purchaseReturnId)
                .orElseThrow(()-> new ResourceNotFoundException("Purchase Return not found with id : "+purchaseReturnId));

        Party party = partyRepository.findById(purchaseReturnRequest.getPartyId())
                        .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+purchaseReturnRequest.getPartyId()));

        purchaseReturnMapper.updatePurchaseReturn(purchaseReturnRequest,purchaseReturn);
        purchaseReturn.setParty(party);
        purchaseReturn.getPurchaseReturnItems().clear();

        List<PurchaseReturnItem> purchaseReturnItems = setReturnItemListFields(purchaseReturnRequest.getPurchaseReturnItemRequests(),purchaseReturn);

//        List<PurchaseReturnItem> purchaseReturnItems = purchaseReturnRequest.getPurchaseReturnItemRequests()
//                .stream()
//                .map(purchaseReturnItemRequest -> {
//                    PurchaseReturnItem purchaseReturnItem = purchaseReturnItemMapper.convertRequestToEntity(purchaseReturnItemRequest);
//                    purchaseReturnItem.setPurchaseReturn(purchaseReturn);
//                    return purchaseReturnItem;
//                }).toList();


        purchaseReturn.getPurchaseReturnItems().addAll(purchaseReturnItems);

        purchaseReturnRepository.save(purchaseReturn);

        // add party ledger entry in the database
        partyLedgerService.updatePartyLedger(
                purchaseReturn.getParty(),
                purchaseReturn.getCompany(),
                purchaseReturn.getReturnDate(),
                PartyTransactionType.PURCHASE_RETURN,
                purchaseReturn.getPurchaseReturnId(),
                purchaseReturn.getReturnNo(),
                purchaseReturn.getTotalAmount(),
                0.0,
                purchaseReturn.getBalanceAmount(),
                purchaseReturn.getDescription()
        );

        // add party activity entry in the database
        partyActivityService.updatePartyActivity(
                purchaseReturn.getParty(),
                purchaseReturn.getCompany(),
                purchaseReturn.getPurchaseReturnId(),
                purchaseReturn.getReturnNo(),
                purchaseReturn.getReturnDate(),
                PartyTransactionType.PURCHASE_RETURN,
                purchaseReturn.getTotalAmount(),
                purchaseReturn.getBalanceAmount(),
                true,
                purchaseReturn.getDescription());

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(purchaseReturn.getParty());

        List<PurchaseReturnItemResponse> purchaseReturnItemResponses = setReturnItemResponseListFields(purchaseReturn.getPurchaseReturnItems());
       // List<PurchaseReturnItemResponse> purchaseReturnItemResponses = purchaseReturnItemMapper.convertEntityListToResponseList(purchaseReturn.getPurchaseReturnItems());

        PurchaseReturnResponse purchaseReturnResponse = purchaseReturnMapper.convertEntityToResponse(purchaseReturn);
        purchaseReturnResponse.setPartyResponseDto(partyResponseDto);
        purchaseReturnResponse.setPurchaseReturnItemResponses(purchaseReturnItemResponses);

        return purchaseReturnResponse;
    }


    @Transactional
    public String deletePurchaseReturnById(Long purchaseReturnId){
        PurchaseReturn purchaseReturn = purchaseReturnRepository.findById(purchaseReturnId)
                .orElseThrow(()-> new ResourceNotFoundException("Purchase Return not found with id : "+purchaseReturnId));

        partyLedgerService.deletePartyLedger(PartyTransactionType.PURCHASE_RETURN,purchaseReturn.getPurchaseReturnId());
        partyActivityService.deletePartyActivity(PartyTransactionType.PURCHASE_RETURN,purchaseReturn.getPurchaseReturnId());

       purchaseReturnRepository.delete(purchaseReturn);

       return "Purchase Return delete succefully.";
    }


     private PurchaseReturnItem setReturnItemFields(PurchaseReturnItemRequest request,PurchaseReturn purchaseReturn){
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(()-> new ResourceNotFoundException("Item not found with id "+request.getItemId()));

        PurchaseReturnItem purchaseReturnItem = purchaseReturnItemMapper.convertRequestToEntity(request);
        purchaseReturnItem.setItem(item);
        purchaseReturnItem.setPurchaseReturn(purchaseReturn);

        return purchaseReturnItem;
     }

    private List<PurchaseReturnItem> setReturnItemListFields(List<PurchaseReturnItemRequest> requestList,PurchaseReturn purchaseReturn){
       List<PurchaseReturnItem> returnItemList = requestList.stream()
               .map(request-> setReturnItemFields(request,purchaseReturn))
               .toList();

       return returnItemList;
    }


    private PurchaseReturnItemResponse setReturnItemResponseFields(PurchaseReturnItem returnItem){

        Item item = returnItem.getItem();
        PurchaseReturnItemResponse response = purchaseReturnItemMapper.convertEntityToResponse(returnItem);
        response.setName(item.getItemName());
        response.setItemId(item.getItemId());

        return response;
    }

    private List<PurchaseReturnItemResponse> setReturnItemResponseListFields(List<PurchaseReturnItem> returnItemList){
        List<PurchaseReturnItemResponse> responseList = returnItemList.stream()
                .map(returnItem-> setReturnItemResponseFields(returnItem))
                .toList();

        return responseList;
    }

}
