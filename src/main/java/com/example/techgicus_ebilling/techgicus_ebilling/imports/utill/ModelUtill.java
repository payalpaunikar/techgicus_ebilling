package com.example.techgicus_ebilling.techgicus_ebilling.imports.utill;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ModelUtill {

    private final PartyRepository partyRepository;

    @Autowired
    public ModelUtill(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    public  Party findOrCreateParty(String name, String phone, Company company) {
        if (name == null || name.trim().isEmpty()) return null;

        Optional<Party> existing = partyRepository.findByNameAndPhoneNo(
                name.trim(),
                phone != null ? phone.trim() : null
        );

        if (existing.isPresent()) {
            return existing.get();
        }

        // create new
        Party party = new Party();
        party.setName(name.trim());
        party.setCompany(company);
        if (phone != null) party.setPhoneNo(phone.trim());
        return partyRepository.save(party);
    }

    public  Party findOrCreateParty(String name,Company company) {
        if (name == null || name.trim().isEmpty()) return null;

        Optional<Party> existing = partyRepository.findByName(
                name.trim()
                // phone != null ? phone.trim() : null
        );

        if (existing.isPresent()) {
            return existing.get();
        }

        // create new
        Party party = new Party();
        party.setName(name.trim());
        party.setCompany(company);
        // if (phone != null) party.setPhoneNo(phone.trim());
        return partyRepository.save(party);
    }
}
