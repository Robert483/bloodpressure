package com.comp3717.vu_gilpin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comp3717.vu_gilpin.models.BloodPressureReading;
import com.comp3717.vu_gilpin.services.ConditionService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;

public class ReadingDetailsActivity extends AppCompatActivity {
    private DatabaseReference readingRef;
    private BloodPressureReading reading;

    private int validateReading(int resId) {
        EditText editText = findViewById(resId);
        String value = editText.getText().toString();
        if (TextUtils.isEmpty(value)) {
            editText.setText("0");
            return 0;
        }
        return Integer.parseInt(value);
    }

    private void updateReading() {
        reading.setSystolicReading(validateReading(R.id.edttxt_systolic_reading));
        reading.setDiastolicReading(validateReading(R.id.edttxt_diastolic_reading));
    }

    private void validateCondition() {
        int systolicReading = reading.getSystolicReading();
        int diastolicReading = reading.getDiastolicReading();
        String condition = getString(R.string.readingdetails_condition,
                ConditionService.getCondition(this, systolicReading, diastolicReading));
        TextView textView = findViewById(R.id.txtv_condition);
        textView.setText(Html.fromHtml(condition, Html.FROM_HTML_MODE_COMPACT));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_details);

        String userKey = getIntent().getStringExtra("userKey");
        String readingKey = getIntent().getStringExtra("readingKey");
        String path = String.join("/", "readings", userKey, readingKey);
        readingRef = FirebaseDatabase.getInstance().getReference(path);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateReading();
                validateCondition();
            }
        };
        EditText editText = findViewById(R.id.edttxt_systolic_reading);
        editText.addTextChangedListener(watcher);
        editText = findViewById(R.id.edttxt_diastolic_reading);
        editText.addTextChangedListener(watcher);

        readingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reading = dataSnapshot.getValue(BloodPressureReading.class);
                if (reading == null) {
                    return;
                }

                String userId = getIntent().getStringExtra("userId");
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
                validateCondition();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReadingDetailsActivity.this, R.string.app_operation_cancelled, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void onUpdateButtonClick(View view) {
        updateReading();
        Task<Void> setValuetask = readingRef.setValue(reading);
        setValuetask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ReadingDetailsActivity.this, R.string.readingdetails_updated, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void onDeleteButtonClick(View view) {
        Task<Void> removeValuetask = readingRef.removeValue();
        removeValuetask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ReadingDetailsActivity.this, R.string.readingdetails_deleted, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
