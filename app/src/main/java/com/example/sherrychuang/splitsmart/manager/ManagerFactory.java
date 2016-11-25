package com.example.sherrychuang.splitsmart.manager;

import android.content.Context;

import com.example.sherrychuang.splitsmart.data.Event;
import com.example.sherrychuang.splitsmart.model.BillDAO;
import com.example.sherrychuang.splitsmart.model.EventDAO;
import com.example.sherrychuang.splitsmart.model.ItemDAO;
import com.example.sherrychuang.splitsmart.model.PersonDAO;

/**
 * Created by Louis on 11/23/16.
 */

public class ManagerFactory {
    private static BillManager billManager;
    private static EventManager eventManager;
    private static ItemManager itemManager;
    private static PersonManager personManager;

    //TODO: test if multiple pages can use this BillManager constructed by the same context
    public static BillManager getBillManager(Context context) {
        if (billManager == null) {
            billManager = new BillDAO(context);
        }
        return billManager;
    }

    public static EventManager getEventManager(Context context) {
        if (eventManager == null) {
            eventManager = new EventDAO(context);
        }
        return eventManager;
    }

    public static ItemManager getItemManager(Context context) {
        if (itemManager == null) {
            itemManager = new ItemDAO(context);
        }
        return itemManager;
    }

    public static PersonManager getPersonManager(Context context) {
        if (personManager == null) {
            personManager = new PersonDAO(context);
        }
        return personManager;
    }
}
