package com.comp3717.vu_gilpin.adapters;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.comp3717.vu_gilpin.R;
import com.comp3717.vu_gilpin.models.BloodPressureReading;
import com.comp3717.vu_gilpin.services.ConditionService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.util.List;

public class ReadingAdapter extends ArrayAdapter<BloodPressureReading> {
    private List<BloodPressureReading> readingList;
    private DatabaseReference listRef;

    public ReadingAdapter(Context context, List<BloodPressureReading> readingList, DatabaseReference listRef) {
        super(context, R.layout.item_reading, readingList);
        this.readingList = readingList;
        this.listRef = listRef;
    }

    @NonNull
    @Override
    public View getView(int position, View contextView, @NonNull ViewGroup parent) {
        final Context context = parent.getContext();
        final BloodPressureReading reading = readingList.get(position);

        if (contextView == null) {
            contextView = View.inflate(getContext(), R.layout.item_reading, null);
        }

        ImageButton button = contextView.findViewById(R.id.imgbtn_delete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task<Void> removeValuetask = listRef.child(reading.getReadingKey()).removeValue();
                removeValuetask.addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, R.string.readingdetails_deleted, Toast.LENGTH_SHORT).show();
                    }
                });
                removeValuetask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        String condition = ConditionService.getCondition(
                context, reading.getSystolicReading(), reading.getDiastolicReading());
        String date = DateFormat.getDateTimeInstance().format(reading.getReadingDate());
        String summary = context.getString(R.string.readinglist_summary,
                reading.getSystolicReading(), reading.getDiastolicReading(), date);

        TextView textView = contextView.findViewById(R.id.txtv_condition_readings);
        textView.setText(Html.fromHtml(condition, Html.FROM_HTML_MODE_COMPACT));

        textView = contextView.findViewById(R.id.txtv_date);
        textView.setText(Html.fromHtml(summary, Html.FROM_HTML_MODE_COMPACT));

        return contextView;
    }
}
