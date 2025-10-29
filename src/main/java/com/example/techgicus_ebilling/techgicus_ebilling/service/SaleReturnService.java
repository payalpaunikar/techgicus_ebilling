package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleReturn;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleReturnItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleReturnDto.SaleReturnItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleReturnDto.SaleReturnRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleReturnDto.SaleReturnResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.PartyMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.SaleReturnItemMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.SaleReturnMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CompanyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PartyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.SaleReturnItemRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.SaleReturnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleReturnService {

          private SaleReturnRepository saleReturnRepository;
          private CompanyRepository companyRepository;
          private PartyRepository partyRepository;
          private SaleReturnItemRepository saleReturnItemRepository;
          private SaleReturnMapper saleReturnMapper;
          private SaleReturnItemMapper saleReturnItemMapper;
          private PartyMapper partyMapper;

    @Autowired
    public SaleReturnService(SaleReturnRepository saleReturnRepository, CompanyRepository companyRepository, PartyRepository partyRepository, SaleReturnItemRepository saleReturnItemRepository, SaleReturnMapper saleReturnMapper, SaleReturnItemMapper saleReturnItemMapper, PartyMapper partyMapper) {
        this.saleReturnRepository = saleReturnRepository;
        this.companyRepository = companyRepository;
        this.partyRepository = partyRepository;
        this.saleReturnItemRepository = saleReturnItemRepository;
        this.saleReturnMapper = saleReturnMapper;
        this.saleReturnItemMapper = saleReturnItemMapper;
        this.partyMapper = partyMapper;
    }


    public SaleReturnResponse createdSaleReturn(Long companyId, SaleReturnRequest saleReturnRequest){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Compnay not found with id : "+companyId));

        Party party = partyRepository.findById(saleReturnRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+saleReturnRequest.getPartyId()));


        SaleReturn saleReturn = saleReturnMapper.convertRequestToEntity(saleReturnRequest);
        saleReturn.setParty(party);
        saleReturn.setCompany(company);

        List<SaleReturnItem> saleReturnItems = saleReturnRequest.getSaleReturnItemRequests()
                .stream()
                .map(saleReturnItemRequest -> {
                    SaleReturnItem saleReturnItem = saleReturnItemMapper.convertRequestToEntity(saleReturnItemRequest);
                    saleReturnItem.setSaleReturn(saleReturn);
                    return saleReturnItem;
                }).toList();

        saleReturn.setSaleReturnItems(saleReturnItems);


        saleReturnRepository.save(saleReturn);

        List<SaleReturnItemResponse> saleReturnItemResponses = saleReturnItemMapper.convertEntityListToResponseList(saleReturn.getSaleReturnItems());
        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(saleReturn.getParty());
        SaleReturnResponse saleReturnResponse = saleReturnMapper.convertEntityToResponse(saleReturn);
       saleReturnResponse.setPartyResponseDto(partyResponseDto);
       saleReturnResponse.setSaleReturnItemResponses(saleReturnItemResponses);

       return saleReturnResponse;
    }


    public List<SaleReturnResponse> getSaleReturnListByFilter(Long companyId, LocalDate startDate,LocalDate endDate){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Compnay not found with id : "+companyId));

        List<SaleReturn> saleReturns = saleReturnRepository.findAllByCompanyAndInvoiceDateBetween(company,startDate,endDate);

        List<SaleReturnResponse> saleReturnResponses = saleReturns.stream()
                .map(saleReturn -> {
                    PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(saleReturn.getParty());
                    List<SaleReturnItemResponse> saleReturnItemResponses = saleReturnItemMapper.convertEntityListToResponseList(saleReturn.getSaleReturnItems());
                    SaleReturnResponse saleReturnResponse = saleReturnMapper.convertEntityToResponse(saleReturn);
                    saleReturnResponse.setPartyResponseDto(partyResponseDto);
                    saleReturnResponse.setSaleReturnItemResponses(saleReturnItemResponses);
                    return saleReturnResponse;
                }).toList();

        return saleReturnResponses;
    }

    public SaleReturnResponse getSaleReturnById(Long saleReturnId){
        SaleReturn saleReturn = saleReturnRepository.findById(saleReturnId)
                .orElseThrow(()-> new ResourceNotFoundException("Sale Return not found with id : "+saleReturnId));

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(saleReturn.getParty());
        List<SaleReturnItemResponse> saleReturnItemResponses = saleReturnItemMapper.convertEntityListToResponseList(saleReturn.getSaleReturnItems());
        SaleReturnResponse saleReturnResponse = saleReturnMapper.convertEntityToResponse(saleReturn);
        saleReturnResponse.setPartyResponseDto(partyResponseDto);
        saleReturnResponse.setSaleReturnItemResponses(saleReturnItemResponses);

        return saleReturnResponse;
    }


    public SaleReturnResponse updateSaleReturnById(Long saleReturnId,SaleReturnRequest saleReturnRequest){
        SaleReturn saleReturn = saleReturnRepository.findById(saleReturnId)
                .orElseThrow(()-> new ResourceNotFoundException("Sale Return not found with id : "+saleReturnId));

        Party party = partyRepository.findById(saleReturnRequest.getPartyId())
                        .orElseThrow(()-> new ResourceNotFoundException("Sale Return not found with id : "+saleReturnRequest.getPartyId()));

        saleReturnMapper.updateSaleReturn(saleReturnRequest,saleReturn);
        saleReturn.setParty(party);
        saleReturn.getSaleReturnItems().clear();

        List<SaleReturnItem> saleReturnItems = saleReturnRequest.getSaleReturnItemRequests()
                .stream()
                .map(saleReturnItemRequest -> {
                    SaleReturnItem saleReturnItem = saleReturnItemMapper.convertRequestToEntity(saleReturnItemRequest);
                    saleReturnItem.setSaleReturn(saleReturn);
                    return saleReturnItem;
                }).toList();

        saleReturn.getSaleReturnItems().addAll(saleReturnItems);

        saleReturnRepository.save(saleReturn);

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(saleReturn.getParty());
        List<SaleReturnItemResponse> saleReturnItemResponses = saleReturnItemMapper.convertEntityListToResponseList(saleReturn.getSaleReturnItems());
        SaleReturnResponse saleReturnResponse = saleReturnMapper.convertEntityToResponse(saleReturn);
        saleReturnResponse.setPartyResponseDto(partyResponseDto);
        saleReturnResponse.setSaleReturnItemResponses(saleReturnItemResponses);

        return saleReturnResponse;
    }


    public String deleteSaleReturnById(Long saleReturnId){
        SaleReturn saleReturn = saleReturnRepository.findById(saleReturnId)
                .orElseThrow(()-> new ResourceNotFoundException("Sale Return not found with id : "+saleReturnId));

       saleReturnRepository.delete(saleReturn);

       return "Sale Return delete successfully.";
    }
}
