package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.ItemType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PartyTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.StockTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto.*;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.*;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.BadRequestException;
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
import java.util.ArrayList;
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
        private ItemRepository itemRepository;
        private StockTransactionRepository stockTransactionRepository;
        private PartyLedgerService partyLedgerService;
        private StockTransactionService stockTransactionService;
        private PartyActivityService partyActivityService;

    @Autowired
    public PurchaseService(CompanyRepository companyRepository, PartyRepository partyRepository, PurchaseRepository purchaseRepository, PurchaseItemRepository purchaseItemRepository, PurchaseMapper purchaseMapper, PurchaseItemMapper purchaseItemMapper, PartyMapper partyMapper, PurchasePaymentMapper purchasePaymentMapper, PurchasePaymentRepository purchasePaymentRepository, ItemRepository itemRepository, StockTransactionRepository stockTransactionRepository, PartyLedgerService partyLedgerService, StockTransactionService stockTransactionService, PartyActivityService partyActivityService) {
        this.companyRepository = companyRepository;
        this.partyRepository = partyRepository;
        this.purchaseRepository = purchaseRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.purchaseMapper = purchaseMapper;
        this.purchaseItemMapper = purchaseItemMapper;
        this.partyMapper = partyMapper;
        this.purchasePaymentMapper = purchasePaymentMapper;
        this.purchasePaymentRepository = purchasePaymentRepository;
        this.itemRepository = itemRepository;
        this.stockTransactionRepository = stockTransactionRepository;
        this.partyLedgerService = partyLedgerService;
        this.stockTransactionService = stockTransactionService;
        this.partyActivityService = partyActivityService;
    }

    @Transactional
    public PurchaseResponse createdPurchase(Long companyId, PurchaseRequest purchaseRequest){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Compnay not found with id : "+companyId));

        Party party = partyRepository.findById(purchaseRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+purchaseRequest.getPartyId()));

        if(purchaseRequest.getTotalAmount() < purchaseRequest.getSendAmount()){
            throw new IllegalArgumentException("Total amount cannot be less than  received amount");
        }

        Double paidAmount = purchaseRequest.getSendAmount() != null ?purchaseRequest.getSendAmount():0.0 ;
        Double totalAmount = purchaseRequest.getTotalAmount() != null ? purchaseRequest.getTotalAmount() : 0.0;

        Double balance = totalAmount - paidAmount;

        boolean isPaid = (balance <=0) ?true:false;
        boolean isOverDue = false;

        // set isOverdue
        if (purchaseRequest.getDueDate() != null && purchaseRequest.getDueDate().isBefore(LocalDate.now()) && balance > 0) {
            isOverDue = true;
        } else {
            isOverDue = false;
        }

        Purchase purchase = new Purchase();
        purchase = setPurchaseFields(purchaseRequest,company,party,purchaseRequest.getBalance(),isPaid,
                isOverDue,LocalDateTime.now(),LocalDateTime.now());


