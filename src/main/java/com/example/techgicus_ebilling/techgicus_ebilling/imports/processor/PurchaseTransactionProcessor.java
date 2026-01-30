package com.example.techgicus_ebilling.techgicus_ebilling.imports.processor;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Purchase;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PartyTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportContext;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.PurchaseRowData;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.extractor.PurchaseRowExtractor;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper.PurchaseEntityMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ModelUtill;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PurchaseRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.service.PartyActivityService;
import com.example.techgicus_ebilling.techgicus_ebilling.service.PartyLedgerService;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PurchaseTransactionProcessor implements TransactionProcessor{

    private final PurchaseRowExtractor purchaseRowExtractor;
    private final PurchaseEntityMapper purchaseEntityMapper;
    private final PurchaseRepository purchaseRepository;
    private final ModelUtill modelUtill;
    private final PartyActivityService partyActivityService;
    private final PartyLedgerService partyLedgerService;

    public PurchaseTransactionProcessor(PurchaseRowExtractor purchaseRowExtractor, PurchaseEntityMapper purchaseEntityMapper, PurchaseRepository purchaseRepository, ModelUtill modelUtill, PartyActivityService partyActivityService, PartyLedgerService partyLedgerService) {
        this.purchaseRowExtractor = purchaseRowExtractor;
        this.purchaseEntityMapper = purchaseEntityMapper;
        this.purchaseRepository = purchaseRepository;
        this.modelUtill = modelUtill;
        this.partyActivityService = partyActivityService;
        this.partyLedgerService = partyLedgerService;
    }

    @Override
    public boolean supports(String transactionType) {
        if (transactionType.toLowerCase().trim().equals("purchase"))return true;
        return false;
    }

    @Override
    public void process(Row row, Company company, ImportContext context) {

        PurchaseRowData rowData = purchaseRowExtractor.extract(row);

        Party party = modelUtill.findOrCreateParty(
                rowData.getPartyName(),
                rowData.getPartyPhone(),
                company
        );

        Optional<Purchase> purchaseOpt = purchaseRepository.findByBillNumberAndCompany(rowData.getBillNo(),company);

        Purchase existingPurchase = purchaseOpt.orElse(null);

        Purchase purchase = purchaseEntityMapper.toEntity(existingPurchase,rowData,company,party);

        boolean isNew = (existingPurchase == null);

        purchase = purchaseRepository.save(purchase);

        context.registerPurchase(purchase);

        if (isNew){
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
        }
        else{

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

        }
    }
}
