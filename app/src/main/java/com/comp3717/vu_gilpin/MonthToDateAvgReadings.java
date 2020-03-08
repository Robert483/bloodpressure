package com.comp3717.vu_gilpin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.comp3717.vu_gilpin.models.BloodPressureReading;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MonthToDateAvgReadings extends AppCompatActivity {
    DatabaseReference databaseReference;
    String userKey;
    TextView tvTitle;
    TextView tvName;
    TextView tvSystolic;
    TextView tvDiastolic;
    TextView tvCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_to_date_avg_readings);
        Intent intent = getIntent();

        this.userKey = intent.getStringExtra("userKey");
        databaseReference = FirebaseDatabase.getInstance().getReference("readings"+userKey);

        tvTitle = findViewById(R.id.mtd_title);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy");
        String dateFormat = formatter.format(date);
        tvTitle.append(dateFormat);

        tvSystolic = findViewById(R.id.mtd_systolic);
        // loop through readings and calculate average systolic

        tvDiastolic = findViewById(R.id.mtd_diastolic);
        // loop through readings and calculate average diastolic

        tvCondition = findViewById(R.id.mtd_condition);
        BloodPressureReading average = new BloodPressureReading();
        average.setSystolicReading(/*average systolic*/);
        average.setDiastolicReading(/*average diastolic*/);
        tvCondition.setText(average.getCondition());
    }
}
