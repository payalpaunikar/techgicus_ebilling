package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PartyActivity;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PartyTransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartyActivityRepository extends JpaRepository<PartyActivity,Long> {
    Page<PartyActivity> findByPartyPartyIdOrderByActivityDateDesc(Long partyId, Pageable pageable);
    List<PartyActivity> findByPartyPartyIdAndTypeOrderByActivityDateDesc(Long partyId, PartyTransactionType type);
    Optional<PartyActivity> findByTypeAndReferenceId(PartyTransactionType type, Long referenceId);
}
