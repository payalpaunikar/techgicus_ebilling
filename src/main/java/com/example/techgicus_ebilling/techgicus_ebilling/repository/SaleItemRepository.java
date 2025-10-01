package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem,Long> {
}
