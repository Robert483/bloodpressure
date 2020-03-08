package com.comp3717.vu_gilpin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;

import androidx.fragment.app.DialogFragment;

public class WarningAlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String warning = getResources().getString(R.string.warning);
        builder.setMessage(Html.fromHtml(warning, Html.FROM_HTML_MODE_COMPACT))
                .setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled dialog
                    }
                });

        return builder.create();
    }
}
