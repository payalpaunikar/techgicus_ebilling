package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration;

public enum DocumentType {
    SALE("KE", "Invoice Number"),
    SALE_ORDER("SO", "Sale Order Number"),
    SALE_RETURN("SR", "Sale Return Number"),
    PURCHASE("PB", "Bill Number"),
    PURCHASE_ORDER("PO", "Purchase Order Number"),
    PURCHASE_RETURN("PR", "Purchase Return Number"),
    DELIVERY("DC", "Delivery Challan Number"),
    PAYMENT_IN("REC", "Receipt Number"),
    PAYMENT_OUT("PAY", "Payment Number"),
    QUOTATION("QTN", "Quotation Number");

    private final String prefix;
    private final String displayName;

    DocumentType(String prefix, String displayName) {
        this.prefix = prefix;
        this.displayName = displayName;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getDisplayName() {
        return displayName;
    }
}

