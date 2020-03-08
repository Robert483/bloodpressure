package com.comp3717.vu_gilpin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.FirebaseDatabase;

public class ReadingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_list);
        FirebaseDatabase.getInstance().getReference("test").setValue("MEOW");
    }

    public void onAddReadingButtonClick(View view) {
        Intent intent = getIntent();
        String userKey = intent.getStringExtra("userKey");
        userKey = "-user1";
        new AddNewReadingDialogFragment(userKey).show(getSupportFragmentManager(),
                "AddNewReadingDialogFragment");
    }
}
