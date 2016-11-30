package com.example.sherrychuang.splitsmart.Activity;

import java.io.Serializable;

/**
 * Created by jenny on 11/29/16.
 * For Item in the Bill Content Page.
 * Description: This ItemInput class stores temporary item information which is parsed to
 * texts from the image, and will be used in the BillContentPage.
 */

public class ItemInput implements Serializable {

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