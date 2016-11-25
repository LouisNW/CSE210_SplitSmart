package com.example.sherrychuang.splitsmart.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sherrychuang.splitsmart.data.Bill;
import com.example.sherrychuang.splitsmart.data.Person;
import com.example.sherrychuang.splitsmart.manager.BillManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherrychuang on 11/15/16.
 */

public class BillDAO implements BillManager {
    public static final String TABLE_NAME = "Bill";
    public static final String KEY_ID = "_id";
    public static final String NAME_COLUMN = "name";
    public static final String TAX_RATE_COLUMN = "taxRate";
    public static final String OWNER_ID_COLUMN = "ownerID";
    public static final String EVENT_ID_COLUMN = "eventID";

    private static final String FOREIGN_KEY_RULES = "ON DELETE CASCADE ON UPDATE RESTRICT";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NAME_COLUMN + " TEXT NOT NULL, " +
                    TAX_RATE_COLUMN + " REAL NOT NULL, " +
                    OWNER_ID_COLUMN + " INTEGER NOT NULL, " +
                    EVENT_ID_COLUMN + " INTEGER NOT NULL, " +
                    "FOREIGN KEY (" + OWNER_ID_COLUMN + ") REFERENCES " +
                    PersonDAO.TABLE_NAME + "(" + PersonDAO.KEY_ID + ") " +
                    FOREIGN_KEY_RULES + ", " +
                    "FOREIGN KEY (" + EVENT_ID_COLUMN + ") REFERENCES " +
                    EventDAO.TABLE_NAME + "(" + EventDAO.KEY_ID + ") " +
                    FOREIGN_KEY_RULES + ")";

    private SQLiteDatabase db;
    private BillPersonRelation billPersonRelation;

    public BillDAO(Context context) {
        db = SplitSmartDBHelper.getDatabase(context);
        billPersonRelation = new BillPersonRelation(context);
    }

    public void close() {
        db.close();
    }

    @Override
    public Bill insertBill(Bill bill) {
        ContentValues cv = convertToContentValue(bill);

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);

        // 設定編號
        bill.setId(id);
        // 回傳結果
        return bill;
    }

    @Override
    public boolean updateBill(Bill bill) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = convertToContentValue(bill);

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = KEY_ID + "=" + bill.getId();

        // 執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    private static ContentValues convertToContentValue(Bill bill) {
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(NAME_COLUMN, bill.getName());
        cv.put(TAX_RATE_COLUMN, bill.getTaxRate());
        cv.put(OWNER_ID_COLUMN, bill.getOwnerID());
        cv.put(EVENT_ID_COLUMN, bill.getEventID());

        return cv;
    }

    @Override
    public boolean deleteBill(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = KEY_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where , null) > 0;
    }

    @Override
    public List<Bill> getAllBillsOfEvent(long eventID) {
        List<Bill> result = new ArrayList<>();
        String where = EVENT_ID_COLUMN + "=" + eventID;
        Cursor cursor = db.query(TABLE_NAME, null, where, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    @Override
    public Bill getBill(long id) {
        Bill bill = null;

        String where = KEY_ID + "=" + id;
        Cursor result = db.query(TABLE_NAME, null, where, null, null, null, null, null);

        if (result.moveToFirst()) {
            bill = getRecord(result);
        }

        result.close();
        return bill;
    }

    // 把Cursor目前的資料包裝為物件
    private Bill getRecord(Cursor cursor) {
        Bill result = new Bill();

        result.setId(cursor.getLong(0));
        result.setName(cursor.getString(1));
        result.setTaxRate(cursor.getDouble(2));
        result.setOwnerID(cursor.getLong(3));
        result.setEventID(cursor.getLong(4));

        return result;
    }

    @Override
    public List<Person> getSharingPersonsOfBill(long billID) {
        return billPersonRelation.getPersonsOfBill(billID);
    }

    @Override
    public boolean setSharingPersonsOfBill(Bill bill, List<Person> persons) {
        return billPersonRelation.updatePersonsOfBill(bill, persons);
    }
}
