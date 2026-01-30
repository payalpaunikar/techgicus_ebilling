package com.example.techgicus_ebilling.techgicus_ebilling.imports.processor;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Sale;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PartyTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportContext;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.SaleRowData;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.extractor.SaleRowExtractor;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper.SaleEntityMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ModelUtill;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.SaleRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.service.PartyActivityService;
import com.example.techgicus_ebilling.techgicus_ebilling.service.PartyLedgerService;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SaleTransactionProcessor implements TransactionProcessor {

    private final SaleRowExtractor saleRowExtractor;
    private final SaleEntityMapper saleEntityMapper;
    private final SaleRepository saleRepository;
    private final ModelUtill modelUtill;
    private final PartyActivityService partyActivityService;
    private final PartyLedgerService partyLedgerService;


    private final static Logger log = LoggerFactory.getLogger(SaleTransactionProcessor.class);

    public SaleTransactionProcessor(SaleRowExtractor saleRowExtractor, SaleEntityMapper saleEntityMapper, SaleRepository saleRepository, ModelUtill modelUtill, PartyActivityService partyActivityService, PartyLedgerService partyLedgerService) {
        this.saleRowExtractor = saleRowExtractor;
        this.saleEntityMapper = saleEntityMapper;
        this.saleRepository = saleRepository;
        this.modelUtill = modelUtill;
        this.partyActivityService = partyActivityService;
        this.partyLedgerService = partyLedgerService;
    }

    @Override
    public boolean supports(String transactionType) {
        log.info("transaction type is : "+transactionType);

        if (transactionType.toLowerCase().trim().equals("sale"))return true;
        return false;
    }

    @Transactional
    @Override
    public void process(Row row, Company company, ImportContext context) {

        log.info("row number : "+row.getRowNum());

        SaleRowData rowData = saleRowExtractor.extract(row);

        Party party = modelUtill.findOrCreateParty(
                rowData.getPartyName(),
                rowData.getPartyPhone(),
                company
        );

        Optional<Sale> existingSaleOpt = saleRepository.findByInvoiceNumberAndCompany(
                rowData.getInvoiceNo(),
                company
        );


        Sale existingSale = existingSaleOpt.orElse(null);

        Sale sale = saleEntityMapper.toEntity(existingSale,rowData,company,party);

        boolean isNew = (existingSale == null);

       sale = saleRepository.save(sale);

       context.registerSale(sale);

        if (isNew) {
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
                    sale.getPaymentDescription()
            );

        } else {
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
                    sale.getPaymentDescription()
            );
        }

    }
}
