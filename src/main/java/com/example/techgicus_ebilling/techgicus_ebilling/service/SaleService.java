package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.ItemType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PartyTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.StockTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.*;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.BadRequestException;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.InvalidPaymentAmountException;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.PartyMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.SaleItemMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.SaleMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.SalePaymentMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
       private ItemRepository itemRepository;
       private StockTransactionRepository stockTransactionRepository;
       private PartyLedgerService partyLedgerService;
       private PartyActivityService partyActivityService;
       private StockTransactionService stockTransactionService;

       private static final Logger log = LoggerFactory.getLogger(SaleService.class);

    @Autowired
    public SaleService(SaleRepository saleRepository, SaleItemRepository saleItemRepository, SaleMapper saleMapper, CompanyRepository companyRepository, PartyRepository partyRepository, SaleItemMapper saleItemMapper, PartyMapper partyMapper, SalePaymentRepository salePaymentRepository, SalePaymentMapper salePaymentMapper, ItemRepository itemRepository, StockTransactionRepository stockTransactionRepository, PartyLedgerService partyLedgerService, PartyActivityService partyActivityService, StockTransactionService stockTransactionService) {
        this.saleRepository = saleRepository;
        this.saleItemRepository = saleItemRepository;
        this.saleMapper = saleMapper;
        this.companyRepository = companyRepository;
        this.partyRepository = partyRepository;
        this.saleItemMapper = saleItemMapper;
        this.partyMapper = partyMapper;
        this.salePaymentRepository = salePaymentRepository;
        this.salePaymentMapper = salePaymentMapper;
        this.itemRepository = itemRepository;
        this.stockTransactionRepository = stockTransactionRepository;
        this.partyLedgerService = partyLedgerService;
        this.partyActivityService = partyActivityService;
        this.stockTransactionService = stockTransactionService;
    }

    @Transactional
    public SaleResponse createdSale(SaleRequest saleRequest, Long companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        Party party = partyRepository.findById(saleRequest.getPartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Party not found with id : "+saleRequest.getPartyId()));

        // 4️⃣ Handle null-safe numeric fields
        double receivedAmount = saleRequest.getReceivedAmount() != null ? saleRequest.getReceivedAmount() : 0.0;
        double totalAmount = saleRequest.getTotalAmount() != null ? saleRequest.getTotalAmount() : 0.0;

        // calculate balance
        Double balance = totalAmount - receivedAmount;

        // set isPaid
        boolean isPaid = (balance <= 0) ? true : false;
        boolean isOverDue = false;

        // set isOverdue
        if (saleRequest.getDueDate() != null && saleRequest.getDueDate().isBefore(LocalDate.now()) && balance > 0) {
            isOverDue = true;
        } else {
            isOverDue = false;
        }

        // set sale fields
        Sale sale = new Sale();
        sale = setSaleFields(saleRequest,company,party,balance,isPaid,isOverDue);

        //save sale
        saleRepository.save(sale);

        List<SaleItem> saleItemList = new ArrayList<>();
        List<StockTransaction> stockTransactionList = new ArrayList<>();
        List<SalePayment> payments = new ArrayList<>();

        // convert sales item request list into sale item list
        for(SaleItemRequest saleItemRequest : saleRequest.getSaleItems()){
            Item item = itemRepository.findById(saleItemRequest.getItemId())
                            .orElseThrow(()-> new ResourceNotFoundException("Item not found with id : "+saleItemRequest.getItemId()));

           SaleItem saleItem = new SaleItem();
           saleItem = setSaleItemFields(saleItemRequest,sale,LocalDateTime.now(),LocalDateTime.now(),item);

           // add sale item in the lsit
            saleItemList.add(saleItem);

            if(item.getItemType().equals(ItemType.PRODUCT)) {
                 item = updateStockAfterSale(item,saleItem);
                 // save item
                itemRepository.save(item);
            }


            // set stock transaction values
            StockTransaction stockTransaction = new StockTransaction();
            stockTransaction =  stockTransactionService.addStockTransaction(item,StockTransactionType.SALE,saleItem.getTotalAmount(),sale.getInvoceDate(),sale.getSaleId().toString());

            // if the item is product then stock transaction quantity add it.
            if (item.getItemType().equals(ItemType.PRODUCT)) {
                stockTransaction.setQuantity(saleItem.getQuantity());
            }
                // add stock transcation in list
                stockTransactionList.add(stockTransaction);

        }

        sale.setSaleItem(saleItemList);
        stockTransactionRepository.saveAll(stockTransactionList);

        // ---------------------- Payment Details ----------------------
        SalePayment salePayment = new SalePayment();

        String paymentDescription = "Received During Sale";
        //set Sale payment
        salePayment = setSalePaymentFields(paymentDescription,saleRequest.getPaymentType(),
                sale.getInvoceDate(),sale,sale.getReceivedAmount(),LocalDateTime.now(),LocalDateTime.now(),null,null);
        // add payment
        payments.add(salePayment);

        sale.setSalePayments(payments);

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

        // add party activity entry in the database
        partyActivityService.addActivity(
                sale.getParty(),
                sale.getCompany(),
                sale.getSaleId(),
                sale.getInvoiceNumber(),
                sale.getInvoceDate(),
                PartyTransactionType.SALE,
                sale.getTotalAmount(),
                sale.getBalance(),
                true,
                sale.getPaymentDescription());

        saleRepository.save(sale);

        // convert party into party response
        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(sale.getParty());
      //  List<SaleItemResponse> saleItemResponses = saleItemMapper.convertSaleItemListIntoSaleItmResponseList(sale.getSaleItem());

        List<SaleItemResponse> saleItemResponses = new ArrayList<>();
        saleItemResponses = convertSaleItemListIntoResponseList(sale);

        List<SalePaymentResponse> salePaymentResponseList = new ArrayList<>();
        salePaymentResponseList = convertSalePaymentListIntoSalePaymentResponseList(sale.getSalePayments());

        SaleResponse saleResponse = new SaleResponse();
        saleResponse = setSaleResponseField(sale,partyResponseDto,saleItemResponses,sale.getInvoceDate(),sale.getDueDate(),salePaymentResponseList);

        return saleResponse;
    }



    public List<SaleResponse> getComapnySales(Long companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        List<Sale> sales = saleRepository.findAllByCompanyOrderByInvoceDateDesc(company);

        List<SaleResponse> saleResponseList = sales.stream()
                .map(sale -> {
                    PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(sale.getParty());

                    //convert saleItem List into sale item response list
                    List<SaleItemResponse> saleItemResponses = new ArrayList<>();
                    saleItemResponses = convertSaleItemListIntoResponseList(sale);

                    //convert sale payment list into response list
                    List<SalePaymentResponse> salePaymentResponses = salePaymentMapper.convertSalePaymentListIntoSalePaymentResponseList(sale.getSalePayments());

                    // set sale response field
                    SaleResponse saleResponse = new SaleResponse();
                    saleResponse = setSaleResponseField(sale,partyResponseDto,saleItemResponses,sale.getInvoceDate(),sale.getDueDate(),salePaymentResponses);

                    return saleResponse;
                }).toList();


        return saleResponseList;
    }


    public SaleResponse getSaleById(Long saleId){
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(()-> new ResourceNotFoundException("Sale not found with id : "+saleId));

        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(sale.getParty());

        //convert sale item list into response list
        List<SaleItemResponse> saleItemResponses = new ArrayList<>();
        saleItemResponses = convertSaleItemListIntoResponseList(sale);

        List<SalePaymentResponse> salePaymentResponses = salePaymentMapper.convertSalePaymentListIntoSalePaymentResponseList(sale.getSalePayments());

        SaleResponse saleResponse = new SaleResponse();
        saleResponse = setSaleResponseField(sale,partyResponseDto,saleItemResponses,sale.getInvoceDate(),sale.getDueDate(),salePaymentResponses);

        return saleResponse;
    }


    @Transactional
    public SaleResponse updateSaleById(Long saleId,SaleRequest saleRequest){
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(()-> new ResourceNotFoundException("Sale not found with id : "+saleId));

        Party party = partyRepository.findById(saleRequest.getPartyId())
                        .orElseThrow(() -> new ResourceNotFoundException("Party not found with id : "+saleRequest.getPartyId()));


        if(sale.getReceivedAmount()>saleRequest.getTotalAmount()){
            throw new IllegalArgumentException("Total amount cannot be less than the already received amount");
        }

        List<SalePayment> paymentList = sale.getSalePayments();

        // 🔴 RULE 1: Block update if multiple payments exist
        if (paymentList.size() > 1  && sale.getReceivedAmount() != saleRequest.getReceivedAmount()) {
            throw new BadRequestException(
                    "Sale cannot be updated because multiple payments are already done."
            );
        }

        saleMapper.updateSaleFromDto(saleRequest,sale);
        sale.setParty(party);

        sale.getSaleItem().clear();

        List<SaleItem> saleItems = new ArrayList<>();
        List<StockTransaction> stockTransactionList = new ArrayList<>();

        for(SaleItemRequest saleItemRequest : saleRequest.getSaleItems()){
            Item item = itemRepository.findById(saleItemRequest.getItemId())
                    .orElseThrow(()-> new ResourceNotFoundException("Item not found with id : "+saleItemRequest.getItemId()));

            SaleItem saleItem = new SaleItem();
            saleItem = setSaleItemFields(saleItemRequest,sale,sale.getCreatedAt(),LocalDateTime.now(),item);

            if(item.getItemType().equals(ItemType.PRODUCT)) {
                // management of item stock after sale happend
                item = updateStockAfterSale(item, saleItem);
                itemRepository.save(item);
            }

                StockTransaction stockTransaction = new StockTransaction();
                stockTransactionService.addStockTransaction(item,StockTransactionType.SALE,saleItem.getTotalAmount(),sale.getInvoceDate(),sale.getSaleId().toString());

                // if the item is product then stock transaction quantity add it.
                if (item.getItemType().equals(ItemType.PRODUCT)) {
                    stockTransaction.setQuantity(saleItem.getQuantity());
                }

                stockTransactionList.add(stockTransaction);

            saleItems.add(saleItem);
        }



        if (paymentList.isEmpty()) {
            throw new RuntimeException("First payment is missing. Sale data is corrupted.");
        }

        SalePayment firstPayment = paymentList.get(0); // Only update this
        String paymentDescription = "Received During Sale";
         updateSalePayment(firstPayment,paymentDescription,saleRequest.getPaymentType(),sale.getInvoceDate(),
                sale.getReceivedAmount(),LocalDateTime.now(),
                null,null);


        //set all sale item list in the sale
        sale.getSaleItem().addAll(saleItems);
        sale.setSalePayments(paymentList);

        stockTransactionRepository.saveAll(stockTransactionList);



        // update party ledger entry in the database
        partyLedgerService.updatePartyLedger(
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

        // update party activity entry in the database
        partyActivityService.updatePartyActivity(
                sale.getParty(),
                sale.getCompany(),
                sale.getSaleId(),
                sale.getInvoiceNumber(),
                sale.getInvoceDate(),
                PartyTransactionType.SALE,
                sale.getTotalAmount(),
                sale.getBalance(),
                true,
                sale.getPaymentDescription());

        saleRepository.save(sale);


        PartyResponseDto partyResponseDto = partyMapper.convertEntityIntoResponse(party);
        List<SaleItemResponse> saleItemResponses = new ArrayList<>();
        saleItemResponses = convertSaleItemListIntoResponseList(sale);


        List<SalePaymentResponse> salePaymentResponses = new ArrayList<>();
        salePaymentResponses = convertSalePaymentListIntoSalePaymentResponseList(sale.getSalePayments());

        SaleResponse saleResponse = new SaleResponse();
        saleResponse = setSaleResponseField(sale,partyResponseDto,saleItemResponses,sale.getInvoceDate(),sale.getDueDate(),salePaymentResponses);

        return saleResponse;
    }


    @Transactional
    public String deleteSaleById(Long saleId){
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(()-> new ResourceNotFoundException("Sale not found with id : "+saleId));



        sale.getSaleItem().stream()
                        .forEach(saleItem -> {
                            Item item = saleItem.getItem();

                            if(item.getItemType().equals(ItemType.PRODUCT)) {
                                item.setTotalStockIn(item.getTotalStockIn() + saleItem.getQuantity());
                                item.setStockValue(item.getStockValue() + item.getPurchasePrice());
                                item.setAvailableStock(item.getTotalStockIn());
                            }

                                StockTransaction stockTransaction = stockTransactionRepository.findByReferenceNumberAndItemAndTransactionType(
                                        sale.getSaleId().toString(), item, StockTransactionType.SALE
                                ).orElseThrow(() -> new ResourceNotFoundException("Stock transaction not found "));

                                stockTransactionRepository.delete(stockTransaction);

                        });

        partyLedgerService.deletePartyLedger(PartyTransactionType.SALE,sale.getSaleId());
        partyActivityService.deletePartyActivity(PartyTransactionType.SALE,sale.getSaleId());

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
        salePayment = setSalePaymentFields(salePaymentRequest.getPaymentDescription(),salePaymentRequest.getPaymentType(),
                salePaymentRequest.getPaymentDate(),sale,salePaymentRequest.getAmountPaid(),LocalDateTime.now(),LocalDateTime.now(),
                salePaymentRequest.getReceiptNo(),salePaymentRequest.getReferenceNumber());



        // Update sale balance
        double newReceived = sale.getReceivedAmount()+salePaymentRequest.getAmountPaid();
        sale.setReceivedAmount(newReceived);
        sale.setBalance(sale.getTotalAmount()-newReceived);
        sale.setPaid(sale.getBalance() <= 0);

        sale.getSalePayments().add(salePayment);

        salePaymentRepository.save(salePayment);

        // update party ledger entry in the database
        partyLedgerService.updatePartyLedger(
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

        // update party activity entry in the database
        partyActivityService.updatePartyActivity(
                sale.getParty(),
                sale.getCompany(),
                sale.getSaleId(),
                sale.getInvoiceNumber(),
                sale.getInvoceDate(),
                PartyTransactionType.SALE,
                sale.getTotalAmount(),
                sale.getBalance(),
                true,
                sale.getPaymentDescription());

        saleRepository.save(sale);

        SalePaymentResponse salePaymentResponse = new SalePaymentResponse();
        salePaymentResponse = setSalePaymentResponse(salePayment.getPaymentId(),salePayment.getPaymentDate(),salePayment.getPaymentDescription(),
                salePayment.getPaymentType(),salePayment.getReferenceNumber(),salePayment.getAmountPaid(),
                salePayment.getReceiptNo());

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


   private SalePayment setSalePaymentFields(String paymentDescription, PaymentType paymentType,LocalDate paymentDate,
                                      Sale sale,Double amountPaid,LocalDateTime creationDateAndTime,LocalDateTime updationDateAndTime,
                                            String receiptNo,String referenceNumber){
       SalePayment salePayment = new SalePayment();
       salePayment.setPaymentDescription(paymentDescription);
       salePayment.setPaymentType(paymentType);
       salePayment.setPaymentDate(paymentDate);
       salePayment.setSale(sale);
       salePayment.setCreatedAt(creationDateAndTime);
       salePayment.setUpdateAt(updationDateAndTime);
       salePayment.setAmountPaid(amountPaid);
       salePayment.setReceiptNo(receiptNo);
       salePayment.setReferenceNumber(referenceNumber);

       return salePayment;
   }

    private void updateSalePayment(
            SalePayment salePayment,
            String paymentDescription,
            PaymentType paymentType,
            LocalDate paymentDate,
            Double amountPaid,
            LocalDateTime updationDateAndTime,
            String receiptNo,
            String referenceNumber
    ) {
        salePayment.setPaymentDescription(paymentDescription);
        salePayment.setPaymentType(paymentType);
        salePayment.setPaymentDate(paymentDate);
        salePayment.setAmountPaid(amountPaid);
        salePayment.setUpdateAt(updationDateAndTime);
        salePayment.setReceiptNo(receiptNo);
        salePayment.setReferenceNumber(referenceNumber);
    }



    private List<SaleItemResponse> convertSaleItemListIntoResponseList(Sale sale){
       List<SaleItemResponse> saleItemResponses = sale.getSaleItem().stream()
               .map(saleItem -> {
                   Item item = saleItem.getItem();
                   SaleItemResponse saleItemResponse = saleItemMapper.covertSaleItemIntoSaleItemResponse(saleItem);
                   // saleItemResponse.setSaleItemId(saleItem.getSaleItemId());
                   saleItemResponse.setItemName(item.getItemName());
                   saleItemResponse.setItemHsnCode(item.getItemHsn());
                   saleItemResponse.setUnit(item.getBaseUnit());
                   saleItemResponse.setItemId(item.getItemId());

                   return saleItemResponse;
               }).toList();

       return saleItemResponses;
   }


   private List<SalePaymentResponse> convertSalePaymentListIntoSalePaymentResponseList(List<SalePayment> salePayments){
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

       return salePaymentResponses;
   }


   public Sale setSaleFields(SaleRequest saleRequest,Company company,Party party,Double balance,boolean isPaid,boolean isOverDue){
       Sale sale = saleMapper.convertSaleRequestIntoSale(saleRequest);
       sale.setCompany(company);
       sale.setParty(party);
       sale.setInvoceDate(saleRequest.getInvoceDate());
       sale.setDueDate(saleRequest.getDueDate());
       sale.setCreatedAt(LocalDateTime.now());
       sale.setUpdatedAt(LocalDateTime.now());
       sale.setBalance(balance);
       sale.setOverdue(isOverDue);
       sale.setPaid(isPaid);

       return sale;
   }


   public SaleItem setSaleItemFields(SaleItemRequest saleItemRequest, Sale sale, LocalDateTime createdAt, LocalDateTime updateAt, Item item){
       SaleItem saleItem = saleItemMapper.convertSaleItemRequestIntoSaleItem(saleItemRequest);
       saleItem.setSale(sale);
       saleItem.setCreatedAt(createdAt);
       saleItem.setUpdateAt(updateAt);
       saleItem.setItem(item);

       return saleItem;
   }


    // management of item stock after sale happend
   private Item updateStockAfterSale(Item item,SaleItem saleItem){
       double oldQty = item.getTotalStockIn();
       double oldValue = item.getStockValue();

       log.info("old qty : "+oldQty);

       log.info("Old value : "+oldValue);


       //double avgCost = oldValue / oldQty ;

       double avgCost = (oldQty>0) ? oldValue/oldQty :0.0;

       log.info("Avg  : "+avgCost);

       double reducedValue = (avgCost>0) ? saleItem.getQuantity() * avgCost:0.0;

       log.info("reduced value : "+reducedValue);

       item.setTotalStockIn(oldQty-saleItem.getQuantity());
       item.setStockValue(oldValue-reducedValue);
       item.setAvailableStock(item.getTotalStockIn());

       return item;
   }

   private SaleResponse setSaleResponseField(Sale sale,PartyResponseDto partyResponseDto,List<SaleItemResponse> saleItemResponses,LocalDate inovoiceDate,LocalDate dueDate,
                                             List<SalePaymentResponse> salePaymentResponses){
       SaleResponse saleResponse = saleMapper.convertSaleIntoSaleResponse(sale);
       saleResponse.setPartyResponseDto(partyResponseDto);
       saleResponse.setSaleItemResponses(saleItemResponses);
       saleResponse.setInvoceDate(inovoiceDate);
       saleResponse.setDueDate(dueDate);
       saleResponse.setSalePaymentResponses(salePaymentResponses);
       return saleResponse;
   }

   private SalePaymentResponse setSalePaymentResponse(Long paymentId, LocalDate paymentDate, String paymentDescription,
                                                      PaymentType paymentType, String referenceNumber, Double amountPaid,
                                                      String receiptNo){
       SalePaymentResponse salePaymentResponse = new SalePaymentResponse();
       salePaymentResponse.setPaymentId(paymentId);
       salePaymentResponse.setPaymentDate(paymentDate);
       salePaymentResponse.setPaymentDescription(paymentDescription);
       salePaymentResponse.setPaymentType(paymentType);
       salePaymentResponse.setReferenceNumber(referenceNumber);
       salePaymentResponse.setAmountPaid(amountPaid);
       salePaymentResponse.setReceiptNo(receiptNo);

       return salePaymentResponse;
   }


}
