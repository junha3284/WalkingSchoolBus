package com.jade.walkinggroupbus.walkingschoolbus.rewardscentrefragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.app.MainMenuActivity;
import com.jade.walkinggroupbus.walkingschoolbus.model.MyRewards;

import java.util.List;

/**
 * Created by Richard Wong on 2018-04-01.
 */

public class MyRewardsFragment extends Fragment{
    private static final String TAG = "MyRewardsFragment";
    // Rewards singleton
    private MyRewards myRewards;
    private List<Boolean> obtainedRewards;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_rewards_tab, container, false);

        setButtons(view);

        // TODO this function currently breaks the build, most likely due to uninitialized arrays in the rewards
        //setUpRewardsDisplay(view);

        return view;
    }

    private void setButtons(View view) {
        // Sets the theme
        Button setThemeBtn = (Button) view.findViewById(R.id.RCA_button_set);
        setThemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle theme setting here
            }
        });

        // Allows the user to preview a theme
        Button previewBtn = (Button) view.findViewById(R.id.RCA_button_preview);
        previewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean preview = true;
                Intent intent = MainMenuActivity.makeIntent(getActivity(), preview);
                startActivity(intent);
            }
        });
    }

    private void setUpRewardsDisplay(View view) {
        obtainedRewards = myRewards.getObtainedRewards();
        setSpinner(view);
    }

    // Set up the spinner
    private void setSpinner(View view) {
        //create adapter for spinner
        int size = obtainedRewards.size();
        String[] items = new String[size + 1];

        // Parse collection for all obtained rewards
        items[0] = "Default - Light";
        for(int i=0; i < size; i++){
            items[i+1] = String.valueOf(myRewards.checkObtainedRewardsByIndex(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_template_spinner_item, items);

        // set Adapter for spinner
        Spinner recipientSpinner = (Spinner) view.findViewById(R.id.spinner_recipient);
        recipientSpinner.setAdapter(adapter);

        // set OnItemListener
        recipientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // For each of the objects in the spinner
                // TODO Display all the obtained rewards here in the spinner
                if( position == 0) {

                }
            }

            // Do nothing if nothing is selected
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
}
