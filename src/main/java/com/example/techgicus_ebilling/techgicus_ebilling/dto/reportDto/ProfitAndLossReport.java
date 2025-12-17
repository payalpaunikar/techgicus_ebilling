package com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto;

public class ProfitAndLossReport {

    // Sales Section
    private double sale;
    private double creditNote;
    private double saleFA;


    // Purchase Section
    private double purchase;
    private double debitNote;
    private double purchaseFA;


    // payment Section
    private double paymentOutDiscount;
    private double paymentInDiscount;

    // expenses Section
    private double otherDirectExpenses;

    // Tax Payable Section
    private double gstPayable;
    private double tcsPayable;
    private double tdsPayable;

    // Tax Receivable Section
    private double gstReceivable;
    private double tcsReceivable;
    private double tdsReceivable;

    // Stock Section
    private double openingStock;
    private double closingStock;
    private double openingStockFA;
    private double closingStockFA;


    private double grossProfitAndLoss;
    private double otherIncome;

    // indirect expenses
    private double otherExpense;
    private double loanInterestExpense;
    private double loanProcessingFeeExpense;
    private double loanChargeExpense;


    private double netProfitAndLoss;

    public double getSale() {
        return sale;
    }

    public void setSale(double sale) {
        this.sale = sale;
    }

    public double getCreditNote() {
        return creditNote;
    }

    public void setCreditNote(double creditNote) {
        this.creditNote = creditNote;
    }

    public double getSaleFA() {
        return saleFA;
    }

    public void setSaleFA(double saleFA) {
        this.saleFA = saleFA;
    }

    public double getPurchase() {
        return purchase;
    }

    public void setPurchase(double purchase) {
        this.purchase = purchase;
    }

    public double getDebitNote() {
        return debitNote;
    }

    public void setDebitNote(double debitNote) {
        this.debitNote = debitNote;
    }

    public double getPurchaseFA() {
        return purchaseFA;
    }

    public void setPurchaseFA(double purchaseFA) {
        this.purchaseFA = purchaseFA;
    }

    public double getPaymentOutDiscount() {
        return paymentOutDiscount;
    }

    public void setPaymentOutDiscount(double paymentOutDiscount) {
        this.paymentOutDiscount = paymentOutDiscount;
    }

    public double getPaymentInDiscount() {
        return paymentInDiscount;
    }

    public void setPaymentInDiscount(double paymentInDiscount) {
        this.paymentInDiscount = paymentInDiscount;
    }

    public double getOtherDirectExpenses() {
        return otherDirectExpenses;
    }

    public void setOtherDirectExpenses(double otherDirectExpenses) {
        this.otherDirectExpenses = otherDirectExpenses;
    }

    public double getGstPayable() {
        return gstPayable;
    }

    public void setGstPayable(double gstPayable) {
        this.gstPayable = gstPayable;
    }

    public double getTcsPayable() {
        return tcsPayable;
    }

    public void setTcsPayable(double tcsPayable) {
        this.tcsPayable = tcsPayable;
    }

    public double getTdsPayable() {
        return tdsPayable;
    }

    public void setTdsPayable(double tdsPayable) {
        this.tdsPayable = tdsPayable;
    }

    public double getGstReceivable() {
        return gstReceivable;
    }

    public void setGstReceivable(double gstReceivable) {
        this.gstReceivable = gstReceivable;
    }

    public double getTcsReceivable() {
        return tcsReceivable;
    }

    public void setTcsReceivable(double tcsReceivable) {
        this.tcsReceivable = tcsReceivable;
    }

    public double getTdsReceivable() {
        return tdsReceivable;
    }

    public void setTdsReceivable(double tdsReceivable) {
        this.tdsReceivable = tdsReceivable;
    }

    public double getOpeningStock() {
        return openingStock;
    }

    public void setOpeningStock(double openingStock) {
        this.openingStock = openingStock;
    }

    public double getClosingStock() {
        return closingStock;
    }

    public void setClosingStock(double closingStock) {
        this.closingStock = closingStock;
    }

    public double getOpeningStockFA() {
        return openingStockFA;
    }

    public void setOpeningStockFA(double openingStockFA) {
        this.openingStockFA = openingStockFA;
    }

    public double getClosingStockFA() {
        return closingStockFA;
    }

    public void setClosingStockFA(double closingStockFA) {
        this.closingStockFA = closingStockFA;
    }

    public double getGrossProfitAndLoss() {
        return grossProfitAndLoss;
    }

    public void setGrossProfitAndLoss(double grossProfitAndLoss) {
        this.grossProfitAndLoss = grossProfitAndLoss;
    }

    public double getOtherIncome() {
        return otherIncome;
    }

    public void setOtherIncome(double otherIncome) {
        this.otherIncome = otherIncome;
    }

    public double getOtherExpense() {
        return otherExpense;
    }

    public void setOtherExpense(double otherExpense) {
        this.otherExpense = otherExpense;
    }

    public double getLoanInterestExpense() {
        return loanInterestExpense;
    }

    public void setLoanInterestExpense(double loanInterestExpense) {
        this.loanInterestExpense = loanInterestExpense;
    }

    public double getLoanProcessingFeeExpense() {
        return loanProcessingFeeExpense;
    }

    public void setLoanProcessingFeeExpense(double loanProcessingFeeExpense) {
        this.loanProcessingFeeExpense = loanProcessingFeeExpense;
    }

    public double getLoanChargeExpense() {
        return loanChargeExpense;
    }

    public void setLoanChargeExpense(double loanChargeExpense) {
        this.loanChargeExpense = loanChargeExpense;
    }

    public double getNetProfitAndLoss() {
        return netProfitAndLoss;
    }

    public void setNetProfitAndLoss(double netProfitAndLoss) {
        this.netProfitAndLoss = netProfitAndLoss;
    }
}
