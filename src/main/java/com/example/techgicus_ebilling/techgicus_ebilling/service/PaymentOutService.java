package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PaymentIn;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PaymentOut;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PartyTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.paymentInDto.PaymentInRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.paymentInDto.PaymentInResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.paymentOutDto.PaymentOutRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.paymentOutDto.PaymentOutResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.PartyMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.PaymentOutMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CompanyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PartyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PaymentOutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentOutService {

        private PaymentOutRepository paymentOutRepository;
        private CompanyRepository companyRepository;
        private PartyRepository partyRepository;
        private PartyMapper partyMapper;
        private PaymentOutMapper paymentOutMapper;
        private PartyLedgerService partyLedgerService;
        private PartyActivityService partyActivityService;

    @Autowired
    public PaymentOutService(PaymentOutRepository paymentOutRepository, CompanyRepository companyRepository, PartyRepository partyRepository, PartyMapper partyMapper, PaymentOutMapper paymentOutMapper, PartyLedgerService partyLedgerService, PartyActivityService partyActivityService) {
        this.paymentOutRepository = paymentOutRepository;
        this.companyRepository = companyRepository;
        this.partyRepository = partyRepository;
        this.partyMapper = partyMapper;
        this.paymentOutMapper = paymentOutMapper;
        this.partyLedgerService = partyLedgerService;
        this.partyActivityService = partyActivityService;
    }


    @Transactional
    public PaymentOutResponse addPaymentOut(Long companyId, PaymentOutRequest paymentOutRequest){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Compnay not found with id : "+companyId));

        Party party = partyRepository.findById(paymentOutRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+paymentOutRequest.getPartyId()));

        PaymentOut paymentOut = paymentOutMapper.convertRequestToEntity(paymentOutRequest);
        paymentOut.setCompany(company);
        paymentOut.setParty(party);
        paymentOut.setCreateAt(LocalDateTime.now());
        paymentOut.setUpdateAt(LocalDateTime.now());


        partyLedgerService.addLedgerEntry(
                paymentOut.getParty(),
                paymentOut.getCompany(),
                paymentOut.getPaymentDate(),
                PartyTransactionType.PAYMENT_OUT,
                paymentOut.getPaymentOutId(),
                paymentOut.getReceiptNo(),
                0.0,
                paymentOut.getPaidAmount(),
                0.0,
                paymentOut.getDescription()

        );


        partyActivityService.addActivity(
                paymentOut.getParty(),
                paymentOut.getCompany(),
                paymentOut.getPaymentOutId(),
                paymentOut.getReceiptNo(),
                paymentOut.getPaymentDate(),
                PartyTransactionType.PAYMENT_OUT,
                paymentOut.getPaidAmount(),
                0.0,
                true,
                paymentOut.getDescription()
        );

        paymentOutRepository.save(paymentOut);

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(party);

        PaymentOutResponse paymentOutResponse = paymentOutMapper.convertEntityToResponse(paymentOut);
        paymentOutResponse.setPartyResponseDto(partyResponseDto);

        return paymentOutResponse;
    }


    public List<PaymentOutResponse> getAllPaymentOutByFilter(Long companyId, Long partyId, LocalDate startDate, LocalDate endDate){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        if(partyId !=null){
            Party party = partyRepository.findById(partyId)
                    .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+partyId));
        }

        List<PaymentOut> paymentOuts = paymentOutRepository.findPaymentsByFilters(companyId,partyId,startDate,endDate);

        List<PaymentOutResponse> paymentOutResponses = paymentOuts.stream()
                .map(paymentIn -> {
                    PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(paymentIn.getParty());
                    PaymentOutResponse paymentOutResponse = paymentOutMapper.convertEntityToResponse(paymentIn);
                    paymentOutResponse.setPartyResponseDto(partyResponseDto);

                    return paymentOutResponse;
                }).toList();

        return paymentOutResponses;

    }

    public PaymentOutResponse getPaymentOutById(Long paymentOutId){
        PaymentOut paymentOut = paymentOutRepository.findById(paymentOutId)
                .orElseThrow(()-> new ResourceNotFoundException("Payment out not found by id : "+paymentOutId));

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(paymentOut.getParty());
        PaymentOutResponse paymentOutResponse = paymentOutMapper.convertEntityToResponse(paymentOut);
        paymentOutResponse.setPartyResponseDto(partyResponseDto);

        return paymentOutResponse;
    }


    @Transactional
    public PaymentOutResponse updatePaymentOutById(Long paymentOutId,PaymentOutRequest paymentOutRequest){

        Party party = partyRepository.findById(paymentOutRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+paymentOutRequest.getPartyId()));

         PaymentOut paymentOut = paymentOutRepository.findById(paymentOutId)
                .orElseThrow(()-> new ResourceNotFoundException("Payment out not found by id : "+paymentOutId));

        paymentOutMapper.updateEntity(paymentOutRequest,paymentOut);
        paymentOut.setParty(party);
        paymentOut.setUpdateAt(LocalDateTime.now());

        partyLedgerService.updatePartyLedger(
                paymentOut.getParty(),
                paymentOut.getCompany(),
                paymentOut.getPaymentDate(),
                PartyTransactionType.PAYMENT_OUT,
                paymentOut.getPaymentOutId(),
                paymentOut.getReceiptNo(),
                0.0,
                paymentOut.getPaidAmount(),
                paymentOut.getPaidAmount(),
                paymentOut.getDescription()

        );


        partyActivityService.updatePartyActivity(
                paymentOut.getParty(),
                paymentOut.getCompany(),
                paymentOut.getPaymentOutId(),
                paymentOut.getReceiptNo(),
                paymentOut.getPaymentDate(),
                PartyTransactionType.PAYMENT_OUT,
                paymentOut.getPaidAmount(),
                0.0,
                true,
                paymentOut.getDescription()
        );

        paymentOutRepository.save(paymentOut);

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(paymentOut.getParty());
        PaymentOutResponse paymentOutResponse = paymentOutMapper.convertEntityToResponse(paymentOut);
        paymentOutResponse.setPartyResponseDto(partyResponseDto);

        return paymentOutResponse;
    }


    @Transactional
    public String deletePaymentOutById(Long paymentOutId){
       PaymentOut paymentOut = paymentOutRepository.findById(paymentOutId)
                .orElseThrow(()-> new ResourceNotFoundException("Payment out not found by id : "+paymentOutId));

        partyLedgerService.deletePartyLedger(PartyTransactionType.PAYMENT_OUT,paymentOut.getPaymentOutId());

        partyActivityService.deletePartyActivity(PartyTransactionType.PAYMENT_OUT,paymentOut.getPaymentOutId());

       paymentOutRepository.delete(paymentOut);

        return "Payment In delete successfully.";
    }

}
