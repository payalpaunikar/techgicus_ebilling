package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PartyLedger;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PartyTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CompanyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PartyLedgerRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PartyLedgerService {

    private final PartyLedgerRepository ledgerRepo;
    private final PartyRepository partyRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public PartyLedgerService(PartyLedgerRepository ledgerRepo, PartyRepository partyRepository, CompanyRepository companyRepository) {
        this.ledgerRepo = ledgerRepo;
        this.partyRepository = partyRepository;
        this.companyRepository = companyRepository;
    }

    public void addLedgerEntry(
            Party party,
            Company company,
            LocalDate date,
            PartyTransactionType type,
            Long referenceId,
            String referenceNo,
            Double debit,
            Double credit,
            Double runningBalance,
            String description
    ) {

//        // Get previous balance
//        List<PartyLedger> all = ledgerRepo.findByPartyPartyIdOrderByDateAsc(party.getPartyId());


        PartyLedger ledger = new PartyLedger();
        ledger.setParty(party);
        ledger.setCompany(company);
        ledger.setDate(date);
        ledger.setType(type);
        ledger.setReferenceId(referenceId);
        ledger.setDebitAmount(debit);
        ledger.setCreditAmount(credit);
        ledger.setReferenceNo(referenceNo);
        ledger.setDescription(description);
        ledger.setRunningBalance(runningBalance);
        ledger.setCreatedAt(LocalDateTime.now());

        ledgerRepo.save(ledger);
    }


    public void deletePartyLedger(PartyTransactionType type,Long referenceId){
        PartyLedger partyLedger = ledgerRepo.findByTypeAndReferenceId(type,referenceId)
                .orElseThrow(()-> new ResourceNotFoundException("Party leader not found"));

        ledgerRepo.delete(partyLedger);

    }

    public void updatePartyLedger(Party party,
                                  Company company,
                                  LocalDate date,
                                  PartyTransactionType type,
                                  Long referenceId,
                                  String referenceNo,
                                  Double debit,
                                  Double credit,
                                  Double runningBalance,
                                  String description){


        PartyLedger ledger = ledgerRepo.findByTypeAndReferenceId(type,referenceId)
                .orElseThrow(()-> new ResourceNotFoundException("Party Ledger not found"));

        ledger.setParty(party);
        ledger.setCompany(company);
        ledger.setDate(date);
        ledger.setType(type);
        ledger.setReferenceId(referenceId);
        ledger.setDebitAmount(debit);
        ledger.setCreditAmount(credit);
        ledger.setReferenceNo(referenceNo);
        ledger.setDescription(description);
        ledger.setRunningBalance(runningBalance);

      ledgerRepo.save(ledger);
    }


}
