package com.comp3717.vu_gilpin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.comp3717.vu_gilpin.models.BloodPressureReading;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReadingListActivity extends AppCompatActivity {
    private ListView lvReadings;
    private List<BloodPressureReading> readingList;
    String userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_list);
        Intent intent = getIntent();
        LayoutInflater inflater = this.getLayoutInflater();

        this.userKey = intent.getStringExtra("userKey");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("readings"+userKey);
        this.lvReadings = findViewById(R.id.lstv_readings);
        this.readingList = new ArrayList<>();

        View footerView = inflater.inflate(R.layout.footer_view, null, false);
        lvReadings.addFooterView(footerView);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readingList.clear();
                for (DataSnapshot readingSnapshot: dataSnapshot.getChildren()) {
                    BloodPressureReading reading = readingSnapshot.getValue(BloodPressureReading.class);
                    readingList.add(reading);
                }
                ReadingListAdapter adapter = new ReadingListAdapter(ReadingListActivity.this, readingList);
                lvReadings.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void onAddReadingButtonClick(View view) {
        new AddNewReadingDialogFragment(userKey).show(getSupportFragmentManager(),
                "AddNewReadingDialogFragment");
    }

    public void onMonthToDate(View view) {
        Intent intent = new Intent(ReadingListActivity.this, MonthToDateAvgReadings.class);
        intent.putExtra("userKey", userKey);
        startActivity(intent);
    }
}
