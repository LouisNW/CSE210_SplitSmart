package com.example.sherrychuang.splitsmart.model;

import android.content.Context;

import com.example.sherrychuang.splitsmart.data.Bill;
import com.example.sherrychuang.splitsmart.data.Event;
import com.example.sherrychuang.splitsmart.data.EventDate;
import com.example.sherrychuang.splitsmart.data.Item;
import com.example.sherrychuang.splitsmart.data.Person;
import com.example.sherrychuang.splitsmart.manager.BillManager;
import com.example.sherrychuang.splitsmart.manager.EventManager;
import com.example.sherrychuang.splitsmart.manager.ItemManager;
import com.example.sherrychuang.splitsmart.manager.ManagerFactory;
import com.example.sherrychuang.splitsmart.manager.PersonManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Louis on 11/24/16.
 */

public class DatabaseTest {
    private Context context;

    private String test = "Louis test: ";

    public DatabaseTest(Context context) {
        this.context = context;
    }

    public void run() {
        eventTest();
        personTest();
        billTest();
        itemTest();
        foreignKeyIntegrityTest();
        billPersonRelationTest();
        itemPersonRelationTest();
    }

    private void eventTest() {
        System.out.println("== EventTest ==");
        EventManager eventManager = ManagerFactory.getEventManager(context);

        System.out.println("creating events...");
        Event LAevent = new Event("LA", new EventDate(11,21), new EventDate(11,22));
        Event SDevent = new Event("SD", new EventDate(12,21), new EventDate(12,20));
        LAevent = eventManager.insertEvent(LAevent);
        SDevent = eventManager.insertEvent(SDevent);

        List<Event> eventList = eventManager.getAllEvents();
        for (Event e : eventList)
            System.out.println(test + e);

        System.out.println("* testing event.delete");
        eventManager.deleteEvent(LAevent.getId());
        eventList = eventManager.getAllEvents();
        for (Event e : eventList)
            System.out.println(test + e);

        System.out.println("adding back LAevent...");
        LAevent = eventManager.insertEvent(LAevent);

        System.out.println("* testing event.update");
        LAevent.setName("LAtrip");
        eventManager.updateEvent(LAevent);
        eventList = eventManager.getAllEvents();
        for (Event e : eventList)
            System.out.println(test + e);

    }

    private void personTest() {
        System.out.println("== PersonTest ==");
        System.out.println("creating context...");
        EventManager eventManager = ManagerFactory.getEventManager(context);
        Event LAevent = new Event("LA", new EventDate(11,21), new EventDate(11,22));
        LAevent = eventManager.insertEvent(LAevent);
        PersonManager personManager = ManagerFactory.getPersonManager(context);

        Person alice = new Person("Alice", "abc@ucsd.edu", LAevent.getId());
        Person bob = new Person("Bob", "def@ucsd.edu", LAevent.getId());
        alice = personManager.insertPerson(alice);
        bob = personManager.insertPerson(bob);

        System.out.println("* testing person.getAllPersonsOfEvent");
        List<Person> persons = personManager.getAllPersonsOfEvent(LAevent.getId());
        for (Person p : persons)
            System.out.println(p);
//        alice = personManager.getPerson(alice.getId());
//        bob = personManager.getPerson(bob.getId());
//        System.out.println(alice);
//        System.out.println(bob);

        System.out.println("* testing person.delete");
        personManager.deletePerson(bob.getId());
        if (personManager.getPerson(bob.getId()) != null) {
            System.out.println("--> person.delete FAIL");
        }
        else
            System.out.println("--> person.delete PASS");

        System.out.println("adding Bob back ...");
        personManager.insertPerson(bob);

        System.out.println("* testing person.update");
        alice.setEmail("xyz@ucsd.edu");
        personManager.updatePerson(alice);
        System.out.println(personManager.getPerson(alice.getId()));
    }

    private void billTest() {
        System.out.println("=== Bill Test ===");
        System.out.println("creating context...");
        EventManager eventManager = ManagerFactory.getEventManager(context);
        Event LAtrip = new Event("LAtrip",new EventDate(11,21), new EventDate(11,22));
        eventManager.insertEvent(LAtrip);

        PersonManager personManager = ManagerFactory.getPersonManager(context);
        Person alice = new Person("Alice","abc@gmail.com", LAtrip.getId());
        personManager.insertPerson(alice);

        BillManager billManager = ManagerFactory.getBillManager(context);
        Bill ralphs = new Bill("ralphs", alice.getId(), 8, LAtrip.getId());
        Bill costco = new Bill("costco", alice.getId(), 7, LAtrip.getId());
        ralphs = billManager.insertBill(ralphs);
        costco = billManager.insertBill(costco);

        System.out.println("* testing bill.getAllBillsOfEvent");
        List<Bill> allBill = billManager.getAllBillsOfEvent(LAtrip.getId());
        for (Bill b : allBill) System.out.println(b);
//        ralphs = billManager.getBill(ralphs.getId());
//        costco = billManager.getBill(costco.getId());
//        System.out.println(ralphs);
//        System.out.println(costco);

        System.out.println("* testing bill.delete");
        billManager.deleteBill(ralphs.getId());
        if (billManager.getBill(ralphs.getId()) != null) {
            System.out.println("--> bill.delete FAIL");
        }
        else
            System.out.println("--> bill.delete PASS");

        System.out.println("* testing bill.update");
        costco.setTaxRate(9);
        System.out.println("update result: " + billManager.updateBill(costco));
        System.out.println(billManager.getBill(costco.getId()));

        System.out.println("* insert an existing bill");
        Bill costco2 = billManager.insertBill(costco);
        System.out.println("costco: " + costco);
        System.out.println("costco2: " + costco2);
    }

