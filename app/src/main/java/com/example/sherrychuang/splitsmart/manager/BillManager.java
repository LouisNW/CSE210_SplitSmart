package com.example.sherrychuang.splitsmart.manager;

import com.example.sherrychuang.splitsmart.data.Bill;
import com.example.sherrychuang.splitsmart.data.Person;

import java.util.List;

/**
 * Created by Louis on 11/23/16.
 */

public interface BillManager {
    /**
     * Effectively Event.getBills()
     */
    public List<Bill> getAllBillsOfEvent(long eventID);

    /**
     * @param billID
     * @return null when the bill does not exist
     */
    public Bill getBill(long billID);

    public boolean deleteBill(long billID);

    /**
     * Updating a non-existing bill will fail.
     * @param bill to update
     * @return boolean indicating whether the update succeeded
     */
    public boolean updateBill(Bill bill);

    /**
     * Inserting an existing bill has no effect.
     * @param bill to insert
     * @return the inserted bill with an id assigned by the DB; null when failing
     */
    public Bill insertBill(Bill bill);

    /**
     * Precondition: (This function won't check them for you) (Can do it later if I have time)
     *  1) Bill is in the DB.
     *  2) All persons are in the DB.
     * For simplicity, this function simply replaces the previous list (if any) with the given list.
     * @param bill
     * @param persons
     * @return false if any of the person does not belong to the same event
     */
    public boolean setSharingPersonsOfBill(Bill bill, List<Person> persons);

    public List<Person> getSharingPersonsOfBill(long billID);
}
