package com.example.madclassproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    public Context LoginActivityContext;

    public EditText edit_email;
    public Button btn_submit;
    public TextView txt_login;
    public TextView txt_auth;
    //public TextView txt_auth_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginActivityContext = this;
        edit_email = findViewById(R.id.edit_email);
        btn_submit = findViewById(R.id.btn_submit);
        txt_login = findViewById(R.id.txt_login);
        txt_auth= findViewById(R.id.txt_auth);
        //txt_auth_ok = findViewById(R.id.txt_auth_ok);

        btn_submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RemoteContent MADMywork = new RemoteContent(LoginActivityContext);
                        String url = "http://mad.mywork.gr/generate_token.php?e=" + edit_email.getText().toString();
                        MADMywork.execute(url);
                    }
                }
        );
    }
}
