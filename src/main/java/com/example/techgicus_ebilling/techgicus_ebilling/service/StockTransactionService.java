package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Item;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseItem;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.StockTransaction;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.StockTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.StockTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StockTransactionService {

      private StockTransactionRepository stockTransactionRepository;

    @Autowired
    public StockTransactionService(StockTransactionRepository stockTransactionRepository) {
        this.stockTransactionRepository = stockTransactionRepository;
    }


    public StockTransaction addStockTransaction(Item item, StockTransactionType transactionType, Double totalAmount, LocalDate transactionDate,String referenceNumber){
        StockTransaction stockTransaction = new StockTransaction();
        stockTransaction.setItem(item);
        stockTransaction.setTransactionType(transactionType);
        stockTransaction.setTotalAmount(totalAmount);
        stockTransaction.setTransactionDate(transactionDate);
        stockTransaction.setReferenceNumber(referenceNumber);

        return stockTransaction;
    }


}
