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
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Intent intent = getIntent();

        String userKey = intent.getStringExtra("userKey");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("readings/" + userKey);

        TextView tvTitle = findViewById(R.id.txtv_header);
        SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
        tvTitle.setText(getString(R.string.report_header, formatter.format(new Date())));

        TextView tvName = findViewById(R.id.txtv_name);
        tvName.setText(intent.getStringExtra("userId"));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float systolicSum = 0;
                float diastolicSum = 0;
                int count = 0;

                for (DataSnapshot readingSnapshot : dataSnapshot.getChildren()) {
                    BloodPressureReading reading = readingSnapshot.getValue(BloodPressureReading.class);
                    if (reading == null) {
                        continue;
                    }

                    systolicSum += reading.getSystolicReading();
                    diastolicSum += reading.getDiastolicReading();
                    count++;
                }

                TextView tvSystolic = findViewById(R.id.txtv_systolic);
                TextView tvDiastolic = findViewById(R.id.txtv_diastolic);
                TextView tvCondition = findViewById(R.id.txtv_condition);

                if (count == 0) {
                    tvCondition.setText(R.string.app_na);
                    tvSystolic.setText(R.string.app_na);
                    tvDiastolic.setText(R.string.app_na);
                } else {
                    float avgSystolic = systolicSum / count;
                    tvSystolic.setText(getString(R.string.report_value, avgSystolic));

                    float avgDiastolic = diastolicSum / count;
                    tvDiastolic.setText(getString(R.string.report_value, avgDiastolic));

                    String condition = ConditionService.getCondition(
                            ReportActivity.this, avgSystolic, avgDiastolic);
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
