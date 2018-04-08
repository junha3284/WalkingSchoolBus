package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.MyRewards;

/**
 * alert dialogue that confirms if user wants to leave their walk
 */
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

        MyRewards myRewards = MyRewards.MyRewards();


        // set background
        if (!myRewards.getSelectedTheme().equals("Default")
                && !myRewards.getSelectedTheme().equals("Dark")) {
            ImageView img = (ImageView) v.findViewById(R.id.imageView);
            img.setImageResource(myRewards.getSelectedImgID());

        } else {
            // if theme is default or dark, disable filter
            View filter = (View) v.findViewById(R.id.filter);
            filter.setVisibility(View.GONE);
        }

        if (myRewards.getSelectedTheme().equals("Dark")) {
            v.setBackgroundColor(getResources().getColor(R.color.darkBackground));

            TextView tv = (TextView) v.findViewById(R.id.text_end_walk);

            tv.setTextColor(getResources().getColor(R.color.darkTextColor));
        }
        if (myRewards.getSelectedTheme().equals("Fire")) {
            TextView tv = (TextView) v.findViewById(R.id.text_end_walk);

            tv.setTextColor(Color.WHITE);
        }

        // Build the alert dialog
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.yes, listener)
                .setNegativeButton(android.R.string.no, listener)
                .create();
    }
}