//        List<PurchaseItem> purchaseItems = purchaseItemMapper.convertPurchaseItemRequestListIntoPurchaseItemList(purchaseRequest.getPurchaseItemRequests());
//        for(PurchaseItem purchaseItem : purchaseItems){
//
//            purchaseItem.setPurchase(purchase);
//            purchaseItem.setCreatedAt(LocalDateTime.now());
//            purchaseItem.setUpdateAt(LocalDateTime.now());
//        }

        Purchase savedPurchase = purchaseRepository.save(purchase);

        List<PurchaseItem> purchaseItems = new ArrayList<>();
        List<StockTransaction> stockTransactionList = new ArrayList<>();

        for (PurchaseItemRequest purchaseItemRequest : purchaseRequest.getPurchaseItemRequests()){
            Item item = itemRepository.findById(purchaseItemRequest.getItemId())
                    .orElseThrow(()-> new ResourceNotFoundException("Item not found with id : "+purchaseItemRequest.getItemId()));

            PurchaseItem purchaseItem = new PurchaseItem();
            purchaseItem = setPurchaseItemFields(purchaseItemRequest,purchase,LocalDateTime.now(),LocalDateTime.now(),item);

            purchaseItems.add(purchaseItem);

            if(item.getItemType().equals(ItemType.PRODUCT)) {
                // management of item stock after sale happend
                 item = updateStockAfterPurchase(item,purchaseItem);

                itemRepository.save(item);

            }


                StockTransaction stockTransaction = new StockTransaction();
                 stockTransaction = stockTransactionService.addStockTransaction(item,
                         StockTransactionType.PURCHASE,purchaseItem.getTotalAmount(),purchase.getBillDate(),
                         purchase.getPurchaseId().toString());

                if(item.getItemType().equals(ItemType.PRODUCT)) {
                    stockTransaction.setQuantity(purchaseItem.getQuantity());
                }

              stockTransactionList.add(stockTransaction);
        }

        stockTransactionRepository.saveAll(stockTransactionList);
        savedPurchase.setPurchaseItems(purchaseItems);


        PurchasePayment purchasePayment = new PurchasePayment();
        String paymentDescription = "Paid During Purchase";

        purchasePayment = setPurchasePaymentFields(paymentDescription,purchaseRequest.getPaymentType(),
                purchaseRequest.getBillDate(),purchase,purchaseRequest.getSendAmount(),LocalDateTime.now(),LocalDateTime.now(),
                null,null);

        purchasePaymentRepository.save(purchasePayment);


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

        // add party activity entry in the database
        partyActivityService.addActivity(
                purchase.getParty(),
                purchase.getCompany(),
                purchase.getPurchaseId(),
                purchase.getBillNumber(),
                purchase.getBillDate(),
                PartyTransactionType.PURCHASE,
                purchase.getTotalAmount(),
                purchase.getBalance(),
                true,
                purchase.getPaymentDescription());

        Purchase savePurchase =  purchaseRepository.save(purchase);


        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(party);
        List<PurchaseItemResponse> purchaseItemResponses = purchaseItemMapper.convertPurchaseItemsIntoResponseList(savePurchase.getPurchaseItems());

        PurchasePaymentResponse purchasePaymentResponse = purchasePaymentMapper.convertPurchasePaymentIntoPurchasePaymentResponse(purchasePayment);

          List<PurchasePaymentResponse> purchasePaymentResponseList = new ArrayList<>();
          purchasePaymentResponseList.add(purchasePaymentResponse);

         PurchaseResponse purchaseResponse = new PurchaseResponse();
         purchaseResponse = setPurchaseResponseField(purchase,partyResponseDto,purchaseItemResponses,purchasePaymentResponseList);

        return purchaseResponse;

    }


    public List<PurchaseResponse> getCompanyPurchases(Long companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Compnay not found with id : "+companyId));

        List<Purchase> purchases = purchaseRepository.findAllByCompanyOrderByBillDateDesc(company);

        List<PurchaseResponse> purchaseResponses = purchases.stream()
                .map(purchase -> {

                    PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(purchase.getParty());
//                    List<PurchaseItemResponse> purchaseItemResponses = purchaseItemMapper.convertPurchaseItemsIntoResponseList(purchase.getPurchaseItems());

                    List<PurchaseItemResponse> purchaseItemResponses = new ArrayList<>();
                    purchaseItemResponses = convertPurchaseItemIntoResponseList(purchase.getPurchaseItems());
                    List<PurchasePaymentResponse> purchasePaymentResponses = purchasePaymentMapper.convertPurchasePaymentListIntoPurchasePaymentResponseList(purchase.getPurchasePayments());

                    PurchaseResponse purchaseResponse = purchaseMapper.convertPurchaseIntoPurchaseResponse(purchase);
                     purchaseResponse = setPurchaseResponseField(purchase,partyResponseDto,purchaseItemResponses,purchasePaymentResponses);

                    return purchaseResponse;
                }).toList();
        return purchaseResponses;
    }



    public PurchaseResponse getPurchaseById(Long purchaseId){
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(()-> new ResourceNotFoundException("Purchase not found with id : "+purchaseId));

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(purchase.getParty());

        List<PurchaseItemResponse> purchaseItemResponses = new ArrayList<>();
        purchaseItemResponses = convertPurchaseItemIntoResponseList(purchase.getPurchaseItems());

       // List<PurchaseItemResponse> purchaseItemResponses = purchaseItemMapper.convertPurchaseItemsIntoResponseList(purchase.getPurchaseItems());
        List<PurchasePaymentResponse> purchasePaymentResponses = purchasePaymentMapper.convertPurchasePaymentListIntoPurchasePaymentResponseList(purchase.getPurchasePayments());

        PurchaseResponse purchaseResponse = new PurchaseResponse();
        purchaseResponse = setPurchaseResponseField(purchase,partyResponseDto,purchaseItemResponses,purchasePaymentResponses);

        return purchaseResponse;
    }


    @Transactional
    public PurchaseResponse updatePurchaseById(Long purchaseId,PurchaseRequest purchaseRequest){
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(()-> new ResourceNotFoundException("Purchase not found with id : "+purchaseId));

        Party party = partyRepository.findById(purchaseRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+purchaseRequest.getPartyId()));

        if(purchaseRequest.getTotalAmount() < purchaseRequest.getSendAmount()){
            throw new IllegalArgumentException("Total amount cannot be less than  received amount");
        }

        List<PurchasePayment> purchasePaymentList = purchase.getPurchasePayments();

        // 🔴 RULE 1: Block update if multiple payments exist
        if (purchasePaymentList.size() > 1  && purchase.getSendAmount() != purchaseRequest.getSendAmount()) {
            throw new BadRequestException(
                    "Received amount cannot be modified because multiple payments already exist. " +
                            "Please update payments instead to maintain audit consistency."
            );
        }

