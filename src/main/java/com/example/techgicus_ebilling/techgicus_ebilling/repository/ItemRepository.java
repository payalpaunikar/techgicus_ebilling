package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Item;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto.ItemSaleSummaryInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {

    List<Item> findAllByCompany(Company company);

    Optional<Item> findByItemNameAndCompany(String name, Company company);


    @Query(value = """
            SELECT i.item_id AS itemId,
            i.item_name AS itemName,
            SUM(CASE WHEN s.invoce_date 
            BETWEEN  CONCAT(YEAR(CURDATE()), '-01-01') 
                    AND CONCAT(YEAR(CURDATE()), '-12-31') 
            THEN si.quantity ELSE 0 END) AS TotalSaleCount
            FROM item i
            LEFT JOIN sale_item si
            ON i.item_id = si.item_id
            LEFT JOIN sale s
            ON s.sale_id = si.sale_id
            WHERE i.company_id = :companyId
            GROUP BY i.item_id,i.item_name;
            """,nativeQuery = true)
    List<ItemSaleSummaryInterface> findAllIteSaleSummary(@Param("companyId") Long companyId);

}
