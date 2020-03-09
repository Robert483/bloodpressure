package com.comp3717.vu_gilpin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.comp3717.vu_gilpin.adapters.ReadingAdapter;
import com.comp3717.vu_gilpin.fragments.NewReadingDialogFragment;
import com.comp3717.vu_gilpin.fragments.WarningDialogFragment;
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

public class ReadingListActivity extends AppCompatActivity implements NewReadingDialogFragment.DialogListener {
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

        this.userKey = intent.getStringExtra("userKey");
        this.userId = intent.getStringExtra("userId");
        databaseReference = FirebaseDatabase.getInstance().getReference("readings/" + userKey);
        this.lvReadings = findViewById(R.id.lstv_readings);
        this.readingList = new ArrayList<>();

        setTitle(getString(R.string.readinglist_label, userId));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readingList.clear();
                for (DataSnapshot readingSnapshot : dataSnapshot.getChildren()) {
                    BloodPressureReading reading = readingSnapshot.getValue(BloodPressureReading.class);
                    if (reading == null) {
                        continue;
                    }

                    reading.setReadingKey(readingSnapshot.getKey());
                    readingList.add(reading);
                }

                ReadingAdapter adapter = new ReadingAdapter(ReadingListActivity.this, readingList, databaseReference);
                lvReadings.setAdapter(adapter);
                lvReadings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(ReadingListActivity.this, ReadingDetailsActivity.class);
                        intent.putExtra("userKey", userKey);
                        intent.putExtra("userId", userId);
                        intent.putExtra("readingKey", readingList.get((int) id).getReadingKey());
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
        if (dialog.getDialog() == null) {
            return;
        }

        EditText editTextSystolic = dialog.getDialog().findViewById(R.id.systolicReading);
        EditText editTextDiastolic = dialog.getDialog().findViewById(R.id.diastolicReading);

        // get input from EditText
        String systolic = editTextSystolic.getText().toString();
        String diastolic = editTextDiastolic.getText().toString();

        if (systolic.isEmpty() || diastolic.isEmpty()) {
            // do nothing
            Toast.makeText(ReadingListActivity.this, R.string.readinglist_empty_fields, Toast.LENGTH_LONG).show();
        } else {
            // add to firebase
            BloodPressureReading bloodPressureReading = new BloodPressureReading();
            bloodPressureReading.setSystolicReading(Integer.parseInt(systolic));
            bloodPressureReading.setDiastolicReading(Integer.parseInt(diastolic));
            bloodPressureReading.setReadingDate(new Date());
            String readingId = databaseReference.push().getKey();
            if (readingId == null) {
                return;
            }

            // Notify user of success or fail
            Task<Void> setValueTask = databaseReference.child(readingId)
                    .setValue(bloodPressureReading);
            setValueTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ReadingListActivity.this, R.string.readinglist_added, Toast.LENGTH_LONG).show();
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
                new WarningDialogFragment().show(
                        getSupportFragmentManager(), "WarningDialogFragment");
            }
        }
    }

    public void onAddReadingButtonClick(View view) {
        new NewReadingDialogFragment().show(getSupportFragmentManager(),
                "NewReadingDialogFragment");
    }
}
