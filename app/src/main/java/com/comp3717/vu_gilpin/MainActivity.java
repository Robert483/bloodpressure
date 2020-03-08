package com.comp3717.vu_gilpin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.comp3717.vu_gilpin.adapters.UserIDArrayAdapter;
import com.comp3717.vu_gilpin.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase.getInstance().getReference("test").setValue(new Date());

        final ArrayAdapter<User> adapter = new UserIDArrayAdapter(this, R.layout.item_userid);
        ListView listView = findViewById(R.id.lstv_users);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = adapter.getItem((int) id);
                if (user == null) {
                    return;
                }

                Intent intent = new Intent(MainActivity.this, ReadingListActivity.class);
                intent.putExtra("userId", user.getUserId());
                intent.putExtra("userKey", user.getKey());
                startActivity(intent);
            }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        user.setKey(userSnapshot.getKey());
                        adapter.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, R.string.app_operation_cancelled, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onAddUserButtonClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_new_user);
        builder.setMessage(R.string.main_new_user);
        builder.setPositiveButton(R.string.app_add, null);
        builder.setNegativeButton(R.string.app_cancel, null);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = dialog.findViewById(R.id.edttxt_user_id);
                if (textView == null) {
                    dialog.dismiss();
                    return;
                }

                if (TextUtils.isEmpty(textView.getText())) {
                    Toast.makeText(MainActivity.this, R.string.main_no_name, Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = new User(textView.getText().toString());
                String key = userRef.push().getKey();
                if (key == null) {
                    dialog.dismiss();
                    return;
                }

                Task<Void> setValueTask = userRef.child(key).setValue(user);
                setValueTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, R.string.main_user_added, Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
            }
        });
    }
}
