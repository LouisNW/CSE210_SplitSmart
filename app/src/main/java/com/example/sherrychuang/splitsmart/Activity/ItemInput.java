package com.example.sherrychuang.splitsmart.Activity;

import com.cunoraz.tagview.Tag;

import java.io.Serializable;
import java.util.List;

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
    private List<Tag> selectedPeople;

    public ItemInput(boolean taxSelect, String itemName, String itemPrice, List<Tag> selectedPeople) {
        this.taxSelect = taxSelect;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.selectedPeople = selectedPeople;
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

    public List<Tag> getSelectedPeople() { return selectedPeople; }

    public void setSelectedPeople(Tag tempPeople) { selectedPeople.add(tempPeople); }
}