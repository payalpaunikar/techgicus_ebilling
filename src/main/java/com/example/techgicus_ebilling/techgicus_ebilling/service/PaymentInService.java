package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PaymentIn;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PartyTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.paymentInDto.PaymentInRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.paymentInDto.PaymentInResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.PartyMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.PaymentInMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CompanyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PartyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PaymentInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentInService {

         private CompanyRepository companyRepository;
         private PartyRepository partyRepository;
         private PaymentInRepository paymentInRepository;
         private PartyMapper partyMapper;
         private PaymentInMapper paymentInMapper;
         private PartyLedgerService partyLedgerService;
         private PartyActivityService partyActivityService;

    @Autowired
    public PaymentInService(CompanyRepository companyRepository, PartyRepository partyRepository, PaymentInRepository paymentInRepository, PartyMapper partyMapper, PaymentInMapper paymentInMapper, PartyLedgerService partyLedgerService, PartyActivityService partyActivityService) {
        this.companyRepository = companyRepository;
        this.partyRepository = partyRepository;
        this.paymentInRepository = paymentInRepository;
        this.partyMapper = partyMapper;
        this.paymentInMapper = paymentInMapper;
        this.partyLedgerService = partyLedgerService;
        this.partyActivityService = partyActivityService;
    }


    @Transactional
    public PaymentInResponse addPaymentIn(Long companyId, PaymentInRequest paymentInRequest){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Compnay not found with id : "+companyId));

        Party party = partyRepository.findById(paymentInRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+paymentInRequest.getPartyId()));

        PaymentIn paymentIn = paymentInMapper.convertRequestIntoEntity(paymentInRequest);
        paymentIn.setCompany(company);
        paymentIn.setParty(party);
        paymentIn.setCreateAt(LocalDateTime.now());
        paymentIn.setUpdateAt(LocalDateTime.now());

        partyLedgerService.addLedgerEntry(
                paymentIn.getParty(),
                paymentIn.getCompany(),
                paymentIn.getPaymentDate(),
                PartyTransactionType.PAYMENT_IN,
                paymentIn.getPaymentInId(),
                paymentIn.getReceiptNo(),
                paymentIn.getReceivedAmount(),
                0.0,
                0.0,
                paymentIn.getDescription()

        );


        partyActivityService.addActivity(
                paymentIn.getParty(),
                paymentIn.getCompany(),
                paymentIn.getPaymentInId(),
                paymentIn.getReceiptNo(),
                paymentIn.getPaymentDate(),
                PartyTransactionType.PAYMENT_IN,
                paymentIn.getReceivedAmount(),
                0.0,
                true,
                paymentIn.getDescription()
        );

        paymentInRepository.save(paymentIn);

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(party);

        PaymentInResponse paymentInResponse = paymentInMapper.convertEntityToResponse(paymentIn);
        paymentIn.setParty(party);

        return paymentInResponse;
    }


    public List<PaymentInResponse> getAllPaymentInByFilter(Long companyId, Long partyId, LocalDate startDate,LocalDate endDate){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        if(partyId !=null){
            Party party = partyRepository.findById(partyId)
                    .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+partyId));
        }

         List<PaymentIn> paymentIns = paymentInRepository.findPaymentsByFilters(companyId,partyId,startDate,endDate);

        List<PaymentInResponse> paymentInResponses = paymentIns.stream()
                .map(paymentIn -> {
                    PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(paymentIn.getParty());
                    PaymentInResponse paymentInResponse = paymentInMapper.convertEntityToResponse(paymentIn);
                    paymentInResponse.setPartyResponseDto(partyResponseDto);
                    return paymentInResponse;
                }).toList();

        return paymentInResponses;

    }


    public PaymentInResponse getPaymentInById(Long paymentInId){
        PaymentIn paymentIn = paymentInRepository.findById(paymentInId)
                .orElseThrow(()-> new ResourceNotFoundException("Payment in not found by id : "+paymentInId));

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(paymentIn.getParty());
        PaymentInResponse paymentInResponse = paymentInMapper.convertEntityToResponse(paymentIn);
        paymentInResponse.setPartyResponseDto(partyResponseDto);

        return paymentInResponse;
    }


    @Transactional
    public PaymentInResponse updatePaymentInById(Long paymentInId,PaymentInRequest paymentInRequest){

        Party party = partyRepository.findById(paymentInRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+paymentInRequest.getPartyId()));

        PaymentIn paymentIn = paymentInRepository.findById(paymentInId)
                .orElseThrow(()-> new ResourceNotFoundException("Payment in not found by id : "+paymentInId));

        paymentInMapper.updatePaymentIn(paymentInRequest,paymentIn);
        paymentIn.setParty(party);
        paymentIn.setUpdateAt(LocalDateTime.now());

  partyLedgerService.updatePartyLedger(
                paymentIn.getParty(),
                paymentIn.getCompany(),
                paymentIn.getPaymentDate(),
                PartyTransactionType.PAYMENT_IN,
                paymentIn.getPaymentInId(),
                paymentIn.getReceiptNo(),
                paymentIn.getReceivedAmount(),
                0.0,
                0.0,
                paymentIn.getDescription()

        );


        partyActivityService.updatePartyActivity(
                paymentIn.getParty(),
                paymentIn.getCompany(),
                paymentIn.getPaymentInId(),
                paymentIn.getReceiptNo(),
                paymentIn.getPaymentDate(),
                PartyTransactionType.PAYMENT_IN,
                paymentIn.getReceivedAmount(),
                0.0,
                true,
                paymentIn.getDescription()
        );
        paymentInRepository.save(paymentIn);

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(paymentIn.getParty());
        PaymentInResponse paymentInResponse = paymentInMapper.convertEntityToResponse(paymentIn);
        paymentInResponse.setPartyResponseDto(partyResponseDto);

        return paymentInResponse;
    }


    @Transactional
    public String deletePaymentInById(Long paymentInId){
        PaymentIn paymentIn = paymentInRepository.findById(paymentInId)
                .orElseThrow(()-> new ResourceNotFoundException("Payment in not found by id : "+paymentInId));

        partyLedgerService.deletePartyLedger(PartyTransactionType.PAYMENT_IN,paymentIn.getPaymentInId());

        partyActivityService.deletePartyActivity(PartyTransactionType.PAYMENT_IN,paymentIn.getPaymentInId());

        paymentInRepository.delete(paymentIn);

        return "Payment In delete successfully.";
    }
}