//        PurchasePayment firstPayment = purchasePaymentList.get(0);
//        double previousReceivedAmount = purchase.getSendAmount();
//
//        if (previousReceivedAmount>firstPayment.getAmountPaid()){
//
//        }

        purchaseMapper.updatePurchaseByDto(purchaseRequest,purchase);
        purchase.setParty(party);
        purchase.setUpdateAt(LocalDateTime.now());
        purchase.setIsPaid(purchaseRequest.getIsPaid());
        purchase.setOverdue(purchaseRequest.getIsOverdue());

        purchase.getPurchaseItems().clear();

     //   List<PurchaseItem> purchaseItems = purchaseItemMapper.convertPurchaseItemRequestListIntoPurchaseItemList(purchaseRequest.getPurchaseItemRequests());

//        for (PurchaseItem purchaseItem : purchaseItems){
//            purchaseItem.setPurchase(purchase);
//            purchaseItem.setCreatedAt(LocalDateTime.now());
//            purchaseItem.setUpdateAt(LocalDateTime.now());
//        }

        List<PurchaseItem> purchaseItems = new ArrayList<>();
        List<StockTransaction> stockTransactionList = new ArrayList<>();

        for (PurchaseItemRequest purchaseItemRequest : purchaseRequest.getPurchaseItemRequests()) {
            Item item = itemRepository.findById(purchaseItemRequest.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item not found with id : " + purchaseItemRequest.getItemId()));

            PurchaseItem purchaseItem = new PurchaseItem();
            purchaseItem = setPurchaseItemFields(purchaseItemRequest, purchase, LocalDateTime.now(),
                    LocalDateTime.now(), item);

            purchaseItems.add(purchaseItem);

            if (item.getItemType().equals(ItemType.PRODUCT)) {
                // management of item stock after sale happend
                item.setTotalStockIn(item.getTotalStockIn() + purchaseItem.getQuantity());
                item.setStockValue(item.getStockValue() + item.getPurchasePrice());

                itemRepository.save(item);
            }

            StockTransaction stockTransaction = new StockTransaction();
            stockTransaction = stockTransactionService.addStockTransaction(item,
                    StockTransactionType.PURCHASE, purchaseItem.getTotalAmount(),
                    purchase.getBillDate(), purchase.getPurchaseId().toString());

            if (item.getItemType().equals(ItemType.PRODUCT)) {
                stockTransaction.setQuantity(purchaseItem.getQuantity());
            }

            stockTransactionList.add(stockTransaction);

        }


        if (purchasePaymentList.isEmpty()) {
            throw new RuntimeException("First payment is missing. Sale data is corrupted.");
        }

        PurchasePayment firstPayment = purchasePaymentList.get(0);
        String paymentDescription = "Paid During Purchase";
         updateSalePayment(firstPayment,paymentDescription,purchaseRequest.getPaymentType(),
                purchase.getBillDate(),purchase.getSendAmount(),LocalDateTime.now(),
                null,null);

        purchase.getPurchaseItems().addAll(purchaseItems);
        purchase.setPurchasePayments(purchasePaymentList);

        stockTransactionRepository.saveAll(stockTransactionList);


        // update party ledger entry in the database
        partyLedgerService.updatePartyLedger(
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

        // update party activity entry in the database
        partyActivityService.updatePartyActivity(
                purchase.getParty(),
                purchase.getCompany(),
                purchase.getPurchaseId(),
                purchase.getBillNumber(),
                purchase.getBillDate(),
                PartyTransactionType.PURCHASE,
                purchase.getTotalAmount(),
                purchase.getBalance(),
                true,
                purchase.getPaymentDescription());

      Purchase savePurchase =  purchaseRepository.save(purchase);

      PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(party);
      List<PurchaseItemResponse> purchaseItemResponses = purchaseItemMapper.convertPurchaseItemsIntoResponseList(savePurchase.getPurchaseItems());

      List<PurchasePaymentResponse>purchasePaymentResponseList = purchasePaymentMapper.convertPurchasePaymentListIntoPurchasePaymentResponseList(purchasePaymentList);

      PurchaseResponse purchaseResponse = purchaseMapper.convertPurchaseIntoPurchaseResponse(savePurchase);
      purchaseResponse = setPurchaseResponseField(purchase,partyResponseDto,purchaseItemResponses,purchasePaymentResponseList);

      return purchaseResponse;
    }


    @Transactional
    public String deletePurchaseById(Long purchaseId){
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(()-> new ResourceNotFoundException("Purchase not found with id : "+purchaseId));

        purchase.getPurchaseItems().stream()
                .forEach(purchaseItem-> {
                    Item item = purchaseItem.getItem();

                    if(item.getItemType().equals(ItemType.PRODUCT)) {
                        item.setTotalStockIn(item.getTotalStockIn() - purchaseItem.getQuantity());
                        item.setStockValue(item.getStockValue() - item.getPurchasePrice());
                    }

                    StockTransaction stockTransaction = stockTransactionRepository.findByReferenceNumberAndItemAndTransactionType(
                            purchase.getPurchaseId().toString(), item, StockTransactionType.PURCHASE
                    ).orElseThrow(() -> new ResourceNotFoundException("Stock transaction not found "));

                    stockTransactionRepository.delete(stockTransaction);

                });

        partyLedgerService.deletePartyLedger(PartyTransactionType.PURCHASE,purchase.getPurchaseId());
        partyActivityService.deletePartyActivity(PartyTransactionType.PURCHASE,purchase.getPurchaseId());

        purchaseRepository.delete(purchase);

        return "Purchase Delete successfully.";
    }


    @Transactional
    public PurchasePaymentResponse makePurchasePayment(Long purchaseId, PurchasePaymentRequest purchasePaymentRequest){

        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(()-> new ResourceNotFoundException("Purchase not found with id : "+purchaseId));

        if(purchase.getBalance()< purchasePaymentRequest.getAmountPaid()){
            throw new InvalidPaymentAmountException("Payment amount (" + purchasePaymentRequest.getAmountPaid() +
                    ") exceeds remaining balance (" + purchase.getBalance() + ").");
        }

        PurchasePayment purchasePayment = new PurchasePayment();
        purchasePayment = setPurchasePaymentFields(purchasePaymentRequest.getPaymentDescription(),
                purchasePaymentRequest.getPaymentType(),purchasePaymentRequest.getPaymentDate(),purchase,purchasePaymentRequest.getAmountPaid(),
                LocalDateTime.now(),
                LocalDateTime.now(),purchasePaymentRequest.getReceiptNo(),purchasePaymentRequest.getReferenceNumber());

        double newSendAmount = purchase.getSendAmount()+purchasePaymentRequest.getAmountPaid();
        purchase.setSendAmount(newSendAmount);
        purchase.setBalance(purchase.getTotalAmount()-purchase.getSendAmount());

        PurchasePayment savePurchasePayment = purchasePaymentRepository.save(purchasePayment);

        // update party ledger entry in the database
        partyLedgerService.updatePartyLedger(
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

        // update party activity entry in the database
        partyActivityService.updatePartyActivity(
                purchase.getParty(),
                purchase.getCompany(),
                purchase.getPurchaseId(),
                purchase.getBillNumber(),
                purchase.getBillDate(),
                PartyTransactionType.PURCHASE,
                purchase.getTotalAmount(),
                purchase.getBalance(),
                true,
                purchase.getPaymentDescription());


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



    public Purchase setPurchaseFields(PurchaseRequest purchaseRequest, Company company, Party party, Double balance, boolean isPaid, boolean isOverDue,LocalDateTime createdDate,
                                      LocalDateTime updateDateTime){
        Purchase purchase = purchaseMapper.convertPurchaseRequestIntoPurchase(purchaseRequest);
        purchase.setParty(party);
        purchase.setCompany(company);
        purchase.setCreatedAt(createdDate);
        purchase.setUpdateAt(updateDateTime);
        purchase.setIsPaid(purchaseRequest.getIsPaid());
        purchase.setOverdue(purchaseRequest.getIsOverdue());

        return purchase;
    }

    private PurchaseItem setPurchaseItemFields(PurchaseItemRequest purchaseItemRequest, Purchase purchase, LocalDateTime createdAt, LocalDateTime updateAt, Item item){
        PurchaseItem purchaseItem = purchaseItemMapper.convertPurchaseItemRequestIntoPurchaseItem(purchaseItemRequest);
        purchaseItem.setItem(item);
        purchaseItem.setPurchase(purchase);
        purchaseItem.setCreatedAt(createdAt);
        purchaseItem.setUpdateAt(updateAt);

        return purchaseItem;
    }


    // management of item stock after Purchase happend
    private Item updateStockAfterPurchase(Item item,PurchaseItem purchaseItem){
        double oldQty = item.getTotalStockIn();
        double oldValue = item.getStockValue();

        double addedValue = purchaseItem.getQuantity() * purchaseItem.getPricePerUnit();

        item.setTotalStockIn(oldQty+purchaseItem.getQuantity());
        item.setStockValue(oldValue+addedValue);
        item.setAvailableStock(item.getTotalStockIn());

        itemRepository.save(item);

        return item;
    }

    private PurchasePayment setPurchasePaymentFields(String paymentDescription, PaymentType paymentType, LocalDate paymentDate,
                                             Purchase purchase, Double amountPaid, LocalDateTime creationDateAndTime, LocalDateTime updationDateAndTime,
                                             String receiptNo, String referenceNumber){
        PurchasePayment purchasePayment = new PurchasePayment();
        purchasePayment.setPaymentDescription(paymentDescription);
        purchasePayment.setPaymentDate(paymentDate);
        purchasePayment.setAmountPaid(amountPaid);
        purchasePayment.setPaymentType(paymentType);
        purchasePayment.setPurchase(purchase);
        purchasePayment.setCreatedAt(creationDateAndTime);
        purchasePayment.setUpdateAt(updationDateAndTime);
        purchasePayment.setReceiptNo(receiptNo);
        purchasePayment.setReferenceNumber(referenceNumber);

        return purchasePayment;
    }

    private void updateSalePayment(
            PurchasePayment purchasePayment,
            String paymentDescription,
            PaymentType paymentType,
            LocalDate paymentDate,
            Double amountPaid,
            LocalDateTime updationDateAndTime,
            String receiptNo,
            String referenceNumber
    ) {
        purchasePayment.setPaymentDescription(paymentDescription);
        purchasePayment.setPaymentType(paymentType);
        purchasePayment.setPaymentDate(paymentDate);
        purchasePayment.setAmountPaid(amountPaid);
        purchasePayment.setUpdateAt(updationDateAndTime);
        purchasePayment.setReceiptNo(receiptNo);
        purchasePayment.setReferenceNumber(referenceNumber);
    }

    private PurchaseResponse setPurchaseResponseField(Purchase purchase, PartyResponseDto partyResponseDto, List<PurchaseItemResponse> purchaseItemResponses,
                                              List<PurchasePaymentResponse> purchasePaymentResponses){

        PurchaseResponse purchaseResponse = purchaseMapper.convertPurchaseIntoPurchaseResponse(purchase);
        purchaseResponse.setPartyResponseDto(partyResponseDto);
        purchaseResponse.setPurchaseItemResponses(purchaseItemResponses);
        purchaseResponse.setPurchasePaymentResponses(purchasePaymentResponses);

        return purchaseResponse;
    }

    private List<PurchaseItemResponse> convertPurchaseItemIntoResponseList(List<PurchaseItem> purchaseItems){
        List<PurchaseItemResponse> purchaseItemResponses = purchaseItems.stream()
                .map(purchaseItem -> {
                    Item item = purchaseItem.getItem();
                    PurchaseItemResponse purchaseItemResponse = purchaseItemMapper.convertPurchaseItemIntoResponse(purchaseItem);
                    purchaseItemResponse.setItemName(item.getItemName());
                    purchaseItemResponse.setItemHsnCode(item.getItemHsn());
                    purchaseItemResponse.setItemDescription(item.getDescription());
                    purchaseItemResponse.setUnit(item.getBaseUnit());
                    purchaseItemResponse.setItemId(item.getItemId());
                    return purchaseItemResponse;
                }).toList();

        return purchaseItemResponses;
    }

}
