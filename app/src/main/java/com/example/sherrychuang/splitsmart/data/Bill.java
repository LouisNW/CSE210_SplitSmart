package com.example.sherrychuang.splitsmart.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sherrychuang on 11/15/16.
 */

public class Bill implements Serializable {
    private long id;
    private String name;
    private long ownerID;
    private double taxRate;
    private long eventID;

    public Bill(String name, long ownerID, double taxRate, long eventID) {
        this.name = name;
        this.ownerID = ownerID;
        this.taxRate = taxRate;
        this.eventID = eventID;
    }

    public Bill() {
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

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ownerID=" + ownerID +
                ", taxRate=" + taxRate +
                ", eventID=" + eventID +
                '}';
    }
}
