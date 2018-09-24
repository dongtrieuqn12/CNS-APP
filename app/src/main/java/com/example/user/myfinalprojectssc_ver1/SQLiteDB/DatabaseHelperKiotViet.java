package com.example.user.myfinalprojectssc_ver1.SQLiteDB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.user.myfinalprojectssc_ver1.ModelSQLite.KiotVietModel;
import com.example.user.myfinalprojectssc_ver1.Others.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ho Dong Trieu on 09/19/2018
 */
public class DatabaseHelperKiotViet extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "KiotViet";
    private static final String TABLE_NAME = "Categories";

    //create value of database
    private static final String KEY_ID = "id";
    private static final String KEY_ID_KIOT = "id_kiot";
    private static final String KEY_CODE = "code";
    private static final String KEY_FULL_NAME = "fullName";
    private static final String KEY_CATEGORYID = "categoryId";
    private static final String KEY_ALLOWSSALE = "allowsSale";
    private static final String KEY_BASE_PRICE = "basePrice";
    private static final String KEY_IS_ACTIVE = "isActive";
    private static final String KEY_PRODUCT_ID = "productId";
    private static final String KEY_PRODUCT_CODE = "productCode";
    private static final String KEY_PRODUCT_NAME = "productName";
    private static final String KEY_BRANCH_ID = "branchId";
    private static final String KEY_ON_HAND = "onHand";
    private static final String KEY_IMAGES = "images";

    public DatabaseHelperKiotViet(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_KIOT = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ID_KIOT + " LONG,"
                + KEY_CODE + " TEXT,"
                + KEY_FULL_NAME + " TEXT,"
                + KEY_CATEGORYID + " LONG,"
                + KEY_ALLOWSSALE + " INTEGER,"
                + KEY_BASE_PRICE + " TEXT,"
                + KEY_IS_ACTIVE + " INTEGER,"
                + KEY_PRODUCT_ID + " LONG,"
                + KEY_PRODUCT_CODE + " TEXT,"
                + KEY_PRODUCT_NAME + " TEXT,"
                + KEY_BRANCH_ID + " LONG,"
                + KEY_ON_HAND + " INTEGER,"
                + KEY_IMAGES + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE_KIOT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void AddCatelogies(KiotVietModel kiotVietModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_KIOT,kiotVietModel.getId_kiot());
        values.put(KEY_CODE,kiotVietModel.getCode());
        values.put(KEY_FULL_NAME,kiotVietModel.getFullName());
        values.put(KEY_CATEGORYID,kiotVietModel.getCategoryId());
        values.put(KEY_ALLOWSSALE,kiotVietModel.getAllowsSale());
        values.put(KEY_BASE_PRICE,kiotVietModel.getBasePrice());
        values.put(KEY_IS_ACTIVE,kiotVietModel.getIsActive());
        values.put(KEY_PRODUCT_ID,kiotVietModel.getProductId());
        values.put(KEY_PRODUCT_CODE,kiotVietModel.getProductCode());
        values.put(KEY_PRODUCT_NAME,kiotVietModel.getProductName());
        values.put(KEY_BRANCH_ID,kiotVietModel.getBranchId());
        values.put(KEY_ON_HAND,kiotVietModel.getOnHand());
        values.put(KEY_IMAGES,kiotVietModel.getImages());
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+TABLE_NAME;
        db.execSQL(clearDBQuery);
    }

    public List<KiotVietModel> GetAllCatelogies_Is_Active(int isActive, int isAlowsSale){
        List<KiotVietModel> kiotVietModelList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.query(TABLE_NAME,new String [] { KEY_ID,
                KEY_ID_KIOT ,
                KEY_CODE ,
                KEY_FULL_NAME,
                KEY_CATEGORYID,
                KEY_ALLOWSSALE,
                KEY_BASE_PRICE,
                KEY_IS_ACTIVE,
                KEY_PRODUCT_ID,
                KEY_PRODUCT_CODE,
                KEY_PRODUCT_NAME,
                KEY_BRANCH_ID,
                KEY_ON_HAND,
                KEY_IMAGES } , KEY_IS_ACTIVE + "=?" + " AND " + KEY_ALLOWSSALE + "=?", new String[] { String.valueOf(isActive) , String.valueOf(isAlowsSale) },null,null,null,null);


        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                KiotVietModel kiotVietModel = new KiotVietModel();
                kiotVietModel.setId(cursor.getInt(0));
                kiotVietModel.setId_kiot(cursor.getLong(1));
                kiotVietModel.setCode(cursor.getString(2));
                kiotVietModel.setFullName(cursor.getString(3));
                kiotVietModel.setCategoryId(cursor.getLong(4));
                kiotVietModel.setAllowsSale(cursor.getInt(5));
                kiotVietModel.setBasePrice(cursor.getString(6));
                kiotVietModel.setIsActive(cursor.getInt(7));
                kiotVietModel.setProductId(cursor.getLong(8));
                kiotVietModel.setProductCode(cursor.getString(9));
                kiotVietModel.setProductName(cursor.getString(10));
                kiotVietModel.setBranchId(cursor.getLong(11));
                kiotVietModel.setOnHand(cursor.getInt(12));
                kiotVietModel.setImages(cursor.getString(13));
                kiotVietModelList.add(kiotVietModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return kiotVietModelList;
    }

    public int GetCount(){
        String counQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(counQuery,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public KiotVietModel QueryCategory(long productId){
        KiotVietModel kiotVietModel = new KiotVietModel();
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.query(TABLE_NAME,new String [] { KEY_ID,
                KEY_ID_KIOT ,
                KEY_CODE ,
                KEY_FULL_NAME,
                KEY_CATEGORYID,
                KEY_ALLOWSSALE,
                KEY_BASE_PRICE,
                KEY_IS_ACTIVE,
                KEY_PRODUCT_ID,
                KEY_PRODUCT_CODE,
                KEY_PRODUCT_NAME,
                KEY_BRANCH_ID,
                KEY_ON_HAND,
                KEY_IMAGES } , KEY_PRODUCT_ID + "=?" , new String[] { String.valueOf(productId) },null,null,null,null);


        if (cursor != null) {
            cursor.moveToFirst();
            do {
                kiotVietModel.setId(cursor.getInt(0));
                kiotVietModel.setId_kiot(cursor.getLong(1));
                kiotVietModel.setCode(cursor.getString(2));
                kiotVietModel.setFullName(cursor.getString(3));
                kiotVietModel.setCategoryId(cursor.getLong(4));
                kiotVietModel.setAllowsSale(cursor.getInt(5));
                kiotVietModel.setBasePrice(cursor.getString(6));
                kiotVietModel.setIsActive(cursor.getInt(7));
                kiotVietModel.setProductId(cursor.getLong(8));
                kiotVietModel.setProductCode(cursor.getString(9));
                kiotVietModel.setProductName(cursor.getString(10));
                kiotVietModel.setBranchId(cursor.getLong(11));
                kiotVietModel.setOnHand(cursor.getInt(12));
                kiotVietModel.setImages(cursor.getString(13));
            } while (cursor.moveToNext());
        }
        db.close();
        return kiotVietModel;
    }

    public void Update(KiotVietModel kiotVietModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_KIOT,kiotVietModel.getId_kiot());
        values.put(KEY_CODE,kiotVietModel.getCode());
        values.put(KEY_FULL_NAME,kiotVietModel.getFullName());
        values.put(KEY_CATEGORYID,kiotVietModel.getCategoryId());
        values.put(KEY_ALLOWSSALE,kiotVietModel.getAllowsSale());
        values.put(KEY_BASE_PRICE,kiotVietModel.getBasePrice());
        values.put(KEY_IS_ACTIVE,kiotVietModel.getIsActive());
        values.put(KEY_PRODUCT_ID,kiotVietModel.getProductId());
        values.put(KEY_PRODUCT_CODE,kiotVietModel.getProductCode());
        values.put(KEY_PRODUCT_NAME,kiotVietModel.getProductName());
        values.put(KEY_BRANCH_ID,kiotVietModel.getBranchId());
        values.put(KEY_ON_HAND,kiotVietModel.getOnHand());
        values.put(KEY_IMAGES,kiotVietModel.getImages());

        db.update(TABLE_NAME,values,KEY_ID + "=?",new String[]{String.valueOf(kiotVietModel.getId())});
        db.close();
    }
}
