package com.comp3717.vu_gilpin;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.comp3717.vu_gilpin.models.BloodPressureReading;

import java.text.SimpleDateFormat;
import java.util.List;

public class ReadingListAdapter extends ArrayAdapter<BloodPressureReading> {
    private Activity context;
    private List<BloodPressureReading> readingList;

    public ReadingListAdapter(Activity context, List<BloodPressureReading> readingList) {
        super(context, R.layout.reading_list, readingList);
        this.context = context;
        this.readingList = readingList;
    }

    public ReadingListAdapter(Context context, int resource,
                              List<BloodPressureReading> objects,
                              Activity context1, List<BloodPressureReading> readingList) {
        super(context, resource, objects);
        this.context = context1;
        this.readingList = readingList;
    }

    @NonNull
    @Override
    public View getView(int position, View contextView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.reading_list, null, true);

        TextView tvCondition = listViewItem.findViewById(R.id.condition);
        TextView tvDate = listViewItem.findViewById(R.id.date);

        BloodPressureReading reading = readingList.get(position);
        tvCondition.setText(reading.getCondition().name());

        SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        tvDate.setText(formatter.format(reading.getReadingDate()));

        return listViewItem;
    }
}
