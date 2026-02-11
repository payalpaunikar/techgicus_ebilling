package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PartyTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleReturnDto.SaleReturnItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleReturnDto.SaleReturnItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleReturnDto.SaleReturnRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleReturnDto.SaleReturnResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.taxDto.ItemTaxSummaryResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.PartyMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.SaleReturnItemMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.SaleReturnMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class SaleReturnService {

          private SaleReturnRepository saleReturnRepository;
          private CompanyRepository companyRepository;
          private PartyRepository partyRepository;
          private SaleReturnItemRepository saleReturnItemRepository;
          private SaleReturnMapper saleReturnMapper;
          private SaleReturnItemMapper saleReturnItemMapper;
          private PartyMapper partyMapper;
          private PartyLedgerService partyLedgerService;
          private PartyActivityService partyActivityService;
          private ItemRepository itemRepository;
    private final TaxCalculationService taxCalculationService;


    public SaleReturnService(TaxCalculationService taxCalculationService, ItemRepository itemRepository, PartyActivityService partyActivityService, PartyLedgerService partyLedgerService, PartyMapper partyMapper, SaleReturnItemMapper saleReturnItemMapper, SaleReturnMapper saleReturnMapper, SaleReturnItemRepository saleReturnItemRepository, PartyRepository partyRepository, CompanyRepository companyRepository, SaleReturnRepository saleReturnRepository) {
        this.taxCalculationService = taxCalculationService;
        this.itemRepository = itemRepository;
        this.partyActivityService = partyActivityService;
        this.partyLedgerService = partyLedgerService;
        this.partyMapper = partyMapper;
        this.saleReturnItemMapper = saleReturnItemMapper;
        this.saleReturnMapper = saleReturnMapper;
        this.saleReturnItemRepository = saleReturnItemRepository;
        this.partyRepository = partyRepository;
        this.companyRepository = companyRepository;
        this.saleReturnRepository = saleReturnRepository;
    }

    @Transactional
    public SaleReturnResponse createdSaleReturn(Long companyId, SaleReturnRequest saleReturnRequest){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Compnay not found with id : "+companyId));

        Party party = partyRepository.findById(saleReturnRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+saleReturnRequest.getPartyId()));


        SaleReturn saleReturn = saleReturnMapper.convertRequestToEntity(saleReturnRequest);
        saleReturn.setParty(party);
        saleReturn.setCompany(company);

        List<SaleReturnItem> saleReturnItems = setSaleReturnItemListFields(saleReturnRequest.getSaleReturnItemRequests(),saleReturn);

//        List<SaleReturnItem> saleReturnItems = saleReturnRequest.getSaleReturnItemRequests()
//                .stream()
//                .map(saleReturnItemRequest -> {
//                    SaleReturnItem saleReturnItem = saleReturnItemMapper.convertRequestToEntity(saleReturnItemRequest);
//                    saleReturnItem.setSaleReturn(saleReturn);
//                    return saleReturnItem;
//                }).toList();

        saleReturn.setSaleReturnItems(saleReturnItems);


        saleReturnRepository.save(saleReturn);


        // add party ledger entry in the database
        partyLedgerService.addLedgerEntry(
                saleReturn.getParty(),
                saleReturn.getCompany(),
                saleReturn.getReturnDate(),
                PartyTransactionType.SALE_RETURN,
                saleReturn.getSaleReturnId(),
                saleReturn.getReturnNo(),
                0.0,
                saleReturn.getTotalAmount(),
                saleReturn.getBalanceAmount(),
                saleReturn.getDescription()
        );

        // add party activity entry in the database
        partyActivityService.addActivity(
                saleReturn.getParty(),
                saleReturn.getCompany(),
                saleReturn.getSaleReturnId(),
                saleReturn.getReturnNo(),
                saleReturn.getReturnDate(),
                PartyTransactionType.SALE_RETURN,
                saleReturn.getTotalAmount(),
                saleReturn.getBalanceAmount(),
                true,
                saleReturn.getDescription());

        List<SaleReturnItemResponse> saleReturnItemResponses = setSaleReturnItemResponseListFields(saleReturn.getSaleReturnItems());

       // List<SaleReturnItemResponse> saleReturnItemResponses = saleReturnItemMapper.convertEntityListToResponseList(saleReturn.getSaleReturnItems());
        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(saleReturn.getParty());

        List<ItemTaxSummaryResponse> taxSummaryResponses =
                taxCalculationService.calculateTaxSummary(saleReturnItemResponses);
        SaleReturnResponse saleReturnResponse = saleReturnMapper.convertEntityToResponse(saleReturn);
       saleReturnResponse.setPartyResponseDto(partyResponseDto);
       saleReturnResponse.setSaleReturnItemResponses(saleReturnItemResponses);
       saleReturnResponse.setTaxSummary(taxSummaryResponses);

       return saleReturnResponse;
    }


    public List<SaleReturnResponse> getSaleReturnListByFilter(Long companyId, LocalDate startDate,LocalDate endDate){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Compnay not found with id : "+companyId));

        List<SaleReturn> saleReturns = saleReturnRepository.findAllByCompanyAndReturnDateBetweenOrderByReturnDateDesc(company,startDate,endDate);

        List<SaleReturnResponse> saleReturnResponses = saleReturns.stream()
                .map(saleReturn -> {
                    PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(saleReturn.getParty());
                    List<SaleReturnItemResponse> saleReturnItemResponses = setSaleReturnItemResponseListFields(saleReturn.getSaleReturnItems());
                    //List<SaleReturnItemResponse> saleReturnItemResponses = saleReturnItemMapper.convertEntityListToResponseList(saleReturn.getSaleReturnItems());
                    List<ItemTaxSummaryResponse> taxSummaryResponses =
                            taxCalculationService.calculateTaxSummary(saleReturnItemResponses);
                    SaleReturnResponse saleReturnResponse = saleReturnMapper.convertEntityToResponse(saleReturn);
                    saleReturnResponse.setPartyResponseDto(partyResponseDto);
                    saleReturnResponse.setSaleReturnItemResponses(saleReturnItemResponses);
                    saleReturnResponse.setTaxSummary(taxSummaryResponses);
                    return saleReturnResponse;
                }).toList();

        return saleReturnResponses;
    }

    public SaleReturnResponse getSaleReturnById(Long saleReturnId){
        SaleReturn saleReturn = saleReturnRepository.findById(saleReturnId)
                .orElseThrow(()-> new ResourceNotFoundException("Sale Return not found with id : "+saleReturnId));

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(saleReturn.getParty());
        List<SaleReturnItemResponse> saleReturnItemResponses = setSaleReturnItemResponseListFields(saleReturn.getSaleReturnItems());

        List<ItemTaxSummaryResponse> taxSummaryResponses =
                taxCalculationService.calculateTaxSummary(saleReturnItemResponses);
        // List<SaleReturnItemResponse> saleReturnItemResponses = saleReturnItemMapper.convertEntityListToResponseList(saleReturn.getSaleReturnItems());
        SaleReturnResponse saleReturnResponse = saleReturnMapper.convertEntityToResponse(saleReturn);
        saleReturnResponse.setPartyResponseDto(partyResponseDto);
        saleReturnResponse.setSaleReturnItemResponses(saleReturnItemResponses);
        saleReturnResponse.setTaxSummary(taxSummaryResponses);

        return saleReturnResponse;
    }


    @Transactional
    public SaleReturnResponse updateSaleReturnById(Long saleReturnId,SaleReturnRequest saleReturnRequest){
        SaleReturn saleReturn = saleReturnRepository.findById(saleReturnId)
                .orElseThrow(()-> new ResourceNotFoundException("Sale Return not found with id : "+saleReturnId));

        Party party = partyRepository.findById(saleReturnRequest.getPartyId())
                        .orElseThrow(()-> new ResourceNotFoundException("Sale Return not found with id : "+saleReturnRequest.getPartyId()));

        saleReturnMapper.updateSaleReturn(saleReturnRequest,saleReturn);
        saleReturn.setParty(party);
        saleReturn.getSaleReturnItems().clear();

        List<SaleReturnItem> saleReturnItems = setSaleReturnItemListFields(saleReturnRequest.getSaleReturnItemRequests(),saleReturn);

//        List<SaleReturnItem> saleReturnItems = saleReturnRequest.getSaleReturnItemRequests()
//                .stream()
//                .map(saleReturnItemRequest -> {
//                    SaleReturnItem saleReturnItem = saleReturnItemMapper.convertRequestToEntity(saleReturnItemRequest);
//                    saleReturnItem.setSaleReturn(saleReturn);
//                    return saleReturnItem;
//                }).toList();

        saleReturn.getSaleReturnItems().addAll(saleReturnItems);

        saleReturnRepository.save(saleReturn);


        // update party ledger entry in the database
        partyLedgerService.updatePartyLedger(
                saleReturn.getParty(),
                saleReturn.getCompany(),
                saleReturn.getReturnDate(),
                PartyTransactionType.SALE_RETURN,
                saleReturn.getSaleReturnId(),
                saleReturn.getReturnNo(),
                0.0,
                saleReturn.getTotalAmount(),
                saleReturn.getBalanceAmount(),
                saleReturn.getDescription()
        );

        // update party activity entry in the database
        partyActivityService.updatePartyActivity(
                saleReturn.getParty(),
                saleReturn.getCompany(),
                saleReturn.getSaleReturnId(),
                saleReturn.getReturnNo(),
                saleReturn.getReturnDate(),
                PartyTransactionType.SALE_RETURN,
                saleReturn.getTotalAmount(),
                saleReturn.getBalanceAmount(),
                true,
                saleReturn.getDescription());

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(saleReturn.getParty());
        List<SaleReturnItemResponse> saleReturnItemResponses = setSaleReturnItemResponseListFields(saleReturn.getSaleReturnItems());

        List<ItemTaxSummaryResponse> taxSummaryResponses =
                taxCalculationService.calculateTaxSummary(saleReturnItemResponses);
        //  List<SaleReturnItemResponse> saleReturnItemResponses = saleReturnItemMapper.convertEntityListToResponseList(saleReturn.getSaleReturnItems());
        SaleReturnResponse saleReturnResponse = saleReturnMapper.convertEntityToResponse(saleReturn);
        saleReturnResponse.setPartyResponseDto(partyResponseDto);
        saleReturnResponse.setSaleReturnItemResponses(saleReturnItemResponses);
        saleReturnResponse.setTaxSummary(taxSummaryResponses);

        return saleReturnResponse;
    }


    @Transactional
    public String deleteSaleReturnById(Long saleReturnId){
        SaleReturn saleReturn = saleReturnRepository.findById(saleReturnId)
                .orElseThrow(()-> new ResourceNotFoundException("Sale Return not found with id : "+saleReturnId));

        partyLedgerService.deletePartyLedger(PartyTransactionType.SALE_RETURN,saleReturn.getSaleReturnId());
        partyActivityService.deletePartyActivity(PartyTransactionType.SALE_RETURN,saleReturn.getSaleReturnId());


        saleReturnRepository.delete(saleReturn);

       return "Sale Return delete successfully.";
    }


    private SaleReturnItem setSaleReturnItemFields(SaleReturnItemRequest request, SaleReturn saleReturn){
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(()-> new ResourceNotFoundException("Item not found with id : "+request.getItemId()));
        SaleReturnItem saleReturnItem = saleReturnItemMapper.convertRequestToEntity(request);
        saleReturnItem.setItem(item);
        saleReturnItem.setSaleReturn(saleReturn);

        return saleReturnItem;
    }


    private List<SaleReturnItem> setSaleReturnItemListFields(List<SaleReturnItemRequest> requestList,SaleReturn saleReturn){
        List<SaleReturnItem> saleReturnItems = requestList.stream()
                .map(request-> setSaleReturnItemFields(request,saleReturn))
                .toList();

        return saleReturnItems;
    }

    private SaleReturnItemResponse setSaleReturnItemResponseFields(SaleReturnItem saleReturnItem){
        Item item = saleReturnItem.getItem();
        SaleReturnItemResponse itemResponse = saleReturnItemMapper.convertEntityToResponse(saleReturnItem);
        itemResponse.setItemId(item.getItemId());
        itemResponse.setName(item.getItemName());

        return itemResponse;
    }

    private List<SaleReturnItemResponse> setSaleReturnItemResponseListFields(List<SaleReturnItem> orderItemList){
        List<SaleReturnItemResponse> responseList = orderItemList.stream()
                .map(orderItem -> setSaleReturnItemResponseFields(orderItem))
                .toList();

        return responseList;
    }
}
