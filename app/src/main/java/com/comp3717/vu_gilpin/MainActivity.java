package com.comp3717.vu_gilpin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onAddReadingButtonClick(View view) {
        Intent intent = new Intent(this, NewReadingActivity.class);
        startActivity(intent);
    }
}
