package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration;

public enum PaymentType {
    CASH,
    UPI,
    CREDIT_CARD,
    DEBIT_CARD,
    NET_BANKING,
    WALLET,
    CHEQUE,
    OTHER;

    // This method converts a string from Excel to the correct enum value
    public static PaymentType fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            return OTHER;  // default if empty
        }

        String cleaned = text.trim().toUpperCase().replace(" ", "_");

        try {
            return PaymentType.valueOf(cleaned);
        } catch (IllegalArgumentException e) {
            // If exact match fails, try common variations
            if (cleaned.contains("CASH")) return CASH;
            if (cleaned.contains("UPI")) return UPI;
            if (cleaned.contains("CARD")) return CREDIT_CARD;
            if (cleaned.contains("BANKING")) return NET_BANKING;
            if (cleaned.contains("WALLET")) return WALLET;
            if (cleaned.contains("CHEQUE") || cleaned.contains("CHECK")) return CHEQUE;

            return OTHER;  // fallback
        }
    }
}
