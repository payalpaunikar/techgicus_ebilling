package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.ChallanType;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.deliveryChallanDto.DeliveryChallanItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.deliveryChallanDto.DeliveryChallanItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.deliveryChallanDto.DeliveryChallanRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.deliveryChallanDto.DeliveryChallanResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SalePaymentResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.DeliveryChallanAlreadyClosedException;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.*;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import jakarta.persistence.Lob;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeliveryChallanService {

    private DeliveryChallanRepository deliveryChallanRepository;
    private DeliveryChallanItemRepository deliveryChallanItemRepository;
    private DeliveryChallanMapper deliveryChallanMapper;
    private DeliveryChallanItemMapper deliveryChallanItemMapper;
    private CompanyRepository companyRepository;
    private PartyRepository partyRepository;
    private PartyMapper partyMapper;
    private SaleRepository saleRepository;
    private SaleItemMapper saleItemMapper;
    private SaleMapper saleMapper;

    public DeliveryChallanService(DeliveryChallanRepository deliveryChallanRepository, DeliveryChallanItemRepository deliveryChallanItemRepository, DeliveryChallanMapper deliveryChallanMapper, DeliveryChallanItemMapper deliveryChallanItemMapper, CompanyRepository companyRepository, PartyRepository partyRepository, PartyMapper partyMapper, SaleRepository saleRepository, SaleItemMapper saleItemMapper, SaleMapper saleMapper) {
        this.deliveryChallanRepository = deliveryChallanRepository;
        this.deliveryChallanItemRepository = deliveryChallanItemRepository;
        this.deliveryChallanMapper = deliveryChallanMapper;
        this.deliveryChallanItemMapper = deliveryChallanItemMapper;
        this.companyRepository = companyRepository;
        this.partyRepository = partyRepository;
        this.partyMapper = partyMapper;
        this.saleRepository = saleRepository;
        this.saleItemMapper = saleItemMapper;
        this.saleMapper = saleMapper;
    }


    public DeliveryChallanResponse createDeliveryChallan(Long companyId, DeliveryChallanRequest deliveryChallanRequest){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        Party party = partyRepository.findById(deliveryChallanRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+deliveryChallanRequest.getPartyId()));

        DeliveryChallan deliveryChallan = deliveryChallanMapper.convertRequestToEntity(deliveryChallanRequest);
        deliveryChallan.setParty(party);
        deliveryChallan.setCompany(company);
        deliveryChallan.setChallanType(ChallanType.OPEN);

        List<DeliveryChallanItem> deliveryChallanItems = deliveryChallanRequest.getDeliveryChallanItemRequests()
                .stream()
                .map(deliveryChallanItemRequest -> {
                    DeliveryChallanItem deliveryChallanItem = deliveryChallanItemMapper.convertRequestToEntity(deliveryChallanItemRequest);
                    deliveryChallanItem.setDeliveryChallan(deliveryChallan);
                    return deliveryChallanItem;
                }).toList();

        deliveryChallan.setDeliveryChallanItems(deliveryChallanItems);

        deliveryChallanRepository.save(deliveryChallan);

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(deliveryChallan.getParty());
        List<DeliveryChallanItemResponse> deliveryChallanItemResponses = deliveryChallanItemMapper.convertEntityListToResponseList(deliveryChallan.getDeliveryChallanItems());

        DeliveryChallanResponse deliveryChallanResponse = deliveryChallanMapper.convertEntityToResponse(deliveryChallan);
        deliveryChallanResponse.setPartyResponseDto(partyResponseDto);
        deliveryChallanResponse.setDeliveryChallanItemResponses(deliveryChallanItemResponses);

        return deliveryChallanResponse;
    }


    public List<DeliveryChallanResponse> getDeliveryChallanListByFilter(Long companyId, ChallanType challanType){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        List<DeliveryChallan> deliveryChallans = deliveryChallanRepository.findAllByCompanyAndOptionalChallanType(company,challanType);

        List<DeliveryChallanResponse> deliveryChallanResponses = deliveryChallans.stream()
                .map(deliveryChallan -> {
                    PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(deliveryChallan.getParty());
                    List<DeliveryChallanItemResponse> deliveryChallanItemResponses = deliveryChallanItemMapper.convertEntityListToResponseList(deliveryChallan.getDeliveryChallanItems());
                    DeliveryChallanResponse deliveryChallanResponse = deliveryChallanMapper.convertEntityToResponse(deliveryChallan);
                    deliveryChallanResponse.setPartyResponseDto(partyResponseDto);
                    deliveryChallanResponse.setDeliveryChallanItemResponses(deliveryChallanItemResponses);
                    return deliveryChallanResponse;
                }).toList();

        return deliveryChallanResponses;
    }


    public DeliveryChallanResponse getDeliveryChallanById(Long deliveryChallanId){
        DeliveryChallan deliveryChallan = deliveryChallanRepository.findById(deliveryChallanId)
                .orElseThrow(()-> new ResourceNotFoundException("Delivery Challan not not with id : "+deliveryChallanId));

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(deliveryChallan.getParty());
        List<DeliveryChallanItemResponse> deliveryChallanItemResponses = deliveryChallanItemMapper.convertEntityListToResponseList(deliveryChallan.getDeliveryChallanItems());

        DeliveryChallanResponse deliveryChallanResponse = deliveryChallanMapper.convertEntityToResponse(deliveryChallan);
        deliveryChallanResponse.setPartyResponseDto(partyResponseDto);
        deliveryChallanResponse.setDeliveryChallanItemResponses(deliveryChallanItemResponses);

        return deliveryChallanResponse;
    }

    public DeliveryChallanResponse updateDeliveryChallanById(Long deliveryChallanId,DeliveryChallanRequest deliveryChallanRequest){
        DeliveryChallan deliveryChallan = deliveryChallanRepository.findById(deliveryChallanId)
                .orElseThrow(()-> new ResourceNotFoundException("Delivery Challan not not with id : "+deliveryChallanId));

        if (deliveryChallan.getChallanType().equals(ChallanType.CLOSED)){
            throw new DeliveryChallanAlreadyClosedException("Delivery Challan is already convert to sale, So it can't edit.");
        }

        Party party = partyRepository.findById(deliveryChallanRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+deliveryChallanRequest.getPartyId()));

        deliveryChallanMapper.updateDeliveryChallan(deliveryChallanRequest,deliveryChallan);
        deliveryChallan.setParty(party);
        deliveryChallan.getDeliveryChallanItems().clear();

        List<DeliveryChallanItem> deliveryChallanItems = deliveryChallanRequest.getDeliveryChallanItemRequests()
                .stream()
                .map(deliveryChallanItemRequest -> {
                    DeliveryChallanItem deliveryChallanItem = deliveryChallanItemMapper.convertRequestToEntity(deliveryChallanItemRequest);
                    deliveryChallanItem.setDeliveryChallan(deliveryChallan);
                    return deliveryChallanItem;
                }).toList();

        deliveryChallan.getDeliveryChallanItems().addAll(deliveryChallanItems);

        deliveryChallanRepository.save(deliveryChallan);

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(deliveryChallan.getParty());
        List<DeliveryChallanItemResponse> deliveryChallanItemResponses = deliveryChallanItemMapper.convertEntityListToResponseList(deliveryChallan.getDeliveryChallanItems());

        DeliveryChallanResponse deliveryChallanResponse = deliveryChallanMapper.convertEntityToResponse(deliveryChallan);
        deliveryChallanResponse.setPartyResponseDto(partyResponseDto);
        deliveryChallanResponse.setDeliveryChallanItemResponses(deliveryChallanItemResponses);

        return deliveryChallanResponse;
    }

    public String deleteDeliveryChallanById(Long deliveryChallanId){
        DeliveryChallan deliveryChallan = deliveryChallanRepository.findById(deliveryChallanId)
                .orElseThrow(()-> new ResourceNotFoundException("Delivery Challan not not with id : "+deliveryChallanId));

        deliveryChallanRepository.delete(deliveryChallan);

        return "Delivery Challan delete successfully.";
    }


    @Transactional
    public SaleResponse convertDeliveryChallanToSale(Long deliveryChallanId, SaleRequest saleRequest){
        DeliveryChallan deliveryChallan = deliveryChallanRepository.findById(deliveryChallanId)
                .orElseThrow(()-> new ResourceNotFoundException("Delivery Challan not not with id : "+deliveryChallanId));

        if (deliveryChallan.getChallanType().equals(ChallanType.CLOSED)){
            throw new DeliveryChallanAlreadyClosedException("Delivery Challan is already convert to sale.");
        }

        deliveryChallan.setChallanType(ChallanType.CLOSED);

        deliveryChallanRepository.save(deliveryChallan);

        Party party = partyRepository.findById(saleRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+saleRequest.getPartyId()));

        Sale sale = saleMapper.convertSaleRequestIntoSale(saleRequest);
        sale.setParty(party);
        sale.setCompany(deliveryChallan.getCompany());
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

        saleRepository.save(sale);

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(sale.getParty());
        List<SaleItemResponse> saleItemResponses = saleItemMapper.convertSaleItemListIntoSaleItmResponseList(sale.getSaleItem());

        List<SalePaymentResponse> salePaymentResponses = sale.getSalePayments().stream()
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

        SaleResponse saleResponse = saleMapper.convertSaleIntoSaleResponse(sale);
        saleResponse.setPartyResponseDto(partyResponseDto);
        saleResponse.setSaleItemResponses(saleItemResponses);
        saleResponse.setSalePaymentResponses(salePaymentResponses);

        return saleResponse;
    }
}
