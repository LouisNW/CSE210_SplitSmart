package com.example.sherrychuang.splitsmart.manager;

import com.example.sherrychuang.splitsmart.data.Item;
import com.example.sherrychuang.splitsmart.data.Person;

import java.util.List;

/**
 * Created by Louis on 11/23/16.
 */

public interface ItemManager {
    /**
     * Effectively bill.getAllItems()
     */
    public List<Item> getAllItemsOfBill(long billID);

    /**
     * @param itemID
     * @return null when the bill does not exist
     */
    public Item getItem(long itemID);
    public boolean deleteItem(long itemID);

    /**
     * Updating a non-existing item will fail.
     * @param item to update
     * @return boolean indicating whether the update succeeded
     */
    public boolean updateItem(Item item);

    /**
     * Inserting an existing item has no effect.
     * @param item to insert
     * @return the inserted item with an id assigned by the DB; null when failing
     */
    public Item insertItem(Item item);

    /**
     * Precondition: (This function won't do the check for you) (Can do it later if I have time)
     *  1) Item is in the DB.
     *  2) All persons are in the DB.
     * For simplicity, this function simply replaces the previous list (if any) with the given list.
     * @param item
     * @param persons
     * @return false if any of the persons does not belong to the bill and the event
     */
    public boolean setSharingPersonsOfAnItem(Item item, List<Person> persons);

    /**
     * Effectively Item.getSharingPersons()
     */
    public List<Person> getSharingPersonsOfAnItem(long itemID);
}
