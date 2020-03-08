package com.comp3717.vu_gilpin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.comp3717.vu_gilpin.models.BloodPressureReading;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReadingListActivity extends AppCompatActivity
        implements AddNewReadingDialogFragment.DialogListener {
    private ListView lvReadings;
    private List<BloodPressureReading> readingList;
    private String userKey;
    private String userId;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_list);
        Intent intent = getIntent();
        LayoutInflater inflater = this.getLayoutInflater();

        this.userKey = intent.getStringExtra("userKey");
        this.userId = intent.getStringExtra("userId");
        databaseReference = FirebaseDatabase.getInstance().getReference("readings/"+userKey);
        this.lvReadings = findViewById(R.id.lstv_readings);
        this.readingList = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readingList.clear();
                for (DataSnapshot readingSnapshot: dataSnapshot.getChildren()) {
                    BloodPressureReading reading = readingSnapshot.getValue(BloodPressureReading.class);
                    reading.setReadingKey(readingSnapshot.getKey());
                    readingList.add(reading);
                }
                ReadingListAdapter adapter = new ReadingListAdapter(ReadingListActivity.this, readingList);
                lvReadings.setAdapter(adapter);
                lvReadings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(ReadingListActivity.this, ReadingDetailsActivity.class);
                        intent.putExtra("userKey", userKey);
                        intent.putExtra("userId", userId);
                        intent.putExtra("readingKey", readingList.get((int)id).getReadingKey());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        EditText editTextSystolic = dialog.getDialog().findViewById(R.id.systolicReading);
        EditText editTextDiastolic = dialog.getDialog().findViewById(R.id.diastolicReading);

        // get input from EditText
        String systolic = editTextSystolic.getText().toString();
        String diastolic = editTextDiastolic.getText().toString();

        if (systolic.isEmpty() || diastolic.isEmpty()) {
            // do nothing
            Toast.makeText(ReadingListActivity.this, R.string.empty_field, Toast.LENGTH_LONG).show();
        } else {
            // add to firebase
            BloodPressureReading bloodPressureReading = new BloodPressureReading();
            bloodPressureReading.setSystolicReading(Integer.parseInt(systolic));
            bloodPressureReading.setDiastolicReading(Integer.parseInt(diastolic));
            bloodPressureReading.setReadingDate(new Date());
            String readingId = databaseReference.push().getKey();

            // Notify user of success or fail
            Task setValueTask = databaseReference.child(readingId)
                    .setValue(bloodPressureReading);
            setValueTask.addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast.makeText(ReadingListActivity.this, R.string.add_success, Toast.LENGTH_LONG).show();
                }
            });
            setValueTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ReadingListActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            if (bloodPressureReading.getSystolicReading() > 180
                    || bloodPressureReading.getDiastolicReading() > 120) {
                new WarningAlertDialogFragment().show(
                        getSupportFragmentManager(), "WarningAlertDialogFragment");
            }
        }
    }

    public void onAddReadingButtonClick(View view) {
        new AddNewReadingDialogFragment().show(getSupportFragmentManager(),
                "AddNewReadingDialogFragment");
    }

    public void onMonthToDate(View view) {
        Intent intent = new Intent(ReadingListActivity.this, MonthToDateAvgReadings.class);
        intent.putExtra("userKey", userKey);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }
}
