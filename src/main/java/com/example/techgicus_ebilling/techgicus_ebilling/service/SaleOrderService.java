package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.OrderType;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SalePaymentResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleOrderDto.SaleOrderItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleOrderDto.SaleOrderRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleOrderDto.SaleOrderResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.SaleOrderAlreadyClosedException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.*;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleOrderService {

         private SaleOrderRepository saleOrderRepository;
         private SaleOrderItemRepository saleOrderItemRepository;
         private CompanyRepository companyRepository;
         private PartyRepository partyRepository;
         private PartyMapper partyMapper;
         private SaleOrderMapper saleOrderMapper;
         private SaleOrderItemMapper saleOrderItemMapper;
         private SaleMapper saleMapper;
         private SaleItemMapper saleItemMapper;
         private SaleRepository saleRepository;

    @Autowired
    public SaleOrderService(SaleOrderRepository saleOrderRepository, SaleOrderItemRepository saleOrderItemRepository, CompanyRepository companyRepository, PartyRepository partyRepository, PartyMapper partyMapper, SaleOrderMapper saleOrderMapper, SaleOrderItemMapper saleOrderItemMapper, SaleMapper saleMapper, SaleItemMapper saleItemMapper, SaleRepository saleRepository) {
        this.saleOrderRepository = saleOrderRepository;
        this.saleOrderItemRepository = saleOrderItemRepository;
        this.companyRepository = companyRepository;
        this.partyRepository = partyRepository;
        this.partyMapper = partyMapper;
        this.saleOrderMapper = saleOrderMapper;
        this.saleOrderItemMapper = saleOrderItemMapper;
        this.saleMapper = saleMapper;
        this.saleItemMapper = saleItemMapper;
        this.saleRepository = saleRepository;
    }

    public SaleOrderResponse createSaleOrder(Long companyId, SaleOrderRequest saleOrderRequest){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Compnay not found with id : "+companyId));

        Party party = partyRepository.findById(saleOrderRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+saleOrderRequest.getPartyId()));

        SaleOrder saleOrder = saleOrderMapper.convertRequestToEntity(saleOrderRequest);
        saleOrder.setCompany(company);
        saleOrder.setParty(party);
        saleOrder.setOrderType(OrderType.OPEN);

        List<SaleOrderItem> saleOrderItems = saleOrderRequest.getSaleOrderItemRequests()
                .stream()
                .map(saleOrderItemRequest -> {
                    SaleOrderItem saleOrderItem = saleOrderItemMapper.convertRequestToEntity(saleOrderItemRequest);
                    saleOrderItem.setSaleOrder(saleOrder);
                    return saleOrderItem;
                }).toList();

        saleOrder.setSaleOrderItems(saleOrderItems);

        saleOrderRepository.save(saleOrder);

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(party);
        List<SaleOrderItemResponse> saleOrderItemResponses = saleOrderItemMapper.convertEntityListToResponseList(saleOrder.getSaleOrderItems());
        SaleOrderResponse saleOrderResponse = saleOrderMapper.convertEntityToResponse(saleOrder);
        saleOrderResponse.setPartyResponseDto(partyResponseDto);
        saleOrderResponse.setSaleOrderItemResponses(saleOrderItemResponses);

        return saleOrderResponse;
    }

    public List<SaleOrderResponse> getAllSaleOrderByCompanyIdAndOrderType(Long companyId,OrderType orderType){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Compnay not found with id : "+companyId));

        List<SaleOrder> saleOrders = saleOrderRepository.findAllByCompanyAndOptionalOrderType(companyId,orderType);

        List<SaleOrderResponse> saleOrderResponses = saleOrders.stream()
                .map(saleOrder -> {
                    PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(saleOrder.getParty());
                    List<SaleOrderItemResponse> saleOrderItemResponses = saleOrderItemMapper.convertEntityListToResponseList(saleOrder.getSaleOrderItems());
                    SaleOrderResponse saleOrderResponse = saleOrderMapper.convertEntityToResponse(saleOrder);
                    saleOrderResponse.setPartyResponseDto(partyResponseDto);
                    saleOrderResponse.setSaleOrderItemResponses(saleOrderItemResponses);
                    return saleOrderResponse;
                }).toList();

        return saleOrderResponses;
    }

    public SaleOrderResponse getSaleOrderById(Long saleOrderId){
        SaleOrder saleOrder =saleOrderRepository.findById(saleOrderId)
                .orElseThrow(()-> new ResourceNotFoundException("Sale Order not found by id : "+saleOrderId));

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(saleOrder.getParty());
        List<SaleOrderItemResponse> saleOrderItemResponses = saleOrderItemMapper.convertEntityListToResponseList(saleOrder.getSaleOrderItems());
        SaleOrderResponse saleOrderResponse = saleOrderMapper.convertEntityToResponse(saleOrder);
        saleOrderResponse.setPartyResponseDto(partyResponseDto);
        saleOrderResponse.setSaleOrderItemResponses(saleOrderItemResponses);
        return saleOrderResponse;
    }


    public SaleOrderResponse updateSaleOrderById(Long saleOrderId,SaleOrderRequest orderRequest){
        SaleOrder saleOrder =saleOrderRepository.findById(saleOrderId)
                .orElseThrow(()-> new ResourceNotFoundException("Sale Order not found by id : "+saleOrderId));

        if (saleOrder.getOrderType().equals(OrderType.CLOSE)){
            throw new SaleOrderAlreadyClosedException("You can not edit sale order becaused it is already closed.");
        }

        saleOrderMapper.updateSaleOrder(orderRequest,saleOrder);

        Party party = partyRepository.findById(orderRequest.getPartyId())
                        .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+orderRequest.getPartyId()));

        saleOrder.setParty(party);
        saleOrder.getSaleOrderItems().clear();

        List<SaleOrderItem> saleOrderItems = orderRequest.getSaleOrderItemRequests()
                .stream()
                .map(saleOrderItemRequest -> {
                    SaleOrderItem saleOrderItem = saleOrderItemMapper.convertRequestToEntity(saleOrderItemRequest);
                    saleOrderItem.setSaleOrder(saleOrder);
                    return saleOrderItem;
                }).toList();

        saleOrder.getSaleOrderItems().addAll(saleOrderItems);

        saleOrderRepository.save(saleOrder);

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(saleOrder.getParty());
        List<SaleOrderItemResponse> saleOrderItemResponses = saleOrderItemMapper.convertEntityListToResponseList(saleOrder.getSaleOrderItems());
        SaleOrderResponse saleOrderResponse = saleOrderMapper.convertEntityToResponse(saleOrder);
        saleOrderResponse.setPartyResponseDto(partyResponseDto);
        saleOrderResponse.setSaleOrderItemResponses(saleOrderItemResponses);

        return saleOrderResponse;
    }


    public String deleteSaleOrderById(Long saleOrderId){
        SaleOrder saleOrder =saleOrderRepository.findById(saleOrderId)
                .orElseThrow(()-> new ResourceNotFoundException("Sale Order not found by id : "+saleOrderId));

        saleOrderRepository.delete(saleOrder);

        return "Sale Order delete successfully.";
    }


    @Transactional
    public SaleResponse convertSaleOrderToSale(Long saleOrderId, SaleRequest saleRequest){
        SaleOrder saleOrder =saleOrderRepository.findById(saleOrderId)
                .orElseThrow(()-> new ResourceNotFoundException("Sale Order not found by id : "+saleOrderId));

        if(saleOrder.getOrderType().equals(OrderType.CLOSE)){
            throw new SaleOrderAlreadyClosedException("Sale Order is already convert to sale.");
        }

        saleOrder.setOrderType(OrderType.CLOSE);
        saleOrderRepository.save(saleOrder);


        Party party = partyRepository.findById(saleRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+saleRequest.getPartyId()));

        Sale sale = saleMapper.convertSaleRequestIntoSale(saleRequest);
        sale.setCompany(saleOrder.getCompany());
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