    private void itemTest() {
        System.out.println("=== Item Test ===");
        System.out.println("creating context...");
        EventManager eventManager = ManagerFactory.getEventManager(context);
        Event LAtrip = new Event("LAtrip",new EventDate(11,21), new EventDate(11,22));
        eventManager.insertEvent(LAtrip);

        PersonManager personManager = ManagerFactory.getPersonManager(context);
        Person alice = new Person("Alice","abc@gmail.com", LAtrip.getId());
        personManager.insertPerson(alice);

        BillManager billManager = ManagerFactory.getBillManager(context);
        Bill ralphs = new Bill("ralphs", alice.getId(), 8, LAtrip.getId());
        ralphs = billManager.insertBill(ralphs);

        ItemManager itemManager = ManagerFactory.getItemManager(context);
        Item apple = new Item("apple",3.2,false,ralphs.getId());
        Item egg = new Item("egg",2.3,true,ralphs.getId());
        apple = itemManager.insertItem(apple);
        egg = itemManager.insertItem(egg);

        System.out.println("* testing item.getAllItemsOfBill");
        List<Item> itemList = itemManager.getAllItemsOfBill(ralphs.getId());
        for (Item item : itemList)
            System.out.println(item);
//        apple = itemManager.getItem(apple.getId());
//        egg = itemManager.getItem(egg.getId());
//        System.out.println(apple);
//        System.out.println(egg);

        System.out.println("* testing item.delete");
        itemManager.deleteItem(egg.getId());
        if (itemManager.getItem(egg.getId()) != null) {
            System.out.println("--> item.delete FAIL");
        }
        else
            System.out.println("--> item.delete PASS");

        System.out.println("* testing item.update");
        apple.setPrice(9.2);
        System.out.println("update result: " + itemManager.updateItem(apple));
        System.out.println(itemManager.getItem(apple.getId()));
    }

    private void foreignKeyIntegrityTest() {
        System.out.println("=== Foreign Key Integrity Test ===");
        EventManager eventManager = ManagerFactory.getEventManager(context);
        PersonManager personManager = ManagerFactory.getPersonManager(context);

        System.out.println("create event:");
        Event NYtrip = new Event("NYtrip",new EventDate(1,1), new EventDate(2,1));
        NYtrip = eventManager.insertEvent(NYtrip);
        System.out.println(NYtrip);

        System.out.println("create person");
        Person ryan = new Person("Ryan","ryan@gmail.com",NYtrip.getId());
        Person jenny = new Person("Jenny", "jenny@gmail.com",NYtrip.getId());
        personManager.insertPerson(ryan);
        personManager.insertPerson(jenny);
        System.out.println(ryan);
        System.out.println(jenny);

        System.out.println("* test update integrity");
        NYtrip.setId(100);
        boolean updateRes = eventManager.updateEvent(NYtrip);
        if (updateRes) System.out.println("Can update referenced key in event -> FAIL");
        else System.out.println("Cannot update referenced key in event -> PASS");

        System.out.println("* test delete integrity");
        eventManager.deleteEvent(NYtrip.getId());
        if (personManager.getPerson(ryan.getId()) == null)
            System.out.println("deletion of referenced event propagates to referencing person -> PASS");
        else System.out.println("deletion does not propagates -> FAIL");
    }

