package com.example.sherrychuang.splitsmart.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sherrychuang.splitsmart.data.Item;
import com.example.sherrychuang.splitsmart.data.Person;
import com.example.sherrychuang.splitsmart.manager.ItemManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Manage the Item-Item table as well
 */

public class ItemDAO implements ItemManager {
    public static final String TABLE_NAME = "Item";
    public static final String KEY_ID = "_id";
    public static final String NAME_COLUMN = "name";
    public static final String PRICE_COLUMN = "price";
    public static final String IS_TAXED_ITEM_COLUMN = "isTaxedItem";
    public static final String BILL_ID_COLUMN = "billID";

    private static final String FOREIGN_KEY_RULES = "ON DELETE CASCADE ON UPDATE RESTRICT";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NAME_COLUMN + " TEXT NOT NULL, " +
                    PRICE_COLUMN + " REAL NOT NULL, " +
                    IS_TAXED_ITEM_COLUMN + " INTEGER NOT NULL, " +
                    BILL_ID_COLUMN + " INTEGER NOT NULL, " +
                    "FOREIGN KEY (" + BILL_ID_COLUMN + ") REFERENCES " +
                    BillDAO.TABLE_NAME + "(" + BillDAO.KEY_ID + ") " +
                    FOREIGN_KEY_RULES + ")";

    private SQLiteDatabase db;
    private ItemPersonRelation itemPersonRelation;

    public ItemDAO(Context context) {
        db = SplitSmartDBHelper.getDatabase(context);
        itemPersonRelation = new ItemPersonRelation(context);
    }

    public void close() {
        db.close();
    }

    @Override
    public Item insertItem(Item item) {
        ContentValues cv = convertToContentValue(item);

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);

        // 設定編號
        item.setId(id);
        // 回傳結果
        return item;
    }

    @Override
    public boolean updateItem(Item item) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = convertToContentValue(item);

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = KEY_ID + "=" + item.getId();

        // 執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    private static ContentValues convertToContentValue(Item item) {
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(NAME_COLUMN, item.getName());
        cv.put(PRICE_COLUMN, item.getPrice());
        cv.put(IS_TAXED_ITEM_COLUMN, item.isTaxItem());
        cv.put(BILL_ID_COLUMN, item.getBillID());

        return cv;
    }

    @Override
    public boolean deleteItem(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = KEY_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where , null) > 0;
    }

    @Override
    public List<Item> getAllItemsOfBill(long billID) {
        List<Item> result = new ArrayList<>();
        String where = BILL_ID_COLUMN + "=" + billID;
        Cursor cursor = db.query(TABLE_NAME, null, where, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    @Override
    public Item getItem(long id) {
        Item item = null;

        String where = KEY_ID + "=" + id;
        Cursor result = db.query(TABLE_NAME, null, where, null, null, null, null, null);

        if (result.moveToFirst()) {
            item = getRecord(result);
        }

        result.close();
        return item;
    }

    // 把Cursor目前的資料包裝為物件
    private Item getRecord(Cursor cursor) {
        Item result = new Item();

        result.setId(cursor.getLong(0));
        result.setName(cursor.getString(1));
        result.setPrice(cursor.getDouble(2));
        result.setTaxItem(cursor.getInt(3) == 1);
        result.setBillID(cursor.getLong(4));

        return result;
    }

    @Override
    public boolean setSharingPersonsOfAnItem(Item item, List<Person> persons) {
        return itemPersonRelation.updatePersonsOfItem(item, persons);
    }

    @Override
    public List<Person> getSharingPersonsOfAnItem(long itemID) {
        return itemPersonRelation.getPersonsOfItem(itemID);
    }

}
