package com.example.sherrychuang.splitsmart.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sherrychuang.splitsmart.data.Event;
import com.example.sherrychuang.splitsmart.data.EventDate;
import com.example.sherrychuang.splitsmart.manager.EventManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherrychuang on 11/15/16.
 */

public class EventDAO implements EventManager {
    public static final String TABLE_NAME = "Event";
    public static final String KEY_ID = "_id";
    public static final String NAME_COLUMN = "name";
    public static final String START_MONTH_COLUMN = "startMonth";
    public static final String START_DAY_COLUMN = "startDay";
    public static final String END_MONTH_COLUMN = "endMonth";
    public static final String END_DAY_COLUMN = "endDay";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NAME_COLUMN + " TEXT NOT NULL, " +
                    START_MONTH_COLUMN + " INTEGER NOT NULL, " +
                    START_DAY_COLUMN + " INTEGER NOT NULL, " +
                    END_MONTH_COLUMN + " INTEGER NOT NULL, " +
                    END_DAY_COLUMN + " INTEGER NOT NULL)";

    private SQLiteDatabase db;

    public EventDAO(Context context) {
        db = SplitSmartDBHelper.getDatabase(context);
    }

    public void close() {
        db.close();
    }

    @Override
    public Event insertEvent(Event event) {
        ContentValues cv = convertToContentValue(event);

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);

        // 設定編號
        event.setId(id);
        // 回傳結果
        return event;
    }

    @Override
    public boolean updateEvent(Event event) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = convertToContentValue(event);

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = KEY_ID + "=" + event.getId();

        // 執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    private static ContentValues convertToContentValue(Event event) {
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(NAME_COLUMN, event.getName());
        cv.put(START_MONTH_COLUMN, event.getStartDate().getMonth());
        cv.put(START_DAY_COLUMN, event.getStartDate().getDay());
        cv.put(END_MONTH_COLUMN, event.getEndDate().getMonth());
        cv.put(END_DAY_COLUMN, event.getEndDate().getDay());

        return cv;
    }

    @Override
    public boolean deleteEvent(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = KEY_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where , null) > 0;
    }

    @Override
    public List<Event> getAllEvents() {
        List<Event> result = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    @Override
    public Event getEvent(long id) {
        Event item = null;

        String where = KEY_ID + "=" + id;
        Cursor result = db.query(TABLE_NAME, null, where, null, null, null, null, null);

        if (result.moveToFirst()) {
            item = getRecord(result);
        }

        result.close();
        return item;
    }

    // 把Cursor目前的資料包裝為物件
    private Event getRecord(Cursor cursor) {
        Event result = new Event();

        result.setId(cursor.getLong(0));
        result.setName(cursor.getString(1));
        result.setStartDate(new EventDate(cursor.getInt(2), cursor.getInt(3)));
        result.setEndDate(new EventDate(cursor.getInt(4), cursor.getInt(5)));

        return result;
    }
}