    private void billPersonRelationTest() {
        System.out.println("=== BillPersonRelation Test ===");
        EventManager eventManager = ManagerFactory.getEventManager(context);
        BillManager billManager = ManagerFactory.getBillManager(context);
        PersonManager personManager = ManagerFactory.getPersonManager(context);

        Event LAtrip = new Event("LAtrip", new EventDate(1,1), new EventDate(2,1));
        Event SDtrip = new Event("SDtrip", new EventDate(1,1), new EventDate(2,1));
        eventManager.insertEvent(LAtrip);
        eventManager.insertEvent(SDtrip);

        Person alice = new Person("Alice", "abd@gmail.com", LAtrip.getId());
        Person bob = new Person("Bob", "xuy@gmail.com", LAtrip.getId());
        personManager.insertPerson(alice);
        personManager.insertPerson(bob);

        // another person that belongs to SDtrip
        Person Ray = new Person("Ray", "xuy@gmail.com", SDtrip.getId());
        personManager.insertPerson(Ray);

        Bill ralphs = new Bill("ralphs", alice.getId(), 0, LAtrip.getId());
        billManager.insertBill(ralphs);

        List<Person> LAlist = new ArrayList<>();
        LAlist.add(alice);
        LAlist.add(bob);
        billManager.setSharingPersonsOfBill(ralphs, LAlist);

        System.out.println("Person list of ralph is:");
        for (Person p : billManager.getSharingPersonsOfBill(ralphs.getId()))
            System.out.println(p);

        System.out.println("Trying to set an invalid list...");
        System.out.println("(add a person from another event)");
        List<Person> wrongList = new ArrayList<>();
        wrongList.add(bob);
        wrongList.add(Ray);
        if (!billManager.setSharingPersonsOfBill(ralphs, wrongList))
            System.out.println("set rejected -> PASS");
        else System.out.println("set accept -> FAIL");
    }

    private void itemPersonRelationTest() {
        System.out.println("=== ItemPersonRelation Test ===");
        EventManager eventManager = ManagerFactory.getEventManager(context);
        BillManager billManager = ManagerFactory.getBillManager(context);
        PersonManager personManager = ManagerFactory.getPersonManager(context);
        ItemManager itemManager = ManagerFactory.getItemManager(context);

        Event LAtrip = new Event("LAtrip", new EventDate(1,1), new EventDate(2,1));
        Event SDtrip = new Event("SDtrip", new EventDate(1,1), new EventDate(2,1));
        eventManager.insertEvent(LAtrip);
        eventManager.insertEvent(SDtrip);

        Person alice = new Person("Alice", "abd@gmail.com", LAtrip.getId());
        Person bob = new Person("Bob", "xuy@gmail.com", LAtrip.getId());
        Person Ray = new Person("Ray", "xuy@gmail.com", SDtrip.getId()); // under different event
        personManager.insertPerson(alice);
        personManager.insertPerson(bob);
        personManager.insertPerson(Ray);

        Bill ralphs = new Bill("ralphs", alice.getId(), 0, LAtrip.getId());
        Bill costco = new Bill("costco", bob.getId(),1, LAtrip.getId());
        billManager.insertBill(ralphs);
        billManager.insertBill(costco);

        List<Person> ralphPersonList = new ArrayList<>();
        ralphPersonList.add(alice);
        ralphPersonList.add(bob);
        billManager.setSharingPersonsOfBill(ralphs, ralphPersonList);

        List<Person> costcoPersonList = new ArrayList<>();
        costcoPersonList.add(alice);
        billManager.setSharingPersonsOfBill(costco, costcoPersonList);

        System.out.println("Person list of ralph is:");
        for (Person p : billManager.getSharingPersonsOfBill(ralphs.getId()))
            System.out.println(p);
        System.out.println("Person list of costco is:");
        for (Person p : billManager.getSharingPersonsOfBill(costco.getId()))
            System.out.println(p);

        Item cake = new Item("cake",3,true,ralphs.getId());
        itemManager.insertItem(cake);
        List<Person> cakePersonList = new ArrayList<>();
        cakePersonList.add(alice);
        cakePersonList.add(bob);
        itemManager.setSharingPersonsOfAnItem(cake, cakePersonList);

        System.out.println("Person list of cake is:");
        for (Person p : itemManager.getSharingPersonsOfAnItem(cake.getId()))
            System.out.println(p);

        System.out.println("Trying to set an invalid list to ItemPersonRelation...");
        Item cat = new Item("cat",100,false,costco.getId());
        itemManager.insertItem(cat);

        System.out.println("1) add a person from another event");
        List<Person> wrongList = new ArrayList<>();
        wrongList.add(alice);
        wrongList.add(Ray);
        itemManager.setSharingPersonsOfAnItem(cat,wrongList);
        if (!itemManager.setSharingPersonsOfAnItem(cat, wrongList))
            System.out.println("set rejected -> PASS");
        else System.out.println("set accept -> FAIL");

        System.out.println("2) add a person from another bill");
        List<Person> wrongList2 = new ArrayList<>();
        wrongList2.add(alice);
        wrongList2.add(bob);
        itemManager.setSharingPersonsOfAnItem(cat,wrongList2);
        if (!itemManager.setSharingPersonsOfAnItem(cat, wrongList2))
            System.out.println("set rejected -> PASS");
        else System.out.println("set accept -> FAIL");
    }
}
