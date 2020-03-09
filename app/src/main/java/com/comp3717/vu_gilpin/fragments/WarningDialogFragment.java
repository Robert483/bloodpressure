package com.comp3717.vu_gilpin.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;

import com.comp3717.vu_gilpin.R;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class WarningDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        String warning = getResources().getString(R.string.condition_warning);
        builder.setTitle(R.string.app_warning)
                .setMessage(Html.fromHtml(warning, Html.FROM_HTML_MODE_COMPACT))
                .setNegativeButton(R.string.app_dismiss, null);

        return builder.create();
    }
}
