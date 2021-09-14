package com.example.madclassproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public Context MainActivityContext;
    public static String token = "430021";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivityContext = this;
        Toast.makeText(MainActivityContext, R.string.authenticating, Toast.LENGTH_SHORT).show();

        RemoteContent MADMywork = new RemoteContent(MainActivityContext);
        String url = "http://mad.mywork.gr/authenticate.php?t=" + token;
        MADMywork.execute(url);
    }
}