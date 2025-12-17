package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.OrderType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PartyTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SalePaymentResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleOrderDto.SaleOrderItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleOrderDto.SaleOrderItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleOrderDto.SaleOrderRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleOrderDto.SaleOrderResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.SaleOrderAlreadyClosedException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.*;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
         private PartyActivityService partyActivityService;
         private PartyLedgerService partyLedgerService;
         private ItemRepository itemRepository;

    @Autowired
    public SaleOrderService(SaleOrderRepository saleOrderRepository, SaleOrderItemRepository saleOrderItemRepository, CompanyRepository companyRepository, PartyRepository partyRepository, PartyMapper partyMapper, SaleOrderMapper saleOrderMapper, SaleOrderItemMapper saleOrderItemMapper, SaleMapper saleMapper, SaleItemMapper saleItemMapper, SaleRepository saleRepository, PartyActivityService partyActivityService, PartyLedgerService partyLedgerService, ItemRepository itemRepository) {
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
        this.partyActivityService = partyActivityService;
        this.partyLedgerService = partyLedgerService;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public SaleOrderResponse createSaleOrder(Long companyId, SaleOrderRequest saleOrderRequest){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Compnay not found with id : "+companyId));

        Party party = partyRepository.findById(saleOrderRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+saleOrderRequest.getPartyId()));

        SaleOrder saleOrder = saleOrderMapper.convertRequestToEntity(saleOrderRequest);
        saleOrder.setCompany(company);
        saleOrder.setParty(party);
        saleOrder.setOrderType(OrderType.OPEN);

        List<SaleOrderItem> saleOrderItems = setSaleOrderItemListFields(saleOrderRequest.getSaleOrderItemRequests(),saleOrder);

//        List<SaleOrderItem> saleOrderItems = saleOrderRequest.getSaleOrderItemRequests()
//                .stream()
//                .map(saleOrderItemRequest -> {
//                    SaleOrderItem saleOrderItem = saleOrderItemMapper.convertRequestToEntity(saleOrderItemRequest);
//                    saleOrderItem.setSaleOrder(saleOrder);
//                    return saleOrderItem;
//                }).toList();

        saleOrder.setSaleOrderItems(saleOrderItems);

        saleOrderRepository.save(saleOrder);


        // add party activity
        partyActivityService.addActivity(
                saleOrder.getParty(),
                saleOrder.getCompany(),
                saleOrder.getSaleOrderId(),
                saleOrder.getOrderNo(),
                saleOrder.getOrderDate(),
                PartyTransactionType.SALE_ORDER,
                saleOrder.getTotalAmount(),
                saleOrder.getBalanceAmount(),
                false,
                saleOrder.getDescription()
        );

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(party);
        List<SaleOrderItemResponse> saleOrderItemResponses = setSaleOrderItemResponseListFields(saleOrder.getSaleOrderItems());
       // List<SaleOrderItemResponse> saleOrderItemResponses = saleOrderItemMapper.convertEntityListToResponseList(saleOrder.getSaleOrderItems());
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
                    List<SaleOrderItemResponse> saleOrderItemResponses = setSaleOrderItemResponseListFields(saleOrder.getSaleOrderItems());
                   // List<SaleOrderItemResponse> saleOrderItemResponses = saleOrderItemMapper.convertEntityListToResponseList(saleOrder.getSaleOrderItems());
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
        List<SaleOrderItemResponse> saleOrderItemResponses = setSaleOrderItemResponseListFields(saleOrder.getSaleOrderItems());
       // List<SaleOrderItemResponse> saleOrderItemResponses = saleOrderItemMapper.convertEntityListToResponseList(saleOrder.getSaleOrderItems());
        SaleOrderResponse saleOrderResponse = saleOrderMapper.convertEntityToResponse(saleOrder);
        saleOrderResponse.setPartyResponseDto(partyResponseDto);
        saleOrderResponse.setSaleOrderItemResponses(saleOrderItemResponses);
        return saleOrderResponse;
    }


    @Transactional
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

        List<SaleOrderItem> saleOrderItems = setSaleOrderItemListFields(orderRequest.getSaleOrderItemRequests(),saleOrder);

