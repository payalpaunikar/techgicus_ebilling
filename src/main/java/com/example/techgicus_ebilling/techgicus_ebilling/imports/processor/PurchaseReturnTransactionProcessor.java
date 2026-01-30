package com.example.techgicus_ebilling.techgicus_ebilling.imports.processor;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseReturn;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PartyTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportContext;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.PurchaseRowData;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.extractor.PurchaseRowExtractor;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper.PurchaseReturnEntityMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ModelUtill;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PurchaseReturnRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.service.PartyActivityService;
import com.example.techgicus_ebilling.techgicus_ebilling.service.PartyLedgerService;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PurchaseReturnTransactionProcessor implements TransactionProcessor{

    private final PurchaseRowExtractor purchaseRowExtractor;
    private final PurchaseReturnEntityMapper purchaseReturnEntityMapper;
    private final PurchaseReturnRepository purchaseReturnRepository;
    private final ModelUtill modelUtill;
    private final PartyActivityService partyActivityService;
    private final PartyLedgerService partyLedgerService;


    public PurchaseReturnTransactionProcessor(PurchaseRowExtractor purchaseRowExtractor, PurchaseReturnEntityMapper purchaseReturnEntityMapper, PurchaseReturnRepository purchaseReturnRepository, ModelUtill modelUtill, PartyActivityService partyActivityService, PartyLedgerService partyLedgerService) {
        this.purchaseRowExtractor = purchaseRowExtractor;
        this.purchaseReturnEntityMapper = purchaseReturnEntityMapper;
        this.purchaseReturnRepository = purchaseReturnRepository;
        this.modelUtill = modelUtill;
        this.partyActivityService = partyActivityService;
        this.partyLedgerService = partyLedgerService;
    }

    @Override
    public boolean supports(String transactionType) {
        if (transactionType.toLowerCase().trim().equals("debit note"))return true;
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

        Optional<PurchaseReturn> existingPurchaseReturnOpt = purchaseReturnRepository.findByReturnNoAndCompany(rowData.getBillNo(),company);

        PurchaseReturn existingPurchaseReturn = existingPurchaseReturnOpt.orElse(null);

        PurchaseReturn purchaseReturn = purchaseReturnEntityMapper.toEntity(existingPurchaseReturn,rowData,company,party);

        boolean isNew = (existingPurchaseReturn == null);

        context.registerPurchaseReturn(purchaseReturn);

        if (isNew){
            // add party ledger entry in the database
            partyLedgerService.addLedgerEntry(
                    purchaseReturn.getParty(),
                    purchaseReturn.getCompany(),
                    purchaseReturn.getReturnDate(),
                    PartyTransactionType.PURCHASE_RETURN,
                    purchaseReturn.getPurchaseReturnId(),
                    purchaseReturn.getReturnNo(),
                    purchaseReturn.getTotalAmount(),
                    0.0,
                    purchaseReturn.getBalanceAmount(),
                    purchaseReturn.getDescription()
            );

            // add party activity entry in the database
            partyActivityService.addActivity(
                    purchaseReturn.getParty(),
                    purchaseReturn.getCompany(),
                    purchaseReturn.getPurchaseReturnId(),
                    purchaseReturn.getReturnNo(),
                    purchaseReturn.getReturnDate(),
                    PartyTransactionType.PURCHASE_RETURN,
                    purchaseReturn.getTotalAmount(),
                    purchaseReturn.getBalanceAmount(),
                    true,
                    purchaseReturn.getDescription());
        }
        else{

            // add party ledger entry in the database
            partyLedgerService.updatePartyLedger(
                    purchaseReturn.getParty(),
                    purchaseReturn.getCompany(),
                    purchaseReturn.getReturnDate(),
                    PartyTransactionType.PURCHASE_RETURN,
                    purchaseReturn.getPurchaseReturnId(),
                    purchaseReturn.getReturnNo(),
                    purchaseReturn.getTotalAmount(),
                    0.0,
                    purchaseReturn.getBalanceAmount(),
                    purchaseReturn.getDescription()
            );

            // add party activity entry in the database
            partyActivityService.updatePartyActivity(
                    purchaseReturn.getParty(),
                    purchaseReturn.getCompany(),
                    purchaseReturn.getPurchaseReturnId(),
                    purchaseReturn.getReturnNo(),
                    purchaseReturn.getReturnDate(),
                    PartyTransactionType.PURCHASE_RETURN,
                    purchaseReturn.getTotalAmount(),
                    purchaseReturn.getBalanceAmount(),
                    true,
                    purchaseReturn.getDescription());
        }
    }
}
