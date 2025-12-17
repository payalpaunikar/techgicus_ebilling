package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.OrderType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PartyTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto.PurchaseItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto.PurchasePaymentResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto.PurchaseRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto.PurchaseResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseOrderDto.PurchaseOrderItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseOrderDto.PurchaseOrderItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseOrderDto.PurchaseOrderRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseOrderDto.PurchaseOrderResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SalePaymentResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleOrderDto.SaleOrderItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleOrderDto.SaleOrderRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleOrderDto.SaleOrderResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.PurchaseOrderAlreadyClosedException;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.SaleOrderAlreadyClosedException;
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
public class PurchaseOrderService {

       private PurchaseOrderRepository purchaseOrderRepository;
       private CompanyRepository companyRepository;
       private PartyRepository partyRepository;
       private PartyMapper partyMapper;
       private PurchaseOrderItemMapper purchaseOrderItemMapper;
       private PurchaseOrderMapper purchaseOrderMapper;
       private PurchaseMapper purchaseMapper;
       private PurchaseRepository purchaseRepository;
       private PurchasePaymentRepository purchasePaymentRepository;
       private PurchasePaymentMapper purchasePaymentMapper;
       private PurchaseItemMapper purchaseItemMapper;
       private PartyLedgerService partyLedgerService;
       private PartyActivityService partyActivityService;
       private ItemRepository itemRepository;

    @Autowired
    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository, CompanyRepository companyRepository, PartyRepository partyRepository, PartyMapper partyMapper, PurchaseOrderItemMapper purchaseOrderItemMapper, PurchaseOrderMapper purchaseOrderMapper, PurchaseMapper purchaseMapper, PurchaseRepository purchaseRepository, PurchasePaymentRepository purchasePaymentRepository, PurchasePaymentMapper purchasePaymentMapper, PurchaseItemMapper purchaseItemMapper, PartyLedgerService partyLedgerService, PartyActivityService partyActivityService, ItemRepository itemRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.companyRepository = companyRepository;
        this.partyRepository = partyRepository;
        this.partyMapper = partyMapper;
        this.purchaseOrderItemMapper = purchaseOrderItemMapper;
        this.purchaseOrderMapper = purchaseOrderMapper;
        this.purchaseMapper = purchaseMapper;
        this.purchaseRepository = purchaseRepository;
        this.purchasePaymentRepository = purchasePaymentRepository;
        this.purchasePaymentMapper = purchasePaymentMapper;
        this.purchaseItemMapper = purchaseItemMapper;
        this.partyLedgerService = partyLedgerService;
        this.partyActivityService = partyActivityService;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public PurchaseOrderResponse createdPurchaseOrder(Long companyId, PurchaseOrderRequest purchaseOrderRequest){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Compnay not found with id : "+companyId));

        Party party = partyRepository.findById(purchaseOrderRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+purchaseOrderRequest.getPartyId()));


        PurchaseOrder purchaseOrder = purchaseOrderMapper.convertRequestIntoEntity(purchaseOrderRequest);
         purchaseOrder.setCompany(company);
         purchaseOrder.setParty(party);
         purchaseOrder.setOrderType(OrderType.OPEN);

         List<PurchaseOrderItem> purchaseOrderItems = setPurchaseOrderItemList(purchaseOrderRequest.getPurchaseOrderItemRequests(),purchaseOrder);

//        List<PurchaseOrderItem> purchaseOrderItems = purchaseOrderRequest.getPurchaseOrderItemRequests()
//                        .stream()
//                                .map(purchaseOrderItemRequest -> {
//                                    PurchaseOrderItem purchaseOrderItem = purchaseOrderItemMapper.convertRequestToEntity(purchaseOrderItemRequest);
//                                    purchaseOrderItem.setPurchaseOrder(purchaseOrder);
//                                    return purchaseOrderItem;
//                                }).toList();

        purchaseOrder.setPurchaseOrderItems(purchaseOrderItems);

        purchaseOrderRepository.save(purchaseOrder);

        // add party activity entry in the database
        partyActivityService.addActivity(
                purchaseOrder.getParty(),
                purchaseOrder.getCompany(),
                purchaseOrder.getPurchaseOrderId(),
                purchaseOrder.getOrderNo(),
                purchaseOrder.getPurchaseDate(),
                PartyTransactionType.PURCHASE_ORDER,
                purchaseOrder.getTotalAmount(),
                purchaseOrder.getBalanceAmount(),
                false,
                purchaseOrder.getDescription());



        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(party);
        List<PurchaseOrderItemResponse> orderItemResponses = setPurchaseOrderItemResponseList(purchaseOrder.getPurchaseOrderItems());
       // List<PurchaseOrderItemResponse> purchaseOrderItemResponseList = purchaseOrderItemMapper.convertEntityListToResponseList(purchaseOrder.getPurchaseOrderItems());

