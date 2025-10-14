package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto.*;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.InvalidPaymentAmountException;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.PartyMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.PurchaseItemMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.PurchaseMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.PurchasePaymentMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchaseService {

        private CompanyRepository companyRepository;
        private PartyRepository partyRepository;
        private PurchaseRepository purchaseRepository;
        private PurchaseItemRepository purchaseItemRepository;
        private PurchaseMapper purchaseMapper;
        private PurchaseItemMapper purchaseItemMapper;
        private PartyMapper partyMapper;
        private PurchasePaymentMapper purchasePaymentMapper;
        private PurchasePaymentRepository purchasePaymentRepository;

    @Autowired
    public PurchaseService(CompanyRepository companyRepository, PartyRepository partyRepository, PurchaseRepository purchaseRepository, PurchaseItemRepository purchaseItemRepository, PurchaseMapper purchaseMapper, PurchaseItemMapper purchaseItemMapper, PartyMapper partyMapper, PurchasePaymentMapper purchasePaymentMapper, PurchasePaymentRepository purchasePaymentRepository) {
        this.companyRepository = companyRepository;
        this.partyRepository = partyRepository;
        this.purchaseRepository = purchaseRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.purchaseMapper = purchaseMapper;
        this.purchaseItemMapper = purchaseItemMapper;
        this.partyMapper = partyMapper;
        this.purchasePaymentMapper = purchasePaymentMapper;
        this.purchasePaymentRepository = purchasePaymentRepository;
    }

    @Transactional
    public PurchaseResponse createdPurchase(Long companyId, PurchaseRequest purchaseRequest){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Compnay not found with id : "+companyId));

        Party party = partyRepository.findById(purchaseRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+purchaseRequest.getPartyId()));

        Purchase purchase = purchaseMapper.convertPurchaseRequestIntoPurchase(purchaseRequest);
        purchase.setParty(party);
        purchase.setCompany(company);
        purchase.setCreatedAt(LocalDateTime.now());
        purchase.setUpdateAt(LocalDateTime.now());
        purchase.setIsPaid(purchaseRequest.getIsPaid());
        purchase.setOverdue(purchaseRequest.getIsOverdue());


        List<PurchaseItem> purchaseItems = purchaseItemMapper.convertPurchaseItemRequestListIntoPurchaseItemList(purchaseRequest.getPurchaseItemRequests());
        for(PurchaseItem purchaseItem : purchaseItems){
            purchaseItem.setPurchase(purchase);
            purchaseItem.setCreatedAt(LocalDateTime.now());
            purchaseItem.setUpdateAt(LocalDateTime.now());
        }

        purchase.setPurchaseItems(purchaseItems);


        PurchasePayment purchasePayment = new PurchasePayment();
        purchasePayment.setPaymentDate(purchaseRequest.getBillDate());
        purchasePayment.setPaymentDescription("Paid During Purchase");
        purchasePayment.setAmountPaid(purchaseRequest.getSendAmount());
        purchasePayment.setPaymentType(purchaseRequest.getPaymentType());
        purchasePayment.setPurchase(purchase);
        purchasePayment.setCreatedAt(LocalDateTime.now());
        purchasePayment.setUpdateAt(LocalDateTime.now());

        purchasePaymentRepository.save(purchasePayment);

       Purchase savePurchase =  purchaseRepository.save(purchase);

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(party);
        List<PurchaseItemResponse> purchaseItemResponses = purchaseItemMapper.convertPurchaseItemsIntoResponseList(savePurchase.getPurchaseItems());

        PurchasePaymentResponse purchasePaymentResponse = purchasePaymentMapper.convertPurchasePaymentIntoPurchasePaymentResponse(purchasePayment);

        PurchaseResponse purchaseResponse = purchaseMapper.convertPurchaseIntoPurchaseResponse(savePurchase);
        purchaseResponse.setPartyResponseDto(partyResponseDto);
        purchaseResponse.setPurchaseItemResponses(purchaseItemResponses);
        purchaseResponse.setIsPaid(savePurchase.getIsPaid());
        purchaseResponse.setOverdue(savePurchase.getIsOverdue());
        purchaseResponse.setPurchasePaymentResponses(List.of(purchasePaymentResponse));
        purchaseResponse.setPurchaseId(savePurchase.getPurchaseId());

        return purchaseResponse;

    }


    public List<PurchaseResponse> getCompanyPurchases(Long companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Compnay not found with id : "+companyId));

        List<Purchase> purchases = purchaseRepository.findAllByCompanyOrderByBillDateDesc(company);

        List<PurchaseResponse> purchaseResponses = purchases.stream()
                .map(purchase -> {

                    PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(purchase.getParty());
                    List<PurchaseItemResponse> purchaseItemResponses = purchaseItemMapper.convertPurchaseItemsIntoResponseList(purchase.getPurchaseItems());
                    List<PurchasePaymentResponse> purchasePaymentResponses = purchasePaymentMapper.convertPurchasePaymentListIntoPurchasePaymentResponseList(purchase.getPurchasePayments());

                    PurchaseResponse purchaseResponse = purchaseMapper.convertPurchaseIntoPurchaseResponse(purchase);
                    purchaseResponse.setPartyResponseDto(partyResponseDto);
                    purchaseResponse.setPurchaseItemResponses(purchaseItemResponses);
                    purchaseResponse.setIsPaid(purchase.getIsPaid());
                    purchaseResponse.setOverdue(purchase.getIsOverdue());
                    purchaseResponse.setPurchasePaymentResponses(purchasePaymentResponses);
                    purchaseResponse.setPurchaseId(purchase.getPurchaseId());

                    return purchaseResponse;
                }).toList();
        return purchaseResponses;
    }



    public PurchaseResponse getPurchaseById(Long purchaseId){
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(()-> new ResourceNotFoundException("Purchase not found with id : "+purchaseId));

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(purchase.getParty());

        List<PurchaseItemResponse> purchaseItemResponses = purchaseItemMapper.convertPurchaseItemsIntoResponseList(purchase.getPurchaseItems());
        List<PurchasePaymentResponse> purchasePaymentResponses = purchasePaymentMapper.convertPurchasePaymentListIntoPurchasePaymentResponseList(purchase.getPurchasePayments());

        PurchaseResponse purchaseResponse = purchaseMapper.convertPurchaseIntoPurchaseResponse(purchase);
        purchaseResponse.setPartyResponseDto(partyResponseDto);
        purchaseResponse.setPurchaseItemResponses(purchaseItemResponses);
        purchaseResponse.setIsPaid(purchase.getIsPaid());
        purchaseResponse.setOverdue(purchase.getIsOverdue());
        purchaseResponse.setPurchasePaymentResponses(purchasePaymentResponses);
        purchaseResponse.setPurchaseId(purchase.getPurchaseId());

        return purchaseResponse;
    }


    @Transactional
    public PurchaseResponse updatePurchaseById(Long purchaseId,PurchaseRequest purchaseRequest){
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(()-> new ResourceNotFoundException("Purchase not found with id : "+purchaseId));

        Party party = partyRepository.findById(purchaseRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+purchaseRequest.getPartyId()));


        purchaseMapper.updatePurchaseByDto(purchaseRequest,purchase);
        purchase.setParty(party);
        purchase.setUpdateAt(LocalDateTime.now());
        purchase.setIsPaid(purchaseRequest.getIsPaid());
        purchase.setOverdue(purchaseRequest.getIsOverdue());

        purchase.getPurchaseItems().clear();

        List<PurchaseItem> purchaseItems = purchaseItemMapper.convertPurchaseItemRequestListIntoPurchaseItemList(purchaseRequest.getPurchaseItemRequests());

        for (PurchaseItem purchaseItem : purchaseItems){
            purchaseItem.setPurchase(purchase);
            purchaseItem.setCreatedAt(LocalDateTime.now());
            purchaseItem.setUpdateAt(LocalDateTime.now());
        }

        purchase.getPurchaseItems().addAll(purchaseItems);

      Purchase savePurchase =  purchaseRepository.save(purchase);

      PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(party);
      List<PurchaseItemResponse> purchaseItemResponses = purchaseItemMapper.convertPurchaseItemsIntoResponseList(savePurchase.getPurchaseItems());

      PurchaseResponse purchaseResponse = purchaseMapper.convertPurchaseIntoPurchaseResponse(savePurchase);
      purchaseResponse.setPartyResponseDto(partyResponseDto);
      purchaseResponse.setPurchaseItemResponses(purchaseItemResponses);
        purchaseResponse.setIsPaid(savePurchase.getIsPaid());
        purchaseResponse.setOverdue(savePurchase.getIsOverdue());
        purchaseResponse.setPurchaseId(savePurchase.getPurchaseId());

      return purchaseResponse;
    }


    public String deletePurchaseById(Long purchaseId){
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(()-> new ResourceNotFoundException("Purchase not found with id : "+purchaseId));

        purchaseRepository.delete(purchase);

        return "Purchase Delete successfully.";
    }


    public PurchasePaymentResponse makePurchasePayment(Long purchaseId, PurchasePaymentRequest purchasePaymentRequest){

        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(()-> new ResourceNotFoundException("Purchase not found with id : "+purchaseId));

        if(purchase.getBalance()< purchasePaymentRequest.getAmountPaid()){
            throw new InvalidPaymentAmountException("Payment amount (" + purchasePaymentRequest.getAmountPaid() +
                    ") exceeds remaining balance (" + purchase.getBalance() + ").");
        }

        PurchasePayment purchasePayment = purchasePaymentMapper.convertPurchasePaymentRequestIntoPurchasePayment(purchasePaymentRequest);
        purchasePayment.setCreatedAt(LocalDateTime.now());
        purchasePayment.setUpdateAt(LocalDateTime.now());
        purchasePayment.setPurchase(purchase);

        double newSendAmount = purchase.getSendAmount()+purchasePaymentRequest.getAmountPaid();
        purchase.setSendAmount(newSendAmount);
        purchase.setBalance(purchase.getTotalAmount()-purchase.getSendAmount());

        PurchasePayment savePurchasePayment = purchasePaymentRepository.save(purchasePayment);

        purchaseRepository.save(purchase);

        PurchasePaymentResponse purchasePaymentResponse = purchasePaymentMapper.convertPurchasePaymentIntoPurchasePaymentResponse(savePurchasePayment);

        return purchasePaymentResponse;
    }


    public List<PurchaseResponse> getPurchaseReportByFilter(Long companyId, Long partyId,
                                                            LocalDate startDate,LocalDate endDate){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        if (partyId !=null){
            Party party = partyRepository.findById(partyId)
                    .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+partyId));
        }

        List<Purchase> purchases = purchaseRepository.findAllByFilter(companyId,partyId,startDate,endDate);

        List<PurchaseResponse> purchaseResponses = purchases.stream()
                .map(purchase -> {

                    PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(purchase.getParty());
                    List<PurchaseItemResponse> purchaseItemResponses = purchaseItemMapper.convertPurchaseItemsIntoResponseList(purchase.getPurchaseItems());
                    List<PurchasePaymentResponse> purchasePaymentResponses = purchasePaymentMapper.convertPurchasePaymentListIntoPurchasePaymentResponseList(purchase.getPurchasePayments());

                    PurchaseResponse purchaseResponse = purchaseMapper.convertPurchaseIntoPurchaseResponse(purchase);
                    purchaseResponse.setPartyResponseDto(partyResponseDto);
                    purchaseResponse.setPurchaseItemResponses(purchaseItemResponses);
                    purchaseResponse.setIsPaid(purchase.getIsPaid());
                    purchaseResponse.setOverdue(purchase.getIsOverdue());
                    purchaseResponse.setPurchasePaymentResponses(purchasePaymentResponses);
                    purchaseResponse.setPurchaseId(purchase.getPurchaseId());

                    return purchaseResponse;
                }).toList();
        return purchaseResponses;
    }
}
