package com.comp3717.vu_gilpin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.comp3717.vu_gilpin.R;
import com.comp3717.vu_gilpin.ReportActivity;
import com.comp3717.vu_gilpin.models.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UserArrayAdapter extends ArrayAdapter<User> {
    private int resouce;

    public UserArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.resouce = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Context context = getContext();
        final User user = getItem(position);

        if (convertView == null) {
            convertView = View.inflate(context, resouce, null);
        }

        ImageButton button = convertView.findViewById(R.id.imgbtn_view_report);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    return;
                }

                Intent intent = new Intent(context, ReportActivity.class);
                intent.putExtra("userKey", user.getKey());
                intent.putExtra("userId", user.getUserId());
                context.startActivity(intent);
            }
        });

        if (user != null) {
            String internalId = parent.getContext().getString(R.string.main_internal_id, user.getKey());

            TextView textView = convertView.findViewById(R.id.txtv_userid);
            textView.setText(user.getUserId());
            textView = convertView.findViewById(R.id.txtv_userkey);
            textView.setText(internalId);
        }

        return convertView;
    }
}
