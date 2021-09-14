package com.example.madclassproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.LinkedList;


public class OrderActivity extends AppCompatActivity {


    public Context OrderActivityContext;
    DBHandler DB;
    SQLiteDatabase myDB;
    private static TextView tv_total, tv_tabID;
    public  ImageButton btn_order;
    private static String oc; //order contents for the new url
    public RecyclerView mRecyclerView;
    private LinkedList<Product> ProductList;
    public static int[] Orders = new int[16];

    String url = "http://mad.mywork.gr/send_order.php?t=430021&tid=TABLE_ID&oc=CONTENTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //Locators
        OrderActivityContext = this;
        tv_total = findViewById(R.id.txt_total);
        btn_order = findViewById(R.id.btn_order);
        tv_tabID = findViewById(R.id.tv_tabID);

        //Fetch values
        Bundle b = getIntent().getExtras();
        final String tid = b.getString("tid");
        tv_tabID.setText(tid);

        ProductList = new LinkedList<Product>();
        DB = new DBHandler(this);
        myDB = DB.getReadableDatabase();
        Cursor c = DB.getProducts();
        c.moveToFirst();
        if (c != null) {
            do {
                int product_id = c.getInt(0);
                String product_title = c.getString(1);
                String product_price = c.getString(2);
                ProductList.add(new Product(product_id, product_title, Float.parseFloat(product_price), 0)
                );
            }
            while (c.moveToNext());
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.db_product_recycler);
        ProductListAdapter mAdapter = new ProductListAdapter(OrderActivityContext, ProductList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(OrderActivityContext));

        btn_order.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        float total = Float.parseFloat(tv_total.getText().toString());
                        System.out.println(total);
                        if (total == 0) {
                            Toast.makeText(OrderActivity.this, "No order exists", Toast.LENGTH_SHORT).show();
                        } else {
                            String newUrl = url.replaceAll("TABLE_ID", tid);
                            String orderUrl = newUrl.replaceAll("CONTENTS", oc);
                            CTower MADMywork = new CTower(OrderActivityContext);
                            MADMywork.execute(orderUrl);
                        }
                    }
                }
        );
    }


    public static void addItem(float totalOrder, int qty, int itemID) {
        float itemPrice = Float.parseFloat(tv_total.getText().toString());
        itemPrice = itemPrice + totalOrder;
        tv_total.setText(Float.toString(itemPrice));
        Orders[itemID-1] = qty;
        oc = "";
        for (int i=0; i<Orders.length; i++) {
            if (Orders[i]!=0)
                oc = oc + (i+1) + "," + Orders[i] + ";";
        }
        System.out.println(oc);
    }

    public static void subItem(float totalOrder, int qty1, int itemID1) {
        float itemPrice1 = Float.parseFloat(tv_total.getText().toString());
        itemPrice1 = itemPrice1 - totalOrder;
        tv_total.setText(Float.toString(itemPrice1));
        Orders[itemID1-1] = qty1;
        oc = "";
        for (int i=0; i<Orders.length; i++) {
            if (Orders[i]!=0)
                oc = oc + (i+1) + "," + Orders[i] + ";";
        }
        System.out.println(oc);
    }



}