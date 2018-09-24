package com.example.user.myfinalprojectssc_ver1.SQLiteDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.user.myfinalprojectssc_ver1.ModelSQLite.TransactionModel;
import com.example.user.myfinalprojectssc_ver1.Others.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ho Dong Trieu on 09/14/2018
 */
public class DatabaseHelperTransaction extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "TransactionHistory";
    private static final String TABLE_NAME = "Record";

    //create value of database
    private static final String KEY_ID = "id";
    private static final String KEY_DATE_TIME = "date_time";
    private static final String KEY_TYPE = "type";
    private static final String KEY_CARD_NUMBER = "card_number";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_STATUS = "status";
    private static final String KEY_ID_INVOICES = "id_invoices";

    public DatabaseHelperTransaction(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_TRANSACTION = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DATE_TIME + " TEXT,"
                + KEY_TYPE + " INTEGER,"
                + KEY_CARD_NUMBER + " TEXT,"
                + KEY_AMOUNT + " TEXT,"
                + KEY_STATUS + " INTEGER,"
                + KEY_ID_INVOICES + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE_TRANSACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void AddTransaction(TransactionModel transactionModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE_TIME,transactionModel.getDate_time());
        values.put(KEY_TYPE,transactionModel.getType());
        values.put(KEY_CARD_NUMBER,transactionModel.getCard_number());
        values.put(KEY_AMOUNT,transactionModel.getAmount());
        values.put(KEY_STATUS,transactionModel.getStatus());
        values.put(KEY_ID_INVOICES,transactionModel.getId_invoices());
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public List<TransactionModel> GetTransactionTYPESALE_NOTSYNC(int status,int type){
        SQLiteDatabase db = this.getReadableDatabase();

        List<TransactionModel> transactionModelList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME,new String[] { KEY_ID ,
                KEY_DATE_TIME ,
                KEY_TYPE ,
                KEY_CARD_NUMBER ,
                KEY_AMOUNT ,
                KEY_STATUS,
                KEY_ID_INVOICES},KEY_STATUS + "=?" + "AND " + KEY_TYPE + "=?", new String[] { String.valueOf(status) , String.valueOf(type) },null,null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                TransactionModel transactionModel = new TransactionModel();
                transactionModel.setId(cursor.getInt(0));
                transactionModel.setDate_time(cursor.getString(1));
                transactionModel.setType(cursor.getInt(2));
                transactionModel.setCard_number(cursor.getString(3));
                transactionModel.setAmount(cursor.getString(4));
                transactionModel.setStatus(cursor.getInt(5));
                transactionModel.setId_invoices(cursor.getString(6));
                transactionModelList.add(transactionModel);
            } while (cursor.moveToNext());
        }

        db.close();

        return transactionModelList;
    }

    public List<TransactionModel> GetTransactionTYPESALE(int type){
        SQLiteDatabase db = this.getReadableDatabase();

        List<TransactionModel> transactionModelList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME,new String[] { KEY_ID ,
                KEY_DATE_TIME ,
                KEY_TYPE ,
                KEY_CARD_NUMBER ,
                KEY_AMOUNT ,
                KEY_STATUS,
                KEY_ID_INVOICES},KEY_TYPE + "=?", new String[] { String.valueOf(type) },null,null,null,null);
        Log.d(Constants.TAG,cursor.getCount()+ " ");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                TransactionModel transactionModel = new TransactionModel();
                transactionModel.setId(cursor.getInt(0));
                transactionModel.setDate_time(cursor.getString(1));
                transactionModel.setType(cursor.getInt(2));
                transactionModel.setCard_number(cursor.getString(3));
                transactionModel.setAmount(cursor.getString(4));
                transactionModel.setStatus(cursor.getInt(5));
                transactionModel.setId_invoices(cursor.getString(6));
                transactionModelList.add(transactionModel);
            } while (cursor.moveToNext());
        }

        db.close();

        return transactionModelList;
    }

    public void UpdateTransaction(TransactionModel transactionModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DATE_TIME,transactionModel.getDate_time());
        contentValues.put(KEY_TYPE,transactionModel.getType());
        contentValues.put(KEY_CARD_NUMBER,transactionModel.getCard_number());
        contentValues.put(KEY_AMOUNT,transactionModel.getAmount());
        contentValues.put(KEY_STATUS,transactionModel.getStatus());
        contentValues.put(KEY_ID_INVOICES,transactionModel.getId_invoices());
        db.update(TABLE_NAME,contentValues,KEY_ID + "=?", new String[] {String.valueOf(transactionModel.getId())});
        db.close();
    }

    public void DelectRecordTransaction(TransactionModel transactionModel){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,KEY_ID + "=?",new String[]{String.valueOf(transactionModel.getId())});
        db.close();
    }
}
