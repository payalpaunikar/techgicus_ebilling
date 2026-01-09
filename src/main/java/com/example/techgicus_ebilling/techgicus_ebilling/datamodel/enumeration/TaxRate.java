package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration;

public enum TaxRate {
    NONE(0.0),
    EXEMPTED(0.0),
    GST0(0.0),
    IGST0(0.0),
    GST0POINT25(0.25),
    IGST0POINT25(0.25),
    GST3(3.0),
    IGST3(3.0),
    GST5(5.0),
    IGST5(5.0),
    GST12(12.0),
    IGST12(12.0),
    GST18(18.0),
    IGST18(18.0),
    GST28(28.0),
    IGST28(28.0);

    private final double rate;

    // Constructor
    TaxRate(double rate) {
        this.rate = rate;
    }

    // Getter if you need it later
    public double getRate() {
        return rate;
    }

    // The method you're missing
    public static TaxRate fromValue(double value) {
        // Round to avoid floating-point precision issues
        double rounded = Math.round(value * 100.0) / 100.0;

        for (TaxRate tax : values()) {
            if (Math.abs(tax.rate - rounded) < 0.001) {  // small tolerance
                return tax;
            }
        }

        // Fallback – could also throw an exception
        // throw new IllegalArgumentException("Unknown tax rate: " + value);
        return NONE;  // or EXEMPTED – choose your default
    }

    // ✅ for response display
    public String getRateWithPercent() {
        return rate + "%";
    }
}