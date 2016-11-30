package com.example.sherrychuang.splitsmart.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sherrychuang.splitsmart.data.Bill;
import com.example.sherrychuang.splitsmart.data.Item;
import com.example.sherrychuang.splitsmart.data.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Louis on 11/22/16.
 */

public class ItemPersonRelation {
    public static final String TABLE_NAME = "ItemPerson";
    public static final String ITEM_ID_COLUMN = "itemID";
    public static final String PERSON_ID_COLUMN = "personID";

    private static final String FOREIGN_KEY_RULES = "ON DELETE CASCADE ON UPDATE RESTRICT";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    ITEM_ID_COLUMN + " INTEGER NOT NULL, " +
                    PERSON_ID_COLUMN + " INTEGER NOT NULL, " +
                    "PRIMARY KEY (" + ITEM_ID_COLUMN + ", " + PERSON_ID_COLUMN + "), " +
                    "FOREIGN KEY (" + ITEM_ID_COLUMN + ") REFERENCES " +
                    ItemDAO.TABLE_NAME + "(" + ItemDAO.KEY_ID + ") " +
                    FOREIGN_KEY_RULES + ", " +
                    "FOREIGN KEY (" + PERSON_ID_COLUMN + ") REFERENCES " +
                    PersonDAO.TABLE_NAME + "(" + PersonDAO.KEY_ID + ") " +
                    FOREIGN_KEY_RULES + ")";

    private SQLiteDatabase db;

    public ItemPersonRelation(Context context) {
        db = SplitSmartDBHelper.getDatabase(context);
    }

    public List<Person> getPersonsOfItem(long itemID) {
        List<Person> result = new ArrayList<>();

        String where = ITEM_ID_COLUMN + "=" + itemID;
        String[] selects = new String[] {PERSON_ID_COLUMN};
        Cursor cursor = db.query(TABLE_NAME, selects, where, null, null, null, null, null);

        while (cursor.moveToNext()) {
            long personID = cursor.getLong(0);
            result.add(getPerson(personID));
        }

        cursor.close();
        return result;
    }

    public boolean updatePersonsOfItem(Item item, List<Person> persons) {
        if (!isValidInput(item, persons)) return false;

        long itemID = item.getId();
        deleteAllPersonsOfItem(itemID);

        for (Person person : persons) {
            long personID = person.getId();
            ContentValues cv = convertToContentValue(itemID, personID);

            // 新增一筆資料並取得編號
            // 第一個參數是表格名稱
            // 第二個參數是沒有指定欄位值的預設值
            // 第三個參數是包裝新增資料的ContentValues物件
            long id = db.insert(TABLE_NAME, null, cv);
            // 設定編號
//            item.setId(id);
        }
        return true;
    }

    // Check if all the persons are belong to the event and the bill
    private boolean isValidInput(Item item, List<Person> persons) {
        long billID = item.getBillID();
        Bill bill = getBill(billID);
        long eventID = bill.getEventID();
        for (Person p : persons)
            if (p.getEventID() != eventID || !isValidBillPersonPair(billID, p.getId()))
                return false;

        return true;
    }

    private boolean isValidBillPersonPair(long billID, long personID) {

        String where = BillPersonRelation.BILL_ID_COLUMN + "=" + billID +
                " AND " + BillPersonRelation.PERSON_ID_COLUMN + "=" + personID;
        Cursor result = db.query(BillPersonRelation.TABLE_NAME, null, where, null, null, null, null, null);

        int resCnt = result.getCount();

        result.close();
        return resCnt == 1;
    }

    private Bill getBill(long billID) {
        Bill bill = null;

        String where = BillDAO.KEY_ID + "=" + billID;
        Cursor result = db.query(BillDAO.TABLE_NAME, null, where, null, null, null, null, null);

        if (result.moveToFirst()) {
            bill = convertToBill(result);
        }

        result.close();
        return bill;
    }

    private Bill convertToBill(Cursor cursor) {
        Bill result = new Bill();

        result.setId(cursor.getLong(0));
        result.setName(cursor.getString(1));
        result.setTaxRate(cursor.getDouble(2));
        result.setOwnerID(cursor.getLong(3));
        result.setEventID(cursor.getLong(4));

        return result;
    }

    private void deleteAllPersonsOfItem(long itemID) {
        String where = ITEM_ID_COLUMN + "=" + itemID;
        // 刪除指定編號資料並回傳刪除是否成功
        db.delete(TABLE_NAME, where , null);
    }

    private static ContentValues convertToContentValue(long itemID, long personID) {
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(ITEM_ID_COLUMN, itemID);
        cv.put(PERSON_ID_COLUMN, personID);

        return cv;
    }

    private Person getPerson(long id) {
        Person person = null;

        String where = PersonDAO.KEY_ID + "=" + id;
        Cursor result = db.query(PersonDAO.TABLE_NAME, null, where, null, null, null, null, null);

        if (result.moveToFirst()) {
            person = convertToPerson(result);
        }

        result.close();
        return person;
    }

    // 把Cursor目前的資料包裝為物件
    private Person convertToPerson(Cursor cursor) {
        Person result = new Person();

        result.setId(cursor.getLong(0));
        result.setName(cursor.getString(1));
        result.setEmail(cursor.getString(2));
        result.setEventID(cursor.getLong(3));

        return result;
    }
}
