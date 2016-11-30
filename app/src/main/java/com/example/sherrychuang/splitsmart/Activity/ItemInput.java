package com.example.sherrychuang.splitsmart.Activity;

/**
 * Created by jenny on 11/29/16.
 * For Item in the Bill Content Page.
 */

public class ItemInput {

    private boolean taxSelect;
    private String itemName;
    private String itemPrice;

    public ItemInput(boolean taxSelect, String itemName, String itemPrice) {
        this.taxSelect = taxSelect;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public boolean getTax() {
        return taxSelect;
    }

    public void setTax(boolean tax) {
        this.taxSelect = tax;
    }

    public String getItemName() { return itemName; }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrice() {
        return itemPrice;
    }

    public void setPrice(String price) {
        this.itemPrice = price;
    }
}