package com.example.techgicus_ebilling.techgicus_ebilling.imports.context;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Purchase;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseReturn;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Sale;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleReturn;

import java.util.*;

public class ImportContext {

    private final List<ImportError> errors = new ArrayList<>();
    private final Map<String, Sale> sales = new HashMap<>();
    private final Map<String, SaleReturn> saleReturns = new HashMap<>();

    private final Set<Long> initializedSales = new HashSet<>();
    private final Set<Long> initializedSaleReturns = new HashSet<>();


    private final Map<String, Purchase> purchases = new HashMap<>();
    private final Map<String, PurchaseReturn> purchaseReturns = new HashMap<>();
    private final Map<Long, Map<Long, Double>> purchaseOldQtyMap = new HashMap<>();


    private final Set<Long> initializedPurchases = new HashSet<>();
    private final Set<Long> initializedPurchaseReturns = new HashSet<>();

    public boolean isInitialized(Sale sale) {
        return initializedSales.contains(sale.getSaleId());
    }

    public void markInitialized(Sale sale) {
        initializedSales.add(sale.getSaleId());
    }

    public boolean isInitialized(SaleReturn saleReturn) {
        return initializedSaleReturns.contains(saleReturn.getSaleReturnId());
    }

    public void markInitialized(SaleReturn saleReturn) {
        initializedSaleReturns.add(saleReturn.getSaleReturnId());
    }

    public void addError(String sheetName,int row, String message) {
        errors.add(new ImportError(sheetName,row, message));
    }

    public void registerSale(Sale sale) {
        sales.put(sale.getSaleId().toString(), sale);
    }

    public Collection<Sale> getSales() {
        return sales.values();
    }

    public void registerSaleReturn(SaleReturn saleReturn) {
        saleReturns.put(saleReturn.getSaleReturnId().toString(), saleReturn);
    }

    public Collection<SaleReturn> getSaleReturns() {
        return saleReturns.values();
    }


    public boolean isInitialized(Purchase purchase) {
        return initializedPurchases.contains(purchase.getPurchaseId());
    }

    public void markInitialized(Purchase purchase) {
        initializedPurchases.add(purchase.getPurchaseId());
    }

    public boolean isInitialized(PurchaseReturn purchaseReturn) {
        return initializedPurchaseReturns.contains(purchaseReturn.getPurchaseReturnId());
    }

    public void markInitialized(PurchaseReturn purchaseReturn) {
        initializedPurchaseReturns.add(purchaseReturn.getPurchaseReturnId());
    }

    public void registerPurchase(Purchase purchase) {

        purchases.put(purchase.getPurchaseId().toString(), purchase);
    }

    public Collection<Purchase> getPurchases() {
        return purchases.values();
    }

    public void registerPurchaseReturn(PurchaseReturn purchaseReturn) {
//        if (purchaseReturn.getPurchaseReturnId() == null) {
//            errors.add(new ImportError(
//                    -1,
//                    "Internal Error: Purchase Return was not saved correctly (ID is null)"
//            ));
//            return;
//        }

        purchaseReturns.put(purchaseReturn.getPurchaseReturnId().toString(), purchaseReturn);
    }

    public Collection<PurchaseReturn> getPurchaseReturns() {
        return purchaseReturns.values();
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<ImportError> getErrors() {
        return errors;
    }

    public void storePurchaseOldQty(Long purchaseId, Map<Long, Double> qtyMap) {
        purchaseOldQtyMap.put(purchaseId, qtyMap);
    }

    public Map<Long, Double> getOldQtyForPurchase(Long purchaseId) {
        return purchaseOldQtyMap.getOrDefault(purchaseId, new HashMap<>());
    }

    public Set<Long> getInitializedPurchases() {
        return initializedPurchases;
    }


}


