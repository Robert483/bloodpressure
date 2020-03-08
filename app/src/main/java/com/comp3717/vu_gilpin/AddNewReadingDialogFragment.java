package com.comp3717.vu_gilpin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.comp3717.vu_gilpin.models.BloodPressureReading;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class AddNewReadingDialogFragment extends AppCompatDialogFragment {
    private DatabaseReference databaseReference;
    private EditText editTextSystolic;
    private EditText editTextDiastolic;

    public AddNewReadingDialogFragment(String userKey) {
        this.databaseReference = FirebaseDatabase.getInstance().getReference("readings/" + userKey);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_reading, null);
        editTextSystolic = dialogView.findViewById(R.id.systolicReading);
        editTextDiastolic = dialogView.findViewById(R.id.diastolicReading);


        builder.setView(dialogView)
                .setTitle(R.string.add_reading_title)
                .setPositiveButton(R.string.newreading_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseDatabase.getInstance().getReference("test").setValue("MEOW");
                        // get input from EditText
                        String systolic = editTextSystolic.getText().toString();
                        String diastolic = editTextDiastolic.getText().toString();

                        if (systolic.isEmpty() || diastolic.isEmpty()) {
                            // do nothing
                            Toast.makeText(getContext(), "Empty Field", Toast.LENGTH_LONG).show();
                        } else {
                            // add to firebase
                            BloodPressureReading bloodPressureReading = new BloodPressureReading();
                            bloodPressureReading.setSystolicReading(Integer.parseInt(systolic));
                            bloodPressureReading.setDiastolicReading(Integer.parseInt(diastolic));
                            bloodPressureReading.setReadingDate(new Date());
                            String readingId = databaseReference.push().getKey();
                            Task setValueTask = databaseReference.child(readingId)
                                    .setValue(bloodPressureReading);
                            setValueTask.addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(getContext(), "MEOW", Toast.LENGTH_LONG).show();
                                }
                            });
                            setValueTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        AddNewReadingDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
