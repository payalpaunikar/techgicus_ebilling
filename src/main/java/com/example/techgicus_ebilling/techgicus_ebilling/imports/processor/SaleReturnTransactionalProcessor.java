package com.example.techgicus_ebilling.techgicus_ebilling.imports.processor;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleReturn;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PartyTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportContext;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.SaleRowData;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.extractor.SaleRowExtractor;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper.SaleReturnEntityMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ModelUtill;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.SaleReturnRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.service.PartyActivityService;
import com.example.techgicus_ebilling.techgicus_ebilling.service.PartyLedgerService;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class SaleReturnTransactionalProcessor implements TransactionProcessor{

    private final SaleRowExtractor saleRowExtractor;
    private final SaleReturnEntityMapper saleReturnEntityMapper;
    private final SaleReturnRepository saleReturnRepository;
    private final ModelUtill modelUtill;
    private final PartyActivityService partyActivityService;
    private final PartyLedgerService partyLedgerService;


    public SaleReturnTransactionalProcessor(SaleRowExtractor saleRowExtractor, SaleReturnEntityMapper saleReturnEntityMapper, SaleReturnRepository saleReturnRepository, ModelUtill modelUtill, PartyActivityService partyActivityService, PartyLedgerService partyLedgerService) {
        this.saleRowExtractor = saleRowExtractor;
        this.saleReturnEntityMapper = saleReturnEntityMapper;
        this.saleReturnRepository = saleReturnRepository;
        this.modelUtill = modelUtill;
        this.partyActivityService = partyActivityService;
        this.partyLedgerService = partyLedgerService;
    }

    private final static Logger log = LoggerFactory.getLogger(SaleTransactionProcessor.class);


    @Override
    public boolean supports(String transactionType) {
        log.info("transaction type is : "+transactionType);
        if (transactionType.toLowerCase().trim().equals("credit note"))return true;
        return false;
    }

    @Override
    public void process(Row row, Company company, ImportContext context) {
        log.info("row number : "+row.getRowNum());

        SaleRowData rowData = saleRowExtractor.extract(row);

        Party party = modelUtill.findOrCreateParty(
                rowData.getPartyName(),
                rowData.getPartyPhone(),
                company
        );

        Optional<SaleReturn> saleReturnOpt = saleReturnRepository.findByReturnNoAndCompany(
                rowData.getInvoiceNo(),
                company
        );

        SaleReturn existingSaleReturn = saleReturnOpt.orElse(null);

        SaleReturn saleReturn = saleReturnEntityMapper.toEntity(existingSaleReturn,rowData,company,party);

        boolean isNew = (existingSaleReturn == null);

        saleReturn = saleReturnRepository.save(saleReturn);

        if (isNew) {
            partyLedgerService.addLedgerEntry(
                    saleReturn.getParty(),
                    saleReturn.getCompany(),
                    saleReturn.getReturnDate(),
                    PartyTransactionType.SALE_RETURN,
                    saleReturn.getSaleReturnId(),
                    saleReturn.getReturnNo(),
                    0.0,
                    saleReturn.getTotalAmount(),
                    saleReturn.getBalanceAmount(),
                    saleReturn.getDescription()
            );

            // add party activity entry in the database
            partyActivityService.addActivity(
                    saleReturn.getParty(),
                    saleReturn.getCompany(),
                    saleReturn.getSaleReturnId(),
                    saleReturn.getReturnNo(),
                    saleReturn.getReturnDate(),
                    PartyTransactionType.SALE_RETURN,
                    saleReturn.getTotalAmount(),
                    saleReturn.getBalanceAmount(),
                    true,
                    saleReturn.getDescription());

        } else {
            partyLedgerService.updatePartyLedger(
                    saleReturn.getParty(),
                    saleReturn.getCompany(),
                    saleReturn.getReturnDate(),
                    PartyTransactionType.SALE_RETURN,
                    saleReturn.getSaleReturnId(),
                    saleReturn.getReturnNo(),
                    0.0,
                    saleReturn.getTotalAmount(),
                    saleReturn.getBalanceAmount(),
                    saleReturn.getDescription()
            );

            // update party activity entry in the database
            partyActivityService.updatePartyActivity(
                    saleReturn.getParty(),
                    saleReturn.getCompany(),
                    saleReturn.getSaleReturnId(),
                    saleReturn.getReturnNo(),
                    saleReturn.getReturnDate(),
                    PartyTransactionType.SALE_RETURN,
                    saleReturn.getTotalAmount(),
                    saleReturn.getBalanceAmount(),
                    true,
                    saleReturn.getDescription());
        }
    }
}
