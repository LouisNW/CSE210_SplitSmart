package com.example.sherrychuang.splitsmart;

import android.content.Context;

import com.example.sherrychuang.splitsmart.data.Bill;
import com.example.sherrychuang.splitsmart.data.Item;
import com.example.sherrychuang.splitsmart.data.Person;
import com.example.sherrychuang.splitsmart.manager.BillManager;
import com.example.sherrychuang.splitsmart.manager.ItemManager;
import com.example.sherrychuang.splitsmart.manager.ManagerFactory;
import com.example.sherrychuang.splitsmart.manager.PersonManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Created by sherrychuang on 11/15/16.
 */

public class Calculator {
    private Context context;
    private List<Bill> bills;
    private List<Person> peoplelist;
    private Map<Long, Double> money;
    private Map<Person, Double> payer;
    private Map<Person, Double> payee;
    private List<String> result_1;
    private List<String> result_2;

    public Calculator(Context context, long eventID) {
        this.context = context;
        BillManager billManager = ManagerFactory.getBillManager(context);
        PersonManager personManager = ManagerFactory.getPersonManager(context);
        bills = billManager.getAllBillsOfEvent(eventID);
        peoplelist = personManager.getAllPersonsOfEvent(eventID);
        calculate();
        construct_reuslt();
    }
    private void calculate() {
        money = new HashMap<Long, Double>();
        for(int i = 0; i < peoplelist.size(); i++) {
            money.put(peoplelist.get(i).getId(), 0.0);
        }
        ItemManager itemManager = ManagerFactory.getItemManager(context);
        for(int i = 0; i < bills.size(); i++) {
            List<Item> items = itemManager.getAllItemsOfBill(bills.get(i).getId());
            long ownerId = bills.get(i).getOwnerID();
            double taxRate = bills.get(i).getTaxRate();
            for(int j = 0; j < items.size(); j++) {
                List<Person> people = itemManager.getSharingPersonsOfAnItem(items.get(j).getId());
                //System.err.println("Louis: people size is " + people.size());
                double price = items.get(j).getPrice();
                if(items.get(j).isTaxItem()) {price *= (1+taxRate/100);} //bug
                money.put(ownerId, money.get(ownerId) + price);
                for(int k = 0; k < people.size(); k++) {
                    money.put(people.get(k).getId(), money.get(people.get(k).getId()) - price / people.size());
                }
            }
        }
    }
    private void construct_reuslt() {
        result_1 = new ArrayList<>();
        payee = new HashMap<Person, Double>();
        payer = new HashMap<Person, Double>();
        for(int i = 0; i < peoplelist.size(); i++) {
            String str = peoplelist.get(i).getName();
            double price = (double) Math.round(money.get(peoplelist.get(i).getId())*100)/100;
            if(price >= 0) {
                str += " should be paid " + Double.toString(price) + " dollars.";
                payee.put(peoplelist.get(i), price);
            }
            else {
                str += " should pay " + Double.toString(-price) + " dollars.";
                payer.put(peoplelist.get(i), -price);
            }
            result_1.add(str);
        }
        constuct_result_helper();
    }
    private void constuct_result_helper() {
        result_2 = new ArrayList<>();
        List<Person> payeelist = new ArrayList<Person>();
        for(Person key : payee.keySet()) {
            payeelist.add(key);
        }
        List<Person> payerlist = new ArrayList<Person>();
        for(Person key : payer.keySet()) {
            payerlist.add(key);
        }
        while( !payerlist.isEmpty() ) {
            double pay = payer.get(payerlist.get(0));
            double bepaid = payee.get(payeelist.get(0));
            double price = Math.min(pay, bepaid);
            payer.put(payerlist.get(0), (double) Math.round((payer.get(payerlist.get(0)) - price)*100)/100.0);
            payee.put(payeelist.get(0), (double) Math.round((payee.get(payeelist.get(0)) - price)*100)/100.0);
            if(price > 0) {
                String str = payerlist.get(0).getName() + " should pay " + payeelist.get(0).getName() + " " + price + " dollars.";
                result_2.add(str);
            }
            if(price == pay) {payerlist.remove(0);}
            if(price == bepaid) {payeelist.remove(0);}
        }
    }
    public List<String> getResult_pay() {
        return this.result_1;
    }
    public List<String> getResult_final() {
        return this.result_2;
    }

}

