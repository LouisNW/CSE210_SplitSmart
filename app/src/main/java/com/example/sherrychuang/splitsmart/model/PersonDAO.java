package com.example.sherrychuang.splitsmart.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sherrychuang.splitsmart.data.Person;
import com.example.sherrychuang.splitsmart.manager.PersonManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherrychuang on 11/15/16.
 */

public class PersonDAO implements PersonManager {
    public static final String TABLE_NAME = "Person";
    public static final String KEY_ID = "_id";
    public static final String NAME_COLUMN = "name";
    public static final String EMAIL_COLUMN = "email";
    public static final String EVENT_ID_COLUMN = "eventID";

    private static final String FOREIGN_KEY_RULES = "ON DELETE CASCADE ON UPDATE RESTRICT";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NAME_COLUMN + " TEXT NOT NULL, " +
                    EMAIL_COLUMN + " TEXT NOT NULL, " +
                    EVENT_ID_COLUMN + " INTEGER NOT NULL, " +
                    "FOREIGN KEY (" + EVENT_ID_COLUMN + ") REFERENCES " +
                    EventDAO.TABLE_NAME + "(" + EventDAO.KEY_ID + ") " +
                    FOREIGN_KEY_RULES + ")";

    private SQLiteDatabase db;

    public PersonDAO(Context context) {
        db = SplitSmartDBHelper.getDatabase(context);
    }

    public void close() {
        db.close();
    }

    @Override
    public Person insertPerson(Person person) {
        ContentValues cv = convertToContentValue(person);

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);

        // 設定編號
        person.setId(id);
        // 回傳結果
        return person;
    }

    @Override
    public boolean updatePerson(Person person) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = convertToContentValue(person);

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = KEY_ID + "=" + person.getId();

        // 執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    private static ContentValues convertToContentValue(Person person) {
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(NAME_COLUMN, person.getName());
        cv.put(EMAIL_COLUMN, person.getEmail());
        cv.put(EVENT_ID_COLUMN, person.getEventID());

        return cv;
    }

    @Override
    public boolean deletePerson(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = KEY_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where , null) > 0;
    }

    @Override
    public List<Person> getAllPersonsOfEvent(long eventID) {
        List<Person> result = new ArrayList<>();
        String where = EVENT_ID_COLUMN + "=" + eventID;
        Cursor cursor = db.query(TABLE_NAME, null, where, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    @Override
    public Person getPerson(long id) {
        Person item = null;

        String where = KEY_ID + "=" + id;
        Cursor result = db.query(TABLE_NAME, null, where, null, null, null, null, null);

        if (result.moveToFirst()) {
            item = getRecord(result);
        }

        result.close();
        return item;
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
