package com.comp3717.vu_gilpin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.comp3717.vu_gilpin.models.BloodPressureReading;
import com.comp3717.vu_gilpin.services.ConditionService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        databaseReference = FirebaseDatabase.getInstance().getReference("readings/"+userKey);

        tvTitle = findViewById(R.id.mtd_title);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(" MMM yyyy");
        String dateFormat = formatter.format(date);
        tvTitle.append(dateFormat);

        tvName = findViewById(R.id.name);
        tvName.setText(intent.getStringExtra("userId"));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float systolicSum = 0;
                float diastolicSum = 0;
                int count = 0;

                for (DataSnapshot readingSnapshot: dataSnapshot.getChildren()) {
                    BloodPressureReading reading = readingSnapshot.getValue(BloodPressureReading.class);
                    systolicSum += reading.getSystolicReading();
                    diastolicSum += reading.getDiastolicReading();
                    count++;
                }
                tvSystolic = findViewById(R.id.mtd_systolic);
                tvDiastolic = findViewById(R.id.mtd_diastolic);
                tvCondition = findViewById(R.id.mtd_condition);

                if (count == 0) {
                    tvCondition.setText(R.string.na);
                    tvSystolic.setText(R.string.na);
                    tvDiastolic.setText(R.string.na);
                } else {
                    float avgSystolic = systolicSum/count;
                    tvSystolic.setText(String.format("%.2f", avgSystolic));

                    float avgDiastolic = diastolicSum/count;
                    tvDiastolic.setText(String.format("%.2f", avgDiastolic));

                    String condition = ConditionService.getCondition(
                            MonthToDateAvgReadings.this, avgSystolic, avgDiastolic);
                    tvCondition.setText(Html.fromHtml(condition, Html.FROM_HTML_MODE_COMPACT));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // nothing happens
            }
        });
    }
}
