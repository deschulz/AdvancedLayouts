package com.introtoandroid.advancedlayouts;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class BasicLayoutActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        // This is what lets us add actions to it, like UpEnabled
        setSupportActionBar(toolbar);
        // this should do something more than "crash"
        assert getSupportActionBar() != null;
        // This enables the "up" button (left arrow)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}