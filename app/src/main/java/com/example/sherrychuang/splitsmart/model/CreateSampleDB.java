package com.example.sherrychuang.splitsmart.model;

import android.content.Context;

import com.example.sherrychuang.splitsmart.manager.*;
import com.example.sherrychuang.splitsmart.data.*;

import java.util.ArrayList;
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
            List<Person> p_list = new ArrayList<>();
            p_list.add(Kobe);
            p_list.add(Lebron);
            p_list.add(KD);
            p_list.add(Curry);
            Bill b1 = new Bill("bill 1", Kobe.getId(), 8, events.get(i).getId());
            Bill b2 = new Bill("bill 2", Kobe.getId(), 10, events.get(i).getId());
            Bill b3 = new Bill("bill 3", Kobe.getId(), 6, events.get(i).getId());
            Bill b4 = new Bill("bill 4", Kobe.getId(), 6.5, events.get(i).getId());
            Bill b5 = new Bill("bill 5", Kobe.getId(), 9.2, events.get(i).getId());
            Bill b6 = new Bill("bill 6", Kobe.getId(), 8.1, events.get(i).getId());
            Bill b7 = new Bill("bill 7", Kobe.getId(), 7.5, events.get(i).getId());

            billManager.insertBill(b1);
            billManager.insertBill(b2);
            billManager.insertBill(b3);
            billManager.insertBill(b4);
            billManager.insertBill(b5);
            billManager.insertBill(b6);
            billManager.insertBill(b7);

            billManager.setSharingPersonsOfBill(b1, p_list);
            billManager.setSharingPersonsOfBill(b2, p_list);
            billManager.setSharingPersonsOfBill(b3, p_list);
            billManager.setSharingPersonsOfBill(b4, p_list);
            billManager.setSharingPersonsOfBill(b5, p_list);
            billManager.setSharingPersonsOfBill(b6, p_list);
            billManager.setSharingPersonsOfBill(b7, p_list);
        }


    }
    public void EraseAll() {
        List<Event> events = eventManager.getAllEvents();
        for(int i = 0; i < events.size(); i++) {
            eventManager.deleteEvent(events.get(i).getId());
        }
    }
}
