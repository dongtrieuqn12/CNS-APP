package com.example.user.myfinalprojectssc_ver1.SQLiteDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.user.myfinalprojectssc_ver1.ModelSQLite.RecordInvoicesInDay;
import com.example.user.myfinalprojectssc_ver1.ModelSQLite.RecordTopUp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ho Dong Trieu on 09/10/2018
 */
public class DatabaseHelperRecord extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    //Create database for record Invoices.
    //create database.
    private static final String DATABASE_NAME = "QuanLyInvoices";
    private static final String TABLE_NAME = "invoicesToday";

    //create value of database
    private static final String KEY_ID = "id";
    private static final String KEY_CODE_HOA_DON = "InvoicesCode";
    private static final String KEY_UID = "uid";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_DATE_TIME = "datetime";
    private static final String KEY_REQUEST_TO_BE = "request";
    private static final String KEY_STATUS_REQUEST = "status";

    public DatabaseHelperRecord(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_MENU_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_UID + " TEXT,"
                + KEY_CODE_HOA_DON + " TEXT," + KEY_DATE_TIME + " TEXT,"
                + KEY_AMOUNT + " TEXT," + KEY_REQUEST_TO_BE + " TEXT," + KEY_STATUS_REQUEST + " INTEGER" + ")";

        String CREATE_MENU_TABLE_TOP_UP = "CREATE TABLE " + TABLE_NAME_TOP_UP + "("
                + KEY_ID_TOP_UP + " INTEGER PRIMARY KEY," + KEY_CARDID + " TEXT,"
                + KEY_AMOUNT_BEFORE_TOP_UP + " TEXT," + KEY_AMOUNT_AFTER_TOP_UP + " TEXT," + KEY_DATE_TIME_TOP_UP + " TEXT,"
                + KEY_STATUS_TOP_UP + " INTEGER" + ")";

        sqLiteDatabase.execSQL(CREATE_MENU_TABLE);
        sqLiteDatabase.execSQL(CREATE_MENU_TABLE_TOP_UP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TOP_UP);
        onCreate(sqLiteDatabase);
    }

    public void addOfflineInvoices(RecordInvoicesInDay offlineInvoices){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UID,offlineInvoices.getUid());
        values.put(KEY_CODE_HOA_DON,offlineInvoices.getInvoicesCode());
        values.put(KEY_DATE_TIME,offlineInvoices.getDatetime());
        values.put(KEY_AMOUNT,offlineInvoices.getAmount());
        values.put(KEY_REQUEST_TO_BE,offlineInvoices.getRequest());
        values.put(KEY_STATUS_REQUEST,offlineInvoices.getStatus());

        db.insert(TABLE_NAME, null,values);
        db.close();
    }

    public RecordInvoicesInDay getOfflineInvoices(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,new String[]{ KEY_ID , KEY_UID, KEY_CODE_HOA_DON,KEY_DATE_TIME,KEY_AMOUNT,KEY_REQUEST_TO_BE,KEY_STATUS_REQUEST},KEY_ID + "=?",
                new String[] {String.valueOf(id)},null,null,null,null);

        if(cursor != null)
            cursor.moveToFirst();

        RecordInvoicesInDay offlineInvoices = new RecordInvoicesInDay(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getInt(6));

        cursor.close();
        db.close();
        return offlineInvoices;
    }


    public int Update(RecordInvoicesInDay offlineInvoices){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_UID,offlineInvoices.getUid());
        values.put(KEY_CODE_HOA_DON,offlineInvoices.getInvoicesCode());
        values.put(KEY_DATE_TIME,offlineInvoices.getDatetime());
        values.put(KEY_AMOUNT,offlineInvoices.getAmount());
        values.put(KEY_REQUEST_TO_BE,offlineInvoices.getRequest());
        values.put(KEY_STATUS_REQUEST,offlineInvoices.getStatus());

        return db.update(TABLE_NAME,values,KEY_ID + "=?",new String[]{String.valueOf(offlineInvoices.getId())});
    }

    public List<RecordInvoicesInDay> getAllOfflineInvoices(){
        List<RecordInvoicesInDay> offlineInvoicesList = new ArrayList<>();

        String selectQuerry = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuerry,null);

        if(cursor.moveToFirst()){
            do {
                RecordInvoicesInDay offlineInvoices = new RecordInvoicesInDay();
                offlineInvoices.setId(Integer.parseInt(cursor.getString(0)));
                offlineInvoices.setUid(cursor.getString(1));
                offlineInvoices.setInvoicesCode(cursor.getString(2));
                offlineInvoices.setDatetime(cursor.getString(3));
                offlineInvoices.setAmount(cursor.getString(4));
                offlineInvoices.setRequest(cursor.getString(5));
                offlineInvoices.setStatus(cursor.getInt(6));
                offlineInvoicesList.add(offlineInvoices);
            } while (cursor.moveToNext());
        }

        return offlineInvoicesList;
    }

    public int getOfflineInvoicesCount(){
        String counQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(counQuery,null);
        cursor.close();

        return cursor.getCount();
    }

    public void delectOfflineInvoice(RecordInvoicesInDay offlineInvoices){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,KEY_ID + "=?",new String[]{String.valueOf(offlineInvoices.getId())});
        db.close();
    }

    //for Vending Machine
    public boolean checkGetTodayorNot(String uid, String datetime){
        String[] columns = { KEY_UID };

        SQLiteDatabase db = this.getReadableDatabase();
        String selection = KEY_UID + " = ?" + " AND " + KEY_DATE_TIME + " = ?";
        String[] selectionArgs = {uid,datetime};

        Cursor cursor = db.query(TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if(cursorCount > 0){
            return false;
        }else return true;
    }

    //Create database for record topup.
    //create database.
    private static final String TABLE_NAME_TOP_UP = "TopUpRecord";

    //create value of database
    private static final String KEY_ID_TOP_UP = "idtopup";
    private static final String KEY_AMOUNT_BEFORE_TOP_UP = "amountbefore";
    private static final String KEY_CARDID = "cardid";
    private static final String KEY_AMOUNT_AFTER_TOP_UP = "amountafter";
    private static final String KEY_DATE_TIME_TOP_UP = "datetimetopup";
    private static final String KEY_STATUS_TOP_UP = "statustopup";

    public void addRecordTopup(RecordTopUp recordTopUp){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CARDID,recordTopUp.getCardid());
        values.put(KEY_AMOUNT_BEFORE_TOP_UP,recordTopUp.getAmountbefore());
        values.put(KEY_AMOUNT_AFTER_TOP_UP,recordTopUp.getAmountafter());
        values.put(KEY_DATE_TIME_TOP_UP,recordTopUp.getDatetimetopup());
        values.put(KEY_STATUS_TOP_UP,recordTopUp.getStatustopup());

        db.insert(TABLE_NAME_TOP_UP, null,values);
        db.close();
    }

    public RecordTopUp getRecordTopUp(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME_TOP_UP,new String[]{ KEY_ID_TOP_UP , KEY_CARDID, KEY_AMOUNT_BEFORE_TOP_UP,KEY_AMOUNT_AFTER_TOP_UP,KEY_DATE_TIME_TOP_UP,KEY_STATUS_TOP_UP},KEY_ID_TOP_UP + "=?",
                new String[] {String.valueOf(id)},null,null,null,null);

        if(cursor != null)
            cursor.moveToFirst();

        RecordTopUp recordTopUp = new RecordTopUp(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getInt(5));

        cursor.close();
        db.close();
        return recordTopUp;
    }

    public int UpdateDBTopUp(RecordTopUp recordTopUp){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_CARDID,recordTopUp.getCardid());
        values.put(KEY_AMOUNT_BEFORE_TOP_UP,recordTopUp.getAmountbefore());
        values.put(KEY_AMOUNT_AFTER_TOP_UP,recordTopUp.getAmountafter());
        values.put(KEY_DATE_TIME_TOP_UP,recordTopUp.getDatetimetopup());
        values.put(KEY_STATUS_TOP_UP,recordTopUp.getStatustopup());

        return db.update(TABLE_NAME_TOP_UP,values,KEY_ID_TOP_UP + "=?",new String[]{String.valueOf(recordTopUp.getIdtopup())});
    }

    public List<RecordTopUp> getAllRecordTopUp(){
        List<RecordTopUp> recordTopUpList = new ArrayList<>();

        String selectQuerry = "SELECT * FROM " + TABLE_NAME_TOP_UP;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuerry,null);

        if(cursor.moveToFirst()){
            do {
                RecordTopUp recordTopUp = new RecordTopUp();
                recordTopUp.setIdtopup(cursor.getInt(0));
                recordTopUp.setCardid(cursor.getString(1));
                recordTopUp.setAmountbefore(cursor.getString(2));
                recordTopUp.setAmountafter(cursor.getString(3));
                recordTopUp.setDatetimetopup(cursor.getString(4));
                recordTopUp.setStatustopup(cursor.getInt(5));
                recordTopUpList.add(recordTopUp);
            } while (cursor.moveToNext());
        }
        return recordTopUpList;
    }

    public int getRecordTopUpCount(){
        String counQuery = "SELECT * FROM " + TABLE_NAME_TOP_UP;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(counQuery,null);
        cursor.close();
        return cursor.getCount();
    }

    public void delectRecordTopUp(RecordTopUp recordTopUp){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_TOP_UP,KEY_ID_TOP_UP + "=?",new String[]{String.valueOf(recordTopUp.getIdtopup())});
        db.close();
    }
}
