package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Expense;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.expensesCategoryDto.ExpensesCategoryWithExpenseAmountResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense,Long> {

    @Query("""
    SELECT new com.example.techgicus_ebilling.techgicus_ebilling.dto.expensesCategoryDto.ExpensesCategoryWithExpenseAmountResponse(
        ec.expensesCategoryId,
        ec.categoryName,
        SUM(CASE WHEN e.totalAmount != 0 THEN e.totalAmount ELSE 0 END)
    )
    FROM Expense e
    RIGHT JOIN e.expensesCategory ec
    WHERE ec.company.companyId = :companyId
    GROUP BY ec.expensesCategoryId, ec.categoryName
""")
    List<ExpensesCategoryWithExpenseAmountResponse> findAllExpensesCategoryWithExpenseAmount(@Param("companyId") Long companyId);


    @Query("""
    SELECT new com.example.techgicus_ebilling.techgicus_ebilling.dto.expensesCategoryDto.ExpensesCategoryWithExpenseAmountResponse(
        ec.expensesCategoryId,
        ec.categoryName,
        SUM(CASE WHEN e.totalAmount != 0 THEN e.totalAmount ELSE 0 END)
    )
    FROM Expense e
    RIGHT JOIN e.expensesCategory ec
    On e.expenseDate between :startDate And :endDate
    WHERE ec.company.companyId = :companyId
    GROUP BY ec.expensesCategoryId, ec.categoryName
""")
    List<ExpensesCategoryWithExpenseAmountResponse> findAllExpensesCategoryWithExpenseAmountByDate(@Param("companyId") Long companyId, @Param("startDate")LocalDate startDate,
                                                                                                   @Param("endDate")LocalDate endDate);


}
