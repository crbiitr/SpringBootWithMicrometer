package com.integration.micrometer.model;

/**
 * @author Chetan Raj
 * @implNote
 * @since : 2019-09-28
 */
public class Order {
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private int amount;
    private String type;

    public Order(int amount, String type) {
        this.amount = amount;
        this.type = type;
    }
}
