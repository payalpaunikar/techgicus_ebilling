package com.example.techgicus_ebilling.techgicus_ebilling.dto.addExpenseItem;

public class AddExpenseItemRequest {
    private Long expenseItemId;
    private String itemName;

    private Integer quantity;

    private Double perItemRate;

    private Double totalAmount;


    public Long getExpenseItemId() {
        return expenseItemId;
    }

    public void setExpenseItemId(Long expenseItemId) {
        this.expenseItemId = expenseItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPerItemRate() {
        return perItemRate;
    }

    public void setPerItemRate(Double perItemRate) {
        this.perItemRate = perItemRate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
