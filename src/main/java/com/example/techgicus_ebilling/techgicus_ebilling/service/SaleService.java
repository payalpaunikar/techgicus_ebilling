package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.*;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.InvalidPaymentAmountException;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.PartyMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.SaleItemMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.SaleMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.SalePaymentMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidAlgorithmParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SaleService {

       private SaleRepository saleRepository;
       private SaleItemRepository saleItemRepository;
       private SaleMapper saleMapper;
       private CompanyRepository companyRepository;
       private PartyRepository partyRepository;
       private SaleItemMapper saleItemMapper;
       private PartyMapper partyMapper;
       private SalePaymentRepository salePaymentRepository;
       private SalePaymentMapper salePaymentMapper;

    @Autowired
    public SaleService(SaleRepository saleRepository, SaleItemRepository saleItemRepository, SaleMapper saleMapper, CompanyRepository companyRepository, PartyRepository partyRepository, SaleItemMapper saleItemMapper, PartyMapper partyMapper, SalePaymentRepository salePaymentRepository, SalePaymentMapper salePaymentMapper) {
        this.saleRepository = saleRepository;
        this.saleItemRepository = saleItemRepository;
        this.saleMapper = saleMapper;
        this.companyRepository = companyRepository;
        this.partyRepository = partyRepository;
        this.saleItemMapper = saleItemMapper;
        this.partyMapper = partyMapper;
        this.salePaymentRepository = salePaymentRepository;
        this.salePaymentMapper = salePaymentMapper;
    }

    @Transactional
    public SaleResponse createdSale(SaleRequest saleRequest, Long companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        Party party = partyRepository.findById(saleRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+saleRequest.getPartyId()));

        Sale sale = saleMapper.convertSaleRequestIntoSale(saleRequest);
        sale.setCompany(company);
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

        List<SaleItem> saleItems = saleItemMapper.convertSaleItemListRequestIntoSaleItemList(saleRequest.getSaleItems());

        for(SaleItem saleItem : saleItems){
            saleItem.setSale(sale);
            saleItem.setCreatedAt(LocalDateTime.now());
            saleItem.setUpdateAt(LocalDateTime.now());

        }

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
      // List<SaleItem> saleItemList = saleItemRepository.saveAll(saleItems);

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(sale.getParty());
        List<SaleItemResponse> saleItemResponses = saleItemMapper.convertSaleItemListIntoSaleItmResponseList(sale.getSaleItem());

        SaleResponse saleResponse = saleMapper.convertSaleIntoSaleResponse(saveSale);
        saleResponse.setPartyResponseDto(partyResponseDto);
        saleResponse.setSaleItemResponses(saleItemResponses);
        saleResponse.setInvoceDate(saveSale.getInvoceDate());
        saleResponse.setDueDate(saveSale.getDueDate());


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

        saleResponse.setSalePaymentResponses(salePaymentResponses);


        return saleResponse;
    }



    public List<SaleResponse> getComapnySales(Long companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        List<Sale> sales = saleRepository.findAllByCompanyOrderByInvoceDateDesc(company);

        List<SaleResponse> saleResponses = sales.stream()
                .map(sale -> {

                    PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(sale.getParty());
                    List<SaleItemResponse> saleItemResponses = saleItemMapper.convertSaleItemListIntoSaleItmResponseList(sale.getSaleItem());
                    List<SalePaymentResponse> salePaymentResponses = salePaymentMapper.convertSalePaymentListIntoSalePaymentResponseList(sale.getSalePayments());

                    SaleResponse saleResponse = saleMapper.convertSaleIntoSaleResponse(sale);
                    saleResponse.setPartyResponseDto(partyResponseDto);
                    saleResponse.setSaleItemResponses(saleItemResponses);
                    saleResponse.setSalePaymentResponses(salePaymentResponses);

                    return saleResponse;
                }).toList();

        return saleResponses;
    }


    public SaleResponse getSaleById(Long saleId){
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(()-> new ResourceNotFoundException("Sale not found with id : "+saleId));

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(sale.getParty());
        List<SaleItemResponse> saleItemResponses = saleItemMapper.convertSaleItemListIntoSaleItmResponseList(sale.getSaleItem());
        List<SalePaymentResponse> salePaymentResponses = salePaymentMapper.convertSalePaymentListIntoSalePaymentResponseList(sale.getSalePayments());


        SaleResponse saleResponse = saleMapper.convertSaleIntoSaleResponse(sale);
        saleResponse.setPartyResponseDto(partyResponseDto);
        saleResponse.setSaleItemResponses(saleItemResponses);
        saleResponse.setSalePaymentResponses(salePaymentResponses);

        return saleResponse;
    }


    @Transactional
    public SaleResponse updateSaleById(Long saleId,SaleRequest saleRequest){
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(()-> new ResourceNotFoundException("Sale not found with id : "+saleId));

        Party party = partyRepository.findById(saleRequest.getPartyId())
                        .orElseThrow(() -> new ResourceNotFoundException("Party not found with id : "+saleRequest.getPartyId()));

        saleMapper.updateSaleFromDto(saleRequest,sale);
        sale.setParty(party);

        sale.getSaleItem().clear();
        List<SaleItem> saleItems = saleItemMapper.convertSaleItemListRequestIntoSaleItemList(saleRequest.getSaleItems());

        for (SaleItem saleItem : saleItems){
            saleItem.setSale(sale);
            saleItem.setUpdateAt(LocalDateTime.now());

        }
        sale.getSaleItem().addAll(saleItems);

        Sale saveSale = saleRepository.save(sale);

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(party);
        List<SaleItemResponse> saleItemResponse = saleItemMapper.convertSaleItemListIntoSaleItmResponseList(saveSale.getSaleItem());
        SaleResponse saleResponse = saleMapper.convertSaleIntoSaleResponse(saveSale);
        saleResponse.setPartyResponseDto(partyResponseDto);
        saleResponse.setSaleItemResponses(saleItemResponse);

        return saleResponse;
    }


    public String deleteSaleById(Long saleId){
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(()-> new ResourceNotFoundException("Sale not found with id : "+saleId));

        saleRepository.delete(sale);

        return  "Sale delete successfully.";
    }


    @Transactional
    public SalePaymentResponse addSalePayment(Long saleId, SalePaymentRequest salePaymentRequest){

        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(()-> new ResourceNotFoundException("Sale not found by id : "+saleId));

        if (salePaymentRequest.getAmountPaid() > sale.getBalance()) {
           throw new InvalidPaymentAmountException("Payment amount (" + salePaymentRequest.getAmountPaid() +
                   ") exceeds remaining balance (" + sale.getBalance() + ").");
        }



        SalePayment salePayment = new SalePayment();
        salePayment.setSale(sale);
        salePayment.setPaymentDate(salePaymentRequest.getPaymentDate());
        salePayment.setPaymentType(salePaymentRequest.getPaymentType());
        salePayment.setAmountPaid(salePaymentRequest.getAmountPaid());
        salePayment.setReferenceNumber(salePaymentRequest.getReferenceNumber());
        salePayment.setPaymentDescription(salePaymentRequest.getPaymentDescription());
        salePayment.setReceiptNo(salePaymentRequest.getReceiptNo());
        salePayment.setCreatedAt(LocalDateTime.now());
        salePayment.setUpdateAt(LocalDateTime.now());



        // Update sale balance
        double newReceived = sale.getReceivedAmount()+salePaymentRequest.getAmountPaid();
        sale.setReceivedAmount(newReceived);
        sale.setBalance(sale.getTotalAmount()-newReceived);
        sale.setPaid(sale.getBalance() <= 0);

        sale.getSalePayments().add(salePayment);

        salePaymentRepository.save(salePayment);

        saleRepository.save(sale);

        SalePaymentResponse salePaymentResponse = new SalePaymentResponse();
        salePaymentResponse.setPaymentId(salePayment.getPaymentId());
        salePaymentResponse.setPaymentDate(salePayment.getPaymentDate());
        salePaymentResponse.setPaymentDescription(salePayment.getPaymentDescription());
        salePaymentResponse.setPaymentType(salePayment.getPaymentType());
        salePaymentResponse.setReferenceNumber(salePayment.getReferenceNumber());
        salePaymentResponse.setAmountPaid(salePayment.getAmountPaid());
        salePaymentResponse.setReceiptNo(salePayment.getReceiptNo());

        return salePaymentResponse;

    }


    public List<SaleResponse> getSaleReportByFilter(Long companyId,Long partyId,LocalDate startDate,LocalDate endDate){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        if (partyId !=null){
            Party party = partyRepository.findById(partyId)
                    .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+partyId));
        }

        List<Sale> sales = saleRepository.findAllByFilter(companyId,partyId,startDate,endDate);

        List<SaleResponse> saleResponses = sales.stream()
                .map(sale -> {

                    PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(sale.getParty());
                    List<SaleItemResponse> saleItemResponses = saleItemMapper.convertSaleItemListIntoSaleItmResponseList(sale.getSaleItem());
                    List<SalePaymentResponse> salePaymentResponses = salePaymentMapper.convertSalePaymentListIntoSalePaymentResponseList(sale.getSalePayments());

                    SaleResponse saleResponse = saleMapper.convertSaleIntoSaleResponse(sale);
                    saleResponse.setPartyResponseDto(partyResponseDto);
                    saleResponse.setSaleItemResponses(saleItemResponses);
                    saleResponse.setSalePaymentResponses(salePaymentResponses);

                    return saleResponse;
                }).toList();

        return saleResponses;
    }

}
