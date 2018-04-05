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
import android.widget.Toast;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.app.MainMenuActivity;
import com.jade.walkinggroupbus.walkingschoolbus.model.MyRewards;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MyRewardsFragment extends Fragment{
    private static final String TAG = "MyRewardsFragment";

    private UserInfo userInfo;

    // Rewards singleton
    private MyRewards myRewards;
    private List<Boolean> obtainedRewards;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_rewards_tab, container, false);

        userInfo = UserInfo.userInfo();
        myRewards = MyRewards.MyRewards();

        setButtons(view);
        setUpRewardsDisplay(view);

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
        // TODO: if user has null custom json, GIVE THEM A BLANK ONE TO WORK WITH


        // pull my rewards info from userInfo and update MyRewards singleton
        String jsonString = userInfo.getCustomJson();
        myRewards.setRewardsWithJson(jsonString);
        obtainedRewards = myRewards.getObtainedRewards();

        //create adapter for spinner
        int size = obtainedRewards.size();
        ArrayList<String> items = new ArrayList<String>(size);

        // Parse collection for all obtained rewards
        for (int i = 0; i < size; i++){
            items.add(myRewards.getThemes().get(i).getThemeName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_template_spinner_item,
                items);


        // set Adapter for spinner
        Spinner recipientSpinner = (Spinner) view.findViewById(R.id.rewardSpinner);
        recipientSpinner.setAdapter(adapter);


        // set OnItemListener
        recipientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                if (myRewards.getObtainedRewards().get(position)) {
                    String themeName = myRewards.getThemes().get(position).getThemeName();
                    myRewards.setSelectedTheme(themeName);

                    // TODO: update server info on selected theme


                } else {
                    Toast.makeText(getActivity(), "You have not purchased this theme yet!", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            // Do nothing if nothing is selected
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
}
