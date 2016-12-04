package com.example.sherrychuang.splitsmart.model;

import android.content.Context;

import com.example.sherrychuang.splitsmart.manager.*;
import com.example.sherrychuang.splitsmart.data.*;

import java.util.List;

/**
 * Created by Chihhung on 12/3/16.
 */

public class CreateSampleDB {
    private PersonManager personManager;
    private BillManager billManager;
    private EventManager eventManager;

    public CreateSampleDB(Context context) {
        personManager = ManagerFactory.getPersonManager(context);
        billManager = ManagerFactory.getBillManager(context);
        eventManager = ManagerFactory.getEventManager(context);
    }
    public void Run() {
        eventManager.insertEvent(new Event("event A", new EventDate(1,1), new EventDate(11,22)));
        eventManager.insertEvent(new Event("event B", new EventDate(2,21), new EventDate(12,20)));
        eventManager.insertEvent(new Event("event c", new EventDate(3,21), new EventDate(11,22)));
        eventManager.insertEvent(new Event("event D", new EventDate(4,21), new EventDate(12,20)));
        eventManager.insertEvent(new Event("event E", new EventDate(5,21), new EventDate(11,22)));

        List<Event> events = eventManager.getAllEvents();
        for (int i = 0; i < events.size(); i++) {
            Person Kobe = new Person("Kobe","abc@gmail.com", events.get(i).getId());
            Person Lebron = new Person("Lebron","abc@gmail.com", events.get(i).getId());
            Person KD = new Person("KD","abc@gmail.com", events.get(i).getId());
            Person Curry = new Person("Curry","abc@gmail.com", events.get(i).getId());
            personManager.insertPerson(Kobe);
            personManager.insertPerson(KD);
            personManager.insertPerson(Lebron);
            personManager.insertPerson(Curry);
            billManager.insertBill(new Bill("bill 1", Kobe.getId(), 8, events.get(i).getId()));
            billManager.insertBill(new Bill("bill 2", Curry.getId(), 5, events.get(i).getId()));
            billManager.insertBill(new Bill("bill 3", Kobe.getId(), 10, events.get(i).getId()));
            billManager.insertBill(new Bill("bill 4", KD.getId(), 6, events.get(i).getId()));
            billManager.insertBill(new Bill("bill 5", Kobe.getId(), 7, events.get(i).getId()));
            billManager.insertBill(new Bill("bill 6", Lebron.getId(), 7, events.get(i).getId()));
            billManager.insertBill(new Bill("bill 7", Lebron.getId(), 8, events.get(i).getId()));
        }


    }
//    public void EraseAll() {
//
//    }
}
