package com.comp3717.vu_gilpin.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.comp3717.vu_gilpin.R;
import com.comp3717.vu_gilpin.models.User;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UserIDArrayAdapter extends ArrayAdapter<User> {
    private int resouce;

    public UserIDArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.resouce = resource;
    }

    public UserIDArrayAdapter(@NonNull Context context, int resource, @NonNull User[] objects) {
        super(context, resource, objects);
        this.resouce = resource;
    }

    public UserIDArrayAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
        super(context, resource, objects);
        this.resouce = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(getContext(), resouce, null);
        }

        User user = getItem(position);
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
