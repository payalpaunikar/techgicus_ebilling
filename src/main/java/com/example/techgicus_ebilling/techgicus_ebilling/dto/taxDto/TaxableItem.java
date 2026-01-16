package com.example.techgicus_ebilling.techgicus_ebilling.dto.taxDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxRate;

public interface TaxableItem {
   // String getItemName();
   // String getItemHsnCode();
    Double getTotalAmount();
    Double getTaxAmount();
    TaxRate getTaxRate();
}