        PurchaseOrderResponse purchaseOrderResponse = purchaseOrderMapper.convertEntityToResponse(purchaseOrder);
        purchaseOrderResponse.setPartyResponseDto(partyResponseDto);
        purchaseOrderResponse.setPurchaseOrderItemResponseList(orderItemResponses);

        return purchaseOrderResponse;
    }

    public List<PurchaseOrderResponse> getAllPurchaseOrderByCompanyIdAndOrderType(Long companyId,OrderType orderType){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Compnay not found with id : "+companyId));

        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAllByCompanyAndOptionalOrderType(companyId,orderType);

        List<PurchaseOrderResponse> purchaseOrderResponses = purchaseOrders.stream()
                .map(purchaseOrder -> {
                    PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(purchaseOrder.getParty());
                    List<PurchaseOrderItemResponse> purchaseOrderItemResponseList = setPurchaseOrderItemResponseList(purchaseOrder.getPurchaseOrderItems());
                   // List<PurchaseOrderItemResponse> purchaseOrderItemResponseList = purchaseOrderItemMapper.convertEntityListToResponseList(purchaseOrder.getPurchaseOrderItems());
                    PurchaseOrderResponse purchaseOrderResponse = purchaseOrderMapper.convertEntityToResponse(purchaseOrder);
                    purchaseOrderResponse.setPartyResponseDto(partyResponseDto);
                    purchaseOrderResponse.setPurchaseOrderItemResponseList(purchaseOrderItemResponseList);
                    return purchaseOrderResponse;
                }).toList();

        return purchaseOrderResponses;
    }

    public PurchaseOrderResponse getPurchaseOrderById(Long purchaseOrderId){
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(()-> new ResourceNotFoundException("Purchase order is not found with id : "+purchaseOrderId));

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(purchaseOrder.getParty());
        List<PurchaseOrderItemResponse> purchaseOrderItemResponseList = setPurchaseOrderItemResponseList(purchaseOrder.getPurchaseOrderItems());

        //  List<PurchaseOrderItemResponse> purchaseOrderItemResponseList = purchaseOrderItemMapper.convertEntityListToResponseList(purchaseOrder.getPurchaseOrderItems());
        PurchaseOrderResponse purchaseOrderResponse = purchaseOrderMapper.convertEntityToResponse(purchaseOrder);
        purchaseOrderResponse.setPartyResponseDto(partyResponseDto);
        purchaseOrderResponse.setPurchaseOrderItemResponseList(purchaseOrderItemResponseList);

        return purchaseOrderResponse;
    }


    @Transactional
    public PurchaseOrderResponse updatePurchaseOrderById(Long purchaseOrderId, PurchaseOrderRequest orderRequest){
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(()-> new ResourceNotFoundException("Purchase order is not found with id : "+purchaseOrderId));

        if (purchaseOrder.getOrderType().equals(OrderType.CLOSE)){
            throw new SaleOrderAlreadyClosedException("You can not edit purchase order becaused it is already closed.");
        }

        purchaseOrderMapper.updatePurchaseOrder(orderRequest,purchaseOrder);

        Party party = partyRepository.findById(orderRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+orderRequest.getPartyId()));

        purchaseOrder.setParty(party);
        purchaseOrder.getPurchaseOrderItems().clear();

        List<PurchaseOrderItem> purchaseOrderItems = setPurchaseOrderItemList(orderRequest.getPurchaseOrderItemRequests(),purchaseOrder);

