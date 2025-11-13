package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.AddExpenseItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.expenseItemDto.ExpenseItemWithExpenseAmountResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AddExpenseItemRepository extends JpaRepository<AddExpenseItem,Long> {

    @Query("""
            SELECT new com.example.techgicus_ebilling.techgicus_ebilling.dto.expenseItemDto.ExpenseItemWithExpenseAmountResponse
            ( ei.expenseItemId,
            ei.name,
            SUM(CASE WHEN aei.totalAmount !=0 THEN aei.totalAmount ELSE 0 END)
            )
            FROM AddExpenseItem aei RIGHT JOIN  aei.expenseItem ei 
            WHERE ei.company.companyId = :companyId
            GROUP BY ei.expenseItemId,ei.name
            """)
    List<ExpenseItemWithExpenseAmountResponse> findAllExpenseItemWithTotalExpenseAmount(@Param("companyId")Long companyId);


    @Query("""
    SELECT new com.example.techgicus_ebilling.techgicus_ebilling.dto.expenseItemDto.ExpenseItemWithExpenseAmountResponse(
        ei.expenseItemId,
        ei.name,
        SUM(
            CASE 
                WHEN e.expenseDate BETWEEN :startDate AND :endDate 
                THEN aei.totalAmount 
                ELSE 0 
            END
        )
    )
    FROM AddExpenseItem aei
    RIGHT JOIN aei.expenseItem ei
    LEFT JOIN aei.expense e
    WHERE ei.company.companyId = :companyId
    GROUP BY ei.expenseItemId, ei.name
    ORDER BY ei.name
    """)
    List<ExpenseItemWithExpenseAmountResponse> findExpenseItemSummaryByCompanyAndDateRange(
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
