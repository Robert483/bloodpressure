package com.comp3717.vu_gilpin.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.comp3717.vu_gilpin.R;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

public class NewReadingDialogFragment extends AppCompatDialogFragment {

    public interface DialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }

    private DialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement DialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setView(R.layout.dialog_new_reading)
                .setTitle(R.string.readinglist_new_reading)
                .setPositiveButton(R.string.app_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogPositiveClick(NewReadingDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.app_cancel, null);

        return builder.create();
    }
}
