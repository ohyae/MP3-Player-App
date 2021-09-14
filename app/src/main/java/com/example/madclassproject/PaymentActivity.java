package com.example.madclassproject;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.LinkedList;

public class PaymentActivity extends AppCompatActivity {

    public Context PaymentActivityContext;
    //public String token = MainActivity.token;
    public TextView tv_id, tv_cost, tv_paid, tv_bal;
    public EditText et_bal;
    public Button btn_pay;
    public String myResponse;
    public RecyclerView mRecyclerView;
    LinkedList<Product> ProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //Locators
        PaymentActivityContext = this;
        tv_id = findViewById(R.id.tv_id);
        tv_cost = findViewById(R.id.tv_cost);
        tv_paid = findViewById(R.id.tv_paid);
        tv_bal = findViewById(R.id.tv_bal);
        et_bal = findViewById(R.id.et_bal);
        btn_pay = findViewById(R.id.btn_pay);
        ProductList = new LinkedList<Product>();

        //Fetch values
        Bundle b = getIntent().getExtras();
        final String tid = b.getString("tid");
        tv_id.setText(tid);

        //Contact CTower to Get Orders
        String url = "http://mad.mywork.gr/get_order.php?t=430021&tid=TAB_ID";
        String newurl = url.replaceAll("TAB_ID", tid);
        CTower MADMywork = new CTower(PaymentActivityContext);
        MADMywork.execute(newurl);

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable etbal = et_bal.getText();
                String amt = etbal.toString();
                String tvbal = tv_bal.getText().toString();
                if (Float.parseFloat(tvbal) < Float.parseFloat(amt) || (Float.parseFloat(amt)) == 0) {
                    Toast.makeText(PaymentActivity.this, "Please enter a valid amount.", Toast.LENGTH_SHORT).show();
                } else {
                    String urlPay = "http://mad.mywork.gr/send_payment.php?t=430021&tid=TAB_ID&a=AMOUNT, ";
                    String newurlPay = urlPay.replaceAll("TAB_ID", tid);
                    String newurlAmt = newurlPay.replaceAll("AMOUNT", amt);
                    CTower MADMywork = new CTower(PaymentActivityContext);
                    MADMywork.execute(newurlAmt);
                }
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.product_recycler);
        ProductListAdapter mAdapter = new ProductListAdapter(PaymentActivityContext, ProductList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(PaymentActivityContext));
    }

}
