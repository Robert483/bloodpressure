package com.comp3717.vu_gilpin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.comp3717.vu_gilpin.models.BloodPressureReading;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;

public class ReadingDetailsActivity extends AppCompatActivity {
    private DatabaseReference readingRef;
    private BloodPressureReading reading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_details);

        String userKey = "-user1";// getIntent().getStringExtra("userKey");
        String readingKey = "-reading1";// getIntent().getStringExtra("readingKey");
        String path = String.join("/", "readings", userKey, readingKey);
        readingRef = FirebaseDatabase.getInstance().getReference(path);

        readingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reading = dataSnapshot.getValue(BloodPressureReading.class);
                if (reading == null) {
                    return;
                }

                String userId = "Someone"; // getIntent().getStringExtra("userId");
                String date = DateFormat.getDateInstance().format(reading.getReadingDate());
                String time = DateFormat.getTimeInstance().format(reading.getReadingDate());
                String systolicReading = reading.getSystolicReading().toString();
                String diastolicReading = reading.getDiastolicReading().toString();

                EditText editText = findViewById(R.id.edttxt_user_id);
                editText.setText(userId);

                editText = findViewById(R.id.edttxt_reading_date);
                editText.setText(date);

                editText = findViewById(R.id.edttxt_reading_time);
                editText.setText(time);

                editText = findViewById(R.id.edttxt_systolic_reading);
                editText.setText(systolicReading);

                editText = findViewById(R.id.edttxt_diastolic_reading);
                editText.setText(diastolicReading);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReadingDetailsActivity.this, R.string.app_operation_cancelled, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
