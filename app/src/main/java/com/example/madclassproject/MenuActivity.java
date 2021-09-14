package com.example.madclassproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {
    public Context MenuActivityContext;
    public TextView txt_welcome;
    public Button btn_menu, btn_shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Locators
        MenuActivityContext = this;
        Bundle b = getIntent().getExtras();
        txt_welcome = findViewById(R.id.txt_welcome);
        txt_welcome.setText(b.getString("msg"));
        btn_menu = findViewById(R.id.btn_menu);
        btn_shop = findViewById(R.id.btn_shop);

        //User is redirected to JukeboxActivity when he clicks on Menu button
        btn_menu.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MenuActivity.this, JukeboxActivity.class));
                    }
                }
        );

        //User is redirected to TablesActivity
        btn_shop.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MenuActivity.this, TablesActivity.class));
                    }
                }
        );





    }
}