//        List<SaleOrderItem> saleOrderItems = orderRequest.getSaleOrderItemRequests()
//                .stream()
//                .map(saleOrderItemRequest -> {
//                    SaleOrderItem saleOrderItem = saleOrderItemMapper.convertRequestToEntity(saleOrderItemRequest);
//                    saleOrderItem.setSaleOrder(saleOrder);
//                    return saleOrderItem;
//                }).toList();

        saleOrder.getSaleOrderItems().addAll(saleOrderItems);

        saleOrderRepository.save(saleOrder);


        // add party activity
        partyActivityService.updatePartyActivity(
                saleOrder.getParty(),
                saleOrder.getCompany(),
                saleOrder.getSaleOrderId(),
                saleOrder.getOrderNo(),
                saleOrder.getOrderDate(),
                PartyTransactionType.SALE_ORDER,
                saleOrder.getTotalAmount(),
                saleOrder.getBalanceAmount(),
                false,
                saleOrder.getDescription()
        );

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(saleOrder.getParty());
        List<SaleOrderItemResponse> saleOrderItemResponses = setSaleOrderItemResponseListFields(saleOrder.getSaleOrderItems());
       // List<SaleOrderItemResponse> saleOrderItemResponses = saleOrderItemMapper.convertEntityListToResponseList(saleOrder.getSaleOrderItems());
        SaleOrderResponse saleOrderResponse = saleOrderMapper.convertEntityToResponse(saleOrder);
        saleOrderResponse.setPartyResponseDto(partyResponseDto);
        saleOrderResponse.setSaleOrderItemResponses(saleOrderItemResponses);

        return saleOrderResponse;
    }




    @Transactional
    public String deleteSaleOrderById(Long saleOrderId){
        SaleOrder saleOrder =saleOrderRepository.findById(saleOrderId)
                .orElseThrow(()-> new ResourceNotFoundException("Sale Order not found by id : "+saleOrderId));

        partyActivityService.deletePartyActivity(PartyTransactionType.SALE_ORDER,saleOrder.getSaleOrderId());

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


        partyActivityService.deletePartyActivity(PartyTransactionType.SALE_ORDER,saleOrder.getSaleOrderId());

        // add party ledger entry in the database
        partyLedgerService.addLedgerEntry(
                sale.getParty(),
                sale.getCompany(),
                sale.getInvoceDate(),
                PartyTransactionType.SALE,
                sale.getSaleId(),
                sale.getInvoiceNumber(),
                sale.getTotalAmount(),
                0.0,
                sale.getBalance(),
                sale.getPaymentDescription()
        );

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


     @Async
    public CompletableFuture<Double> getTotalOrderOfCurrentYear(Long companyId){

//        Double totalSaleOrder = saleOrderRepository.getTotalOrderOfCurrentYear(companyId);

         Double totalSaleOrder = Optional.ofNullable(
                 saleOrderRepository.getTotalOrderOfCurrentYear(companyId)
         ).orElse(0.0);


         return CompletableFuture.completedFuture(totalSaleOrder);
    }


    private SaleOrderItem setSaleOrderItemFields(SaleOrderItemRequest request,SaleOrder saleOrder){
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(()-> new ResourceNotFoundException("Item not found with id : "+request.getItemId()));
        SaleOrderItem saleOrderItem = saleOrderItemMapper.convertRequestToEntity(request);
        saleOrderItem.setItem(item);
        saleOrderItem.setSaleOrder(saleOrder);

        return saleOrderItem;
    }


    private List<SaleOrderItem> setSaleOrderItemListFields(List<SaleOrderItemRequest> requestList,SaleOrder saleOrder){
       List<SaleOrderItem> saleOrderItems = requestList.stream()
               .map(request-> setSaleOrderItemFields(request,saleOrder))
               .toList();

       return saleOrderItems;
    }

    private SaleOrderItemResponse setSaleOrderItemResponseFields(SaleOrderItem saleOrderItem){
      Item item = saleOrderItem.getItem();
      SaleOrderItemResponse saleOrderItemResponse = saleOrderItemMapper.convertEntityToResponse(saleOrderItem);
      saleOrderItemResponse.setItemId(item.getItemId());
      saleOrderItemResponse.setName(item.getItemName());

      return saleOrderItemResponse;
    }

    private List<SaleOrderItemResponse> setSaleOrderItemResponseListFields(List<SaleOrderItem> orderItemList){
       List<SaleOrderItemResponse> responseList = orderItemList.stream()
               .map(orderItem -> setSaleOrderItemResponseFields(orderItem))
               .toList();

       return responseList;
    }

}
