package com.example.madclassproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.telecom.Call;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

//Author: Hyae Cinth Ochotorena
//Major: MSc in Smart Cities and Communites
//Course: Mobile Application Development

public class CTower extends AsyncTask<String, Void, String[]> {

    private Context CallingContext;
    private Document doc;

    CTower(Context ct) {
        super();
        CallingContext = ct;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        String url = strings[0];
        String[] response = {"", ""};
        try {
            doc = Jsoup.connect(url).get();
            Elements getStatus = doc.getElementsByTag("status");
            response[0] = getStatus.text();
            Elements getMessage = doc.getElementsByTag("msg");
            response[1] = getMessage.text();
        } catch (Exception e){
            System.out.println("Exception Catched: Java IO");
        }
        return response;
    }

    @Override
    protected void onPostExecute(String[] response) {

        DBHandler dB = new DBHandler(CallingContext);
        SQLiteDatabase myDB = dB.getReadableDatabase();

        if (response[0].contains("3-OK")) {

            //Assign Table IDs and Colors based on Table Status
            Elements tableID = doc.select("tables > id");
            Elements tableStatus = doc.select("tables > status");
            for (int i=0; i<tableID.size(); i++){
                int table_id = Integer.parseInt(tableID.get(i).text());
                int table_status = Integer.parseInt(tableStatus.get(i).text());
                ((TablesActivity) CallingContext).btns[i].setText(Integer.toString(table_id));
                if (table_status == 0) {
                    ((TablesActivity) CallingContext).btns[i].setBackgroundColor(Color.GREEN);
                } else if (table_status == 1) {
                    ((TablesActivity) CallingContext).btns[i].setBackgroundColor(Color.RED);
                }
            }

            //Insert Table values
            for (int i=0; i<tableID.size(); i++){
                String table_id = Integer.toString(Integer.parseInt(tableID.get(i).text()));
                String table_status = Integer.toString(Integer.parseInt(tableStatus.get(i).text()));
                dB.insertToTables(table_id, table_status);
            }
            //Insert Product values
            Elements productID = doc.select("product > id");
            Elements productTitle = doc.select("product > title");
            Elements productPrice = doc.select("product > price");
            for (int i=0; i<productID.size(); i++){
                String product_id = Integer.toString(Integer.parseInt(productID.get(i).text()));
                String product_title =  productTitle.get(i).text();
                String product_price = Float.toString(Float.parseFloat(productPrice.get(i).text()));
                dB.insertToProducts(product_id,product_title,product_price);
            }

        } else if (response[0].contains("4-FAIL")) {
            Toast.makeText(CallingContext, response[1], Toast.LENGTH_SHORT).show();

        } else if (response[0].contains("4-OK")){
            Elements tableID = doc.getElementsByTag("table_id");
            int tid = Integer.parseInt(tableID.text());
            int tstat=1;
            dB.updateTable(tid, tstat);
            Intent intent = new Intent(CallingContext, TablesActivity.class);
            CallingContext.startActivity(intent);
            ((OrderActivity) CallingContext).finish();

        } else if (response[0].contains("0-FAIL")||response[0].contains("5-FAIL")){
            Toast.makeText(CallingContext, response[1], Toast.LENGTH_SHORT).show();

        } else if (response[0].contains("5-OK")){
            //Fetch Orders
            Elements orderID = doc.select("product > id");
            Elements orderTitle = doc.select("product > title");
            Elements orderPrice = doc.select("product > price");
            Elements orderQty = doc.select("product > quantity");
            for (int i=0; i<orderID.size(); i++){
                int oid = Integer.parseInt(orderID.get(i).text());
                String otitle = orderTitle.get(i).text();
                Float oprice = Float.parseFloat(orderPrice.get(i).text());
                int oqty = Integer.parseInt(orderQty.get(i).text());
                ((PaymentActivity) CallingContext).ProductList.add( new Product(oid, otitle, oprice, oqty));
            }

            //Fetch Payment Summary (cost, paid, balance, Balance to be paid)
            Elements cost = doc.getElementsByTag("cost");
            Elements paid = doc.getElementsByTag("payment");
            Elements bal = doc.getElementsByTag("balance");
            ((PaymentActivity) CallingContext).tv_cost.setText(cost.text());
            ((PaymentActivity) CallingContext).tv_paid.setText(paid.text());
            ((PaymentActivity) CallingContext).tv_bal.setText(bal.text());
            ((PaymentActivity) CallingContext).et_bal.setText(bal.text());

        } else if (response[0].contains("6-FAIL")){
            Toast.makeText(CallingContext, response[1], Toast.LENGTH_SHORT).show();

        } else if (response[0].contains("6-OK")){
            Elements tableID = doc.getElementsByTag("table_id");
            int tid = Integer.parseInt(tableID.text());
            int tstat=0;
            Elements newBalance = doc.getElementsByTag("new_balance");
            Float newBal = Float.parseFloat(newBalance.text());
            if (newBal<0){
                Toast.makeText(CallingContext, "Error occurred.", Toast.LENGTH_SHORT).show();
            } else if (newBal>0){
                Toast.makeText(CallingContext, "Payment successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CallingContext, TablesActivity.class);
                CallingContext.startActivity(intent);
                ((PaymentActivity) CallingContext).finish();
            } else if (newBal==0){
                Toast.makeText(CallingContext, "Payment successful", Toast.LENGTH_SHORT).show();
                dB.updateTable(tid, tstat);
                //((PaymentActivity) CallingContext).ProductList.clear();
                Intent intent = new Intent(CallingContext, TablesActivity.class);
                CallingContext.startActivity(intent);
                ((PaymentActivity) CallingContext).finish();
            }
        }
    }
}
