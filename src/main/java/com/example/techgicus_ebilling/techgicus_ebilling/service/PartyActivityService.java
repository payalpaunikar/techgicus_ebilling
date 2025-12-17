package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PartyActivity;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PartyTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CompanyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PartyActivityRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PartyActivityService {

    private final PartyActivityRepository repo;
    private final PartyRepository partyRepository;
    private final CompanyRepository companyRepository;

    public PartyActivityService(PartyActivityRepository repo, PartyRepository partyRepository, CompanyRepository companyRepository) {
        this.repo = repo;
        this.partyRepository = partyRepository;
        this.companyRepository = companyRepository;
    }

    public void addActivity(
            Party party,
            Company company,
            Long referenceId,
            String referenceNo,
            LocalDate date,
            PartyTransactionType type,
            Double amount,
            Double runningBalance,
            boolean isFinancial,
            String note
    ) {

        PartyActivity activity =  new PartyActivity();

        activity.setParty(party);
        activity.setCompany(company);
        activity.setReferenceId(referenceId);
        activity.setReferenceNumber(referenceNo);
        activity.setActivityDate(date);
        activity.setType(type);
        activity.setAmount(amount);
        activity.setRunningBalance(runningBalance);
        activity.setFinancial(isFinancial);
        activity.setNote(note);

        repo.save(activity);
    }


    public void deletePartyActivity(PartyTransactionType type,Long referenceId){
        PartyActivity partyActivity = repo.findByTypeAndReferenceId(type,referenceId)
                .orElseThrow(()-> new ResourceNotFoundException("Party active not found "));

        repo.delete(partyActivity);

    }


    public void updatePartyActivity( Party party,
                                     Company company,
                                     Long referenceId,
                                     String referenceNo,
                                     LocalDate date,
                                     PartyTransactionType type,
                                     Double amount,
                                     Double runningBalance,
                                     boolean isFinancial,
                                     String note){

        PartyActivity activity = repo.findByTypeAndReferenceId(type,referenceId)
                .orElseThrow(()-> new ResourceNotFoundException("Party Activity not found."));

        activity.setParty(party);
        activity.setCompany(company);
        activity.setReferenceId(referenceId);
        activity.setReferenceNumber(referenceNo);
        activity.setActivityDate(date);
        activity.setType(type);
        activity.setAmount(amount);
        activity.setRunningBalance(runningBalance);
        activity.setFinancial(isFinancial);
        activity.setNote(note);

        repo.save(activity);

    }


}
