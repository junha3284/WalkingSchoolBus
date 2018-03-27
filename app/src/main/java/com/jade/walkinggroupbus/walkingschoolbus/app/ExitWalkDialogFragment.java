package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.jade.walkinggroupbus.walkingschoolbus.R;

public class ExitWalkDialogFragment extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Create the view
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_exit_walk, null);

        // Create button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch(i) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // End walk
                        getActivity().finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        // Do nothing
                        break;
                }
            }
        };

        // Build the alert dialog
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.yes, listener)
                .setNegativeButton(android.R.string.no, listener)
                .create();
    }
}
