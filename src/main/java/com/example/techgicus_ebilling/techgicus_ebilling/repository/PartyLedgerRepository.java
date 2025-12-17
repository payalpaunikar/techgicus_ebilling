package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PartyLedger;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PartyTransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PartyLedgerRepository extends JpaRepository<PartyLedger,Long> {

    Page<PartyLedger> findByPartyPartyIdOrderByDateDesc(Long partyId, Pageable pageable);

    List<PartyLedger> findByPartyPartyIdOrderByDateAsc(Long partyId); // for running balance

    Optional<PartyLedger> findByTypeAndReferenceId(PartyTransactionType type,Long referenceId);

    List<PartyLedger> findByPartyPartyIdAndDateBetweenOrderByDateAsc(
            Long partyId,
            LocalDate startDate,
            LocalDate endDate
    );



}