//        List<PurchaseOrderItem> purchaseOrderItems = orderRequest.getPurchaseOrderItemRequests()
//                .stream()
//                .map(purchaseOrderItemRequest -> {
//                    PurchaseOrderItem purchaseOrderItem = purchaseOrderItemMapper.convertRequestToEntity(purchaseOrderItemRequest);
//                    purchaseOrderItem.setPurchaseOrder(purchaseOrder);
//                    return purchaseOrderItem;
//                }).toList();

        purchaseOrder.getPurchaseOrderItems().addAll(purchaseOrderItems);

        purchaseOrderRepository.save(purchaseOrder);

        // update party activity entry in the database
        partyActivityService.updatePartyActivity(
                purchaseOrder.getParty(),
                purchaseOrder.getCompany(),
                purchaseOrder.getPurchaseOrderId(),
                purchaseOrder.getOrderNo(),
                purchaseOrder.getPurchaseDate(),
                PartyTransactionType.PURCHASE_ORDER,
                purchaseOrder.getTotalAmount(),
                purchaseOrder.getBalanceAmount(),
                false,
                purchaseOrder.getDescription());



        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(purchaseOrder.getParty());
        List<PurchaseOrderItemResponse> purchaseOrderItemResponseList = setPurchaseOrderItemResponseList(purchaseOrder.getPurchaseOrderItems());

        // List<PurchaseOrderItemResponse> purchaseOrderItemResponseList = purchaseOrderItemMapper.convertEntityListToResponseList(purchaseOrder.getPurchaseOrderItems());
        PurchaseOrderResponse purchaseOrderResponse = purchaseOrderMapper.convertEntityToResponse(purchaseOrder);
        purchaseOrderResponse.setPartyResponseDto(partyResponseDto);
        purchaseOrderResponse.setPurchaseOrderItemResponseList(purchaseOrderItemResponseList);
        return purchaseOrderResponse;
    }


    @Transactional
    public String deletePurchaseOrderById(Long purchaseOrderId){
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(()-> new ResourceNotFoundException("Purchase order is not found with id : "+purchaseOrderId));


        partyActivityService.deletePartyActivity(PartyTransactionType.PURCHASE_ORDER,purchaseOrder.getPurchaseOrderId());

        purchaseOrderRepository.delete(purchaseOrder);

        return "Purchase Order delete successfully.";
    }


    @Transactional
    public PurchaseResponse convertPurchaseOrderToPurchase(Long purchaseOrderId, PurchaseRequest purchaseRequest){
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(()-> new ResourceNotFoundException("Purchase order is not found with id : "+purchaseOrderId));

        if(purchaseOrder.getOrderType().equals(OrderType.CLOSE)){
            throw new PurchaseOrderAlreadyClosedException("Purchase Order is already convert to purchase.");
        }

        purchaseOrder.setOrderType(OrderType.CLOSE);
        purchaseOrderRepository.save(purchaseOrder);


        Party party = partyRepository.findById(purchaseRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+purchaseRequest.getPartyId()));

        Purchase purchase = purchaseMapper.convertPurchaseRequestIntoPurchase(purchaseRequest);
        purchase.setCompany(purchaseOrder.getCompany());
        purchase.setParty(party);
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

        partyActivityService.deletePartyActivity(PartyTransactionType.PURCHASE_ORDER,purchaseOrder.getPurchaseOrderId());

        // add party ledger entry in the database
        partyLedgerService.addLedgerEntry(
                purchase.getParty(),
                purchase.getCompany(),
                purchase.getBillDate(),
                PartyTransactionType.PURCHASE,
                purchase.getPurchaseId(),
                purchase.getBillNumber(),
                0.0,
                purchase.getTotalAmount(),
                purchase.getBalance(),
                purchase.getPaymentDescription()
        );

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


    private PurchaseOrderItem setPurchaseOrderItem(PurchaseOrderItemRequest request,PurchaseOrder order){
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(()-> new ResourceNotFoundException("Item not found with id : "+request.getItemId()));
        PurchaseOrderItem purchaseOrderItem = purchaseOrderItemMapper.convertRequestToEntity(request);
        purchaseOrderItem.setItem(item);
        purchaseOrderItem.setPurchaseOrder(order);

        return purchaseOrderItem;
    }


    private List<PurchaseOrderItem> setPurchaseOrderItemList(List<PurchaseOrderItemRequest> requestList,PurchaseOrder order){
       List<PurchaseOrderItem> purchaseOrderItems = requestList.stream()
               .map(request-> setPurchaseOrderItem(request,order))
               .toList();

       return purchaseOrderItems;
    }


    private PurchaseOrderItemResponse setPurchaseOrderItemResponse(PurchaseOrderItem orderItem){
        Item item = orderItem.getItem();

        PurchaseOrderItemResponse response = purchaseOrderItemMapper.convertEntityToResponse(orderItem);
        response.setItemId(item.getItemId());
        response.setItemName(item.getItemName());

        return response;
    }


    private List<PurchaseOrderItemResponse> setPurchaseOrderItemResponseList(List<PurchaseOrderItem> orderItemList){
        List<PurchaseOrderItemResponse> responseList = orderItemList.stream()
                .map(orderItem-> setPurchaseOrderItemResponse(orderItem))
                .toList();

        return responseList;
    }

}
