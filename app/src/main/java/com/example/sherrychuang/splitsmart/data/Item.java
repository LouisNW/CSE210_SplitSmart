package com.example.sherrychuang.splitsmart.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sherrychuang on 11/15/16.
 */

public class Item implements Serializable {
    private long id;
    private String name;
    private double price;
    private boolean isTaxItem;
    private long billID;

    public Item(String name, double price, boolean isTaxItem, long billID) {
        this.name = name;
        this.price = price;
        this.isTaxItem = isTaxItem;
        this.billID = billID;
    }

    public Item() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isTaxItem() {
        return isTaxItem;
    }

    public void setTaxItem(boolean taxItem) {
        isTaxItem = taxItem;
    }

    public long getBillID() {
        return billID;
    }

    public void setBillID(long billID) {
        this.billID = billID;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", isTaxItem=" + isTaxItem +
                ", billID=" + billID +
                '}';
    }
}
