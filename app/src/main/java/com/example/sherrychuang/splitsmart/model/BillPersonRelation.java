package com.example.sherrychuang.splitsmart.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sherrychuang.splitsmart.data.Bill;
import com.example.sherrychuang.splitsmart.data.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Louis on 11/22/16.
 */

public class BillPersonRelation {
    public static final String TABLE_NAME = "BillPerson";
    public static final String BILL_ID_COLUMN = "billID";
    public static final String PERSON_ID_COLUMN = "personID";

    private static final String FOREIGN_KEY_RULES = "ON DELETE CASCADE ON UPDATE RESTRICT";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    BILL_ID_COLUMN + " INTEGER NOT NULL, " +
                    PERSON_ID_COLUMN + " INTEGER NOT NULL, " +
                    "PRIMARY KEY (" + BILL_ID_COLUMN + ", " + PERSON_ID_COLUMN + "), " +
                    "FOREIGN KEY (" + BILL_ID_COLUMN + ") REFERENCES " +
                    BillDAO.TABLE_NAME + "(" + BillDAO.KEY_ID + ") " +
                    FOREIGN_KEY_RULES + ", " +
                    "FOREIGN KEY (" + PERSON_ID_COLUMN + ") REFERENCES " +
                    EventDAO.TABLE_NAME + "(" + EventDAO.KEY_ID + ") " +
                    FOREIGN_KEY_RULES + ")";

    private SQLiteDatabase db;

    public BillPersonRelation(Context context) {
        db = SplitSmartDBHelper.getDatabase(context);
    }

    public List<Person> getPersonsOfBill(long billID) {
        List<Person> result = new ArrayList<>();

        String where = BILL_ID_COLUMN + "=" + billID;
        String[] selects = new String[] {PERSON_ID_COLUMN};
        Cursor cursor = db.query(TABLE_NAME, selects, where, null, null, null, null, null);

        while (cursor.moveToNext()) {
            long personID = cursor.getLong(0);
            result.add(getPerson(personID));
        }

        cursor.close();
        return result;
    }

    public boolean updatePersonsOfBill(Bill bill, List<Person> persons) {
        if (!isValidInput(bill, persons)) return false;

        long billID = bill.getId();
        deleteAllPersonsOfBill(billID);

        for (Person person : persons) {
            long personID = person.getId();
            ContentValues cv = convertToContentValue(billID, personID);

            // 新增一筆資料並取得編號
            // 第一個參數是表格名稱
            // 第二個參數是沒有指定欄位值的預設值
            // 第三個參數是包裝新增資料的ContentValues物件
            long id = db.insert(TABLE_NAME, null, cv);
            // 設定編號
//            bill.setId(id);
        }
        return true;
    }

    // Check if all the persons are belong to the event
    private boolean isValidInput(Bill bill, List<Person> persons) {
        long billID = bill.getId();

        for (Person p : persons) {
            if (p.getEventID() != billID) return false;
        }
        return true;
    }

    private void deleteAllPersonsOfBill(long billID) {
        String where = BILL_ID_COLUMN + "=" + billID;
        // 刪除指定編號資料並回傳刪除是否成功
        db.delete(TABLE_NAME, where , null);
    }

    private static ContentValues convertToContentValue(long billID, long personID) {
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(BILL_ID_COLUMN, billID);
        cv.put(PERSON_ID_COLUMN, personID);

        return cv;
    }

    private Person getPerson(long id) {
        Person person = null;

        String where = PersonDAO.KEY_ID + "=" + id;
        Cursor result = db.query(PersonDAO.TABLE_NAME, null, where, null, null, null, null, null);

        if (result.moveToFirst()) {
            person = getRecord(result);
        }

        result.close();
        return person;
    }

    // 把Cursor目前的資料包裝為物件
    private Person getRecord(Cursor cursor) {
        Person result = new Person();

        result.setId(cursor.getLong(0));
        result.setName(cursor.getString(1));
        result.setEmail(cursor.getString(2));
        result.setEventID(cursor.getLong(3));

        return result;
    }
}
