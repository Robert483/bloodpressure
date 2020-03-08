package com.comp3717.vu_gilpin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

public class AddNewReadingDialogFragment extends AppCompatDialogFragment {

    public interface DialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    DialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement DialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = requireActivity().getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.dialog_add_reading, null);

        builder.setView(R.layout.dialog_add_reading)
                .setTitle(R.string.add_reading_title)
                .setPositiveButton(R.string.newreading_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogPositiveClick(AddNewReadingDialogFragment.this);
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
