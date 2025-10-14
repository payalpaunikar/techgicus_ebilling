package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.QuotationType;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.quotationDto.QuotationItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.quotationDto.QuotationRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.quotationDto.QuotationResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SalePaymentResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.QuotationAlreadyClosedException;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.*;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuotationService {

     private QuotationRepository quotationRepository;
     private QuotationItemRepository quotationItemRepository;
     private QuotationMapper quotationMapper;
     private QuotationItemMapper quotationItemMapper;
     private CompanyRepository companyRepository;
     private PartyRepository partyRepository;
     private PartyMapper partyMapper;
     private SaleMapper saleMapper;
     private SaleRepository saleRepository;
     private SaleItemMapper saleItemMapper;

    @Autowired
    public QuotationService(QuotationRepository quotationRepository, QuotationItemRepository quotationItemRepository, QuotationMapper quotationMapper, QuotationItemMapper quotationItemMapper, CompanyRepository companyRepository, PartyRepository partyRepository, PartyMapper partyMapper, SaleMapper saleMapper, SaleRepository saleRepository, SaleItemMapper saleItemMapper) {
        this.quotationRepository = quotationRepository;
        this.quotationItemRepository = quotationItemRepository;
        this.quotationMapper = quotationMapper;
        this.quotationItemMapper = quotationItemMapper;
        this.companyRepository = companyRepository;
        this.partyRepository = partyRepository;
        this.partyMapper = partyMapper;
        this.saleMapper = saleMapper;
        this.saleRepository = saleRepository;
        this.saleItemMapper = saleItemMapper;
    }

    @Transactional
    public QuotationResponse createdQuotation(Long companyId, QuotationRequest quotationRequest){

        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        Party party = partyRepository.findById(quotationRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+quotationRequest.getPartyId()));

        Quotation quotation = quotationMapper.convertQuotationRequestIntoQuotation(quotationRequest);
        quotation.setCompany(company);
        quotation.setParty(party);
        quotation.setCreatedAt(LocalDateTime.now());
        quotation.setUpdateAt(LocalDateTime.now());
        quotation.setQuotationType(QuotationType.OPEN);


        List<QuotationItem> quotationItems = quotationRequest.getQuotationItemRequests()
                .stream()
                .map(quotationItemRequest -> {
                    QuotationItem quotationItem = quotationItemMapper.convertQuotationItemRequestIntoQuotationItem(quotationItemRequest);
                    quotationItem.setQuotation(quotation);
                    return quotationItem;
                }).toList();

        quotation.setQuotationItems(quotationItems);

        Quotation saveQuotation = quotationRepository.save(quotation);

        List<QuotationItem> saveQuotationItemList =  quotationItemRepository.saveAll(quotationItems);

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(party);
        List<QuotationItemResponse> quotationItemResponses = quotationItemMapper.convertQuotationItemListIntoQuotationItemResponseList(saveQuotationItemList);

        QuotationResponse quotationResponse = quotationMapper.convertQuotationIntoQuotationResponse(saveQuotation);
        quotationResponse.setPartyResponseDto(partyResponseDto);
        quotationResponse.setQuotationItemResponses(quotationItemResponses);


        return quotationResponse;
    }


    public QuotationResponse getQuotationById(Long quotationId){
        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(()-> new ResourceNotFoundException("Quotation not found with id : "+quotationId));

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(quotation.getParty());
        List<QuotationItemResponse> quotationItemResponses = quotationItemMapper.convertQuotationItemListIntoQuotationItemResponseList(quotation.getQuotationItems());

        QuotationResponse quotationResponse = quotationMapper.convertQuotationIntoQuotationResponse(quotation);
        quotationResponse.setQuotationItemResponses(quotationItemResponses);
        quotationResponse.setPartyResponseDto(partyResponseDto);
        return quotationResponse;
    }


    public List<QuotationResponse> getAllQuotation(Long companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        List<Quotation> quotations = quotationRepository.findAllByCompany(company);

        List<QuotationResponse> quotationResponses = quotations.stream()
                .map(quotation -> {
                    PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(quotation.getParty());
                    List<QuotationItemResponse> quotationItemResponses = quotationItemMapper.convertQuotationItemListIntoQuotationItemResponseList(quotation.getQuotationItems());
                    QuotationResponse quotationResponse = quotationMapper.convertQuotationIntoQuotationResponse(quotation);
                    quotationResponse.setPartyResponseDto(partyResponseDto);
                    quotationResponse.setQuotationItemResponses(quotationItemResponses);

                    return quotationResponse;
                }).toList();

        return quotationResponses;
    }


    public QuotationResponse updateQuotationById(Long quotationId,QuotationRequest quotationRequest){
        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(()-> new ResourceNotFoundException("Quotation not found id : "+quotationId));

        Party party = partyRepository.findById(quotationRequest.getPartyId())
                        .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+quotationRequest.getPartyId()));

        quotationMapper.updateQuotationByDto(quotationRequest,quotation);

        quotation.getQuotationItems().clear();

         List<QuotationItem> quotationItems = quotationRequest.getQuotationItemRequests()
                         .stream()
                                 .map(quotationItemRequest->{
                                     QuotationItem quotationItem = quotationItemMapper.convertQuotationItemRequestIntoQuotationItem(quotationItemRequest);
                                     quotationItem.setQuotation(quotation);

                                     return quotationItem;
                                 }).toList();

         quotation.getQuotationItems().addAll(quotationItems);
         quotation.setParty(party);
         quotation.setUpdateAt(LocalDateTime.now());

         quotationRepository.save(quotation);

         PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(quotation.getParty());
         List<QuotationItemResponse> quotationItemResponses = quotationItemMapper.convertQuotationItemListIntoQuotationItemResponseList(quotation.getQuotationItems());
         QuotationResponse quotationResponse = quotationMapper.convertQuotationIntoQuotationResponse(quotation);
         quotationResponse.setPartyResponseDto(partyResponseDto);
         quotationResponse.setQuotationItemResponses(quotationItemResponses);

         return quotationResponse;
    }


    public String deleteQuotationById(Long quotationId){
        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(()-> new ResourceNotFoundException("Quotation not found by id : "+quotationId));

        quotationRepository.delete(quotation);

        return "Quotation delete successfully.";
    }


    @Transactional
    public SaleResponse convertQuotationToSale(Long quotationId, SaleRequest saleRequest){
        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new ResourceNotFoundException("Quotation not found with id " + quotationId));

        if (quotation.getQuotationType().equals(QuotationType.CLOSE)){
            throw new QuotationAlreadyClosedException("This quotation is already converted to a sale invoice.");
        }

        quotation.setQuotationType(QuotationType.CLOSE);

        quotationRepository.save(quotation);

        Party party = partyRepository.findById(saleRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+saleRequest.getPartyId()));

       Sale sale = saleMapper.convertSaleRequestIntoSale(saleRequest);
        sale.setCompany(quotation.getCompany());
        sale.setParty(party);
        sale.setInvoceDate(saleRequest.getInvoceDate());
        sale.setDueDate(saleRequest.getDueDate());
        sale.setCreatedAt(LocalDateTime.now());
        sale.setUpdatedAt(LocalDateTime.now());

        // 4️⃣ Handle null-safe numeric fields
        double receivedAmount = sale.getReceivedAmount() != null ? sale.getReceivedAmount() : 0.0;
        double totalAmount = sale.getTotalAmount() != null ? sale.getTotalAmount() : 0.0;

        // calculate balance
        Double balance = totalAmount - receivedAmount;
        sale.setBalance(balance);

        // set isPaid
        sale.setPaid(balance <= 0);

        // set isOverdue
        if (sale.getDueDate() != null && sale.getDueDate().isBefore(LocalDate.now()) && balance > 0) {
            sale.setOverdue(true);
        } else {
            sale.setOverdue(false);
        }

        List<SaleItem> saleItems = saleRequest.getSaleItems()
                .stream()
                .map(saleItemRequest -> {
                    SaleItem saleItem = saleItemMapper.convertSaleItemRequestIntoSaleItem(saleItemRequest);
                    saleItem.setSale(sale);
                    saleItem.setCreatedAt(LocalDateTime.now());
                    saleItem.setUpdateAt(LocalDateTime.now());

                    return saleItem;
                }).toList();

        sale.setSaleItem(saleItems);

        SalePayment salePayment = new SalePayment();
        salePayment.setPaymentDescription("Received During Sale");
        salePayment.setPaymentType(saleRequest.getPaymentType());
        salePayment.setPaymentDate(saleRequest.getInvoceDate());
        salePayment.setSale(sale);
        salePayment.setCreatedAt(LocalDateTime.now());
        salePayment.setUpdateAt(LocalDateTime.now());
        salePayment.setAmountPaid(sale.getReceivedAmount());

        sale.setSalePayments(List.of(salePayment));


        Sale saveSale = saleRepository.save(sale);

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(sale.getParty());
        List<SaleItemResponse> saleItemResponses = saleItemMapper.convertSaleItemListIntoSaleItmResponseList(sale.getSaleItem());

        List<SalePayment> salePayments = saveSale.getSalePayments();

        List<SalePaymentResponse> salePaymentResponses = salePayments.stream()
                .map(salePayment1 -> {
                    SalePaymentResponse salePaymentResponse = new SalePaymentResponse();
                    salePaymentResponse.setPaymentId(salePayment1.getPaymentId());
                    salePaymentResponse.setPaymentDate(salePayment1.getPaymentDate());
                    salePaymentResponse.setPaymentType(salePayment1.getPaymentType());
                    salePaymentResponse.setPaymentDescription(salePayment1.getPaymentDescription());
                    salePaymentResponse.setAmountPaid(salePayment1.getAmountPaid());
                    salePaymentResponse.setReceiptNo(salePayment1.getReceiptNo());
                    salePaymentResponse.setReferenceNumber(salePayment1.getReferenceNumber());

                    return salePaymentResponse;
                }).toList();

        SaleResponse saleResponse = saleMapper.convertSaleIntoSaleResponse(saveSale);
        saleResponse.setPartyResponseDto(partyResponseDto);
        saleResponse.setSaleItemResponses(saleItemResponses);
        saleResponse.setInvoceDate(saveSale.getInvoceDate());
        saleResponse.setDueDate(saveSale.getDueDate());
        saleResponse.setSalePaymentResponses(salePaymentResponses);

        return saleResponse;
    }
}
