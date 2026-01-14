package com.example.techgicus_ebilling.techgicus_ebilling.imports.processor;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Sale;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.SaleRowData;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.extractor.SaleRowExtractor;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper.SaleEntityMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ModelUtill;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.SaleRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.service.PartyService;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

@Service
public class SaleTransactionProcessor implements TransactionProcessor {

    private final SaleRowExtractor saleRowExtractor;
    private final SaleEntityMapper saleEntityMapper;
    private final SaleRepository saleRepository;
    private final ModelUtill modelUtill;

    public SaleTransactionProcessor(SaleRowExtractor saleRowExtractor, SaleEntityMapper saleEntityMapper, SaleRepository saleRepository, ModelUtill modelUtill) {
        this.saleRowExtractor = saleRowExtractor;
        this.saleEntityMapper = saleEntityMapper;
        this.saleRepository = saleRepository;
        this.modelUtill = modelUtill;
    }

    @Override
    public boolean supports(String transactionType) {
        if (transactionType.toLowerCase().trim().equals("sale"))return true;
        return false;
    }

    @Override
    public void process(Row row, Company company) {

        SaleRowData rowData = saleRowExtractor.extract(row);

        Party party = modelUtill.findOrCreateParty(
                rowData.getPartyName(),
                rowData.getPartyPhone(),
                company
        );

        Sale sale = saleEntityMapper.toEntity(rowData,company,party);
        saleRepository.save(sale);

    }
}
