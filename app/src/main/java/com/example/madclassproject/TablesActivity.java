package com.example.madclassproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

//Author: Hyae Cinth Ochotorena
//Major: MSc in Smart Cities and Communities
//Course: Mobile Application Development

public class TablesActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    public Context TablesActivityContext;
    public static SQLiteDatabase myDB;
    public String token = MainActivity.token;
    private static final String tables = "tables";
    private static final String products = "products";
    public Button[] btns = new Button[9];
    //public ArrayList<Integer> tableList;

    @Override
    public void onClick(View v) {

        /*DBHandler DB = new DBHandler(getApplicationContext());
        SQLiteDatabase myDB= DB.getReadableDatabase();
        Cursor c = DB.getTables();
        c.moveToFirst();
        if (c != null) {
            // Loop through all Results
            do {
                int table_status = c.getInt(1);
                tableList.add(table_status);
            } while (c.moveToNext());
        }*/

        String tid ="";
        for (int i=0; i<btns.length; i++) {
            if (btns[i].getId() == v.getId()) {
                tid = btns[i].getText().toString();
                break;
            }
        }
        Intent intent = new Intent(TablesActivityContext, OrderActivity.class);
        intent.putExtra("tid", tid);
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
        String tid = "";
        //int tstat =0;
        int colorid = 0;
        for (int i=0; i<btns.length; i++) {
            if (btns[i].getId() == v.getId()) {
                tid = btns[i].getText().toString();
                colorid = ((ColorDrawable)btns[i].getBackground()).getColor();
                //tstat = tableList.get(i);
                if (colorid==0xFF00FF00){
                    Toast.makeText(TablesActivityContext, "No unpaid orders exists.", Toast.LENGTH_SHORT).show();
                } else if (colorid==0xFFFF0000){
                    Intent intent = new Intent(TablesActivity.this, PaymentActivity.class);
                    intent.putExtra("tid", tid);
                    //intent.putExtra("tstat", tstat);
                    startActivity(intent);
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);

        //Locators
        TablesActivityContext = this;
        btns[0] = findViewById(R.id.btn1);
        btns[1] = findViewById(R.id.btn2);
        btns[2] = findViewById(R.id.btn3);
        btns[3] = findViewById(R.id.btn4);
        btns[4] = findViewById(R.id.btn5);
        btns[5] = findViewById(R.id.btn6);
        btns[6] = findViewById(R.id.btn7);
        btns[7] = findViewById(R.id.btn8);
        btns[8] = findViewById(R.id.btn9);

        //myDB = this.openOrCreateDatabase("shopDB", MODE_PRIVATE, null);
        //createDB();

        //Create DB
        DBHandler dB = new DBHandler(this);
        SQLiteDatabase myDB = dB.getReadableDatabase();
        dB.onCreate(myDB);

        //Contact CTower to get Table and Product Details
        CTower MADMywork = new CTower(TablesActivityContext);
        String url = "http://mad.mywork.gr/get_coffee_data.php?t=" + token;
        MADMywork.execute(url);

        //Implement Click and LongClick to all buttons
        for (int i = 0; i < btns.length; i++) {
            btns[i].setOnClickListener(this);
            btns[i].setOnLongClickListener(this);
        }

    }
}