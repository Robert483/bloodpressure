package com.comp3717.vu_gilpin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ReadingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_list);
    }

    public void onAddReadingButtonClick(View view) {
        // REMOVE THIS WHEN MERGE
        Intent intent = new Intent(this, ReadingDetailsActivity.class);
        startActivity(intent);
    }
}
