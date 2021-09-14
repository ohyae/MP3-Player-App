package com.example.madclassproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.Nullable;

import android.content.ContentValues;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.content.Context;

//Author: Hyae Cinth Ochotorena
//Major: MSc in Smart Cities and Communities
//Course: Mobile Application Development

public class DBHandler extends SQLiteOpenHelper {

    //public Context DBHandlerContext;
    private static final String tables = "tables";
    private static final String products = "products";
    SQLiteDatabase myDB;

    public DBHandler(@Nullable Context context) {
        super(context, "shopDB", null, 2);
        myDB = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase myDB) {
        //Create Tables table

        myDB.execSQL("CREATE TABLE IF NOT EXISTS " +
                "tables " +
                "(table_id integer primary key, " +
                "table_status integer)");
        //Create Products table
        myDB.execSQL("CREATE TABLE IF NOT EXISTS " +
                "products " +
                "(product_id integer primary key, " +
                "product_title text not null, " +
                "product_price real)");
        //Delete Old records
        myDB.delete(tables, null, null);
        myDB.delete(products, null, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase myDB, int oldVersion, int newVersion) {
        myDB.execSQL("DROP TABLE IF EXISTS "+tables);
        myDB.execSQL("DROP TABLE IF EXISTS "+products);
        onCreate(myDB);
    }

    public void insertToTables(String id, String status) {
        ContentValues cv1 = new ContentValues();
        cv1.put("table_id", id);
        cv1.put("table_status", status);
        this.myDB.insert(tables, null, cv1);
    }

    public void insertToProducts(String id, String title, String price) {
        ContentValues cv2 = new ContentValues();
        cv2.put("product_id", id);
        cv2.put("product_title", title);
        cv2.put("product_price", price);
        this.myDB.insert(products, null, cv2);
    }

    public void updateTable(int id, int stat) {
        String query = "UPDATE tables SET table_status = "+stat+" WHERE table_id = \""+ id +"\"";
        myDB.execSQL(query);
    }

    public  Cursor getTables(){
        myDB = this.getReadableDatabase();
        Cursor c1 = myDB.rawQuery(" SELECT * FROM " + tables, null);
        return c1;

    }
    public  Cursor getProducts() {
        myDB = this.getReadableDatabase();
        Cursor c2 = myDB.rawQuery(" SELECT * FROM " + products, null);
        return c2;

    }
}