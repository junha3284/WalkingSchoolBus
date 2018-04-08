package com.jade.walkinggroupbus.walkingschoolbus.rewardscentrefragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.jade.walkinggroupbus.walkingschoolbus.app.PreviewThemeActivity;
import com.jade.walkinggroupbus.walkingschoolbus.model.MyRewards;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class MyRewardsFragment extends Fragment{
    private static final String TAG = "MyRewardsFragment";

    private UserInfo userInfo;

    private SharedData sharedData;
    private WGServerProxy proxy;

    // Rewards singleton
    private MyRewards myRewards;
    private List<Boolean> obtainedRewards;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_rewards_tab, container, false);

        sharedData = SharedData.getSharedData();
        String token = sharedData.getToken();

        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken());
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }

        userInfo = UserInfo.userInfo();
        myRewards = MyRewards.MyRewards();

        setButtons(view);
        setUpRewardsDisplay(view);

        return view;
    }


    private void setButtons(View view) {
        // Allows the user to preview a theme
        Button previewBtn = (Button) view.findViewById(R.id.preview);
        previewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = PreviewThemeActivity.makeIntent(getActivity());
                startActivity(intent);
            }
        });
    }


    private void setUpRewardsDisplay(View view) {
        obtainedRewards = myRewards.getObtainedRewards();
        setSpinners(view);
    }

    // Set up the spinner
    private void setSpinners(View view) {

        // pull my rewards info from userInfo and update MyRewards singleton
        String jsonString = userInfo.getCustomJson();
        myRewards.setRewardsWithJson(jsonString);
        obtainedRewards = myRewards.getObtainedRewards();

        //create adapter for spinner
        int size = obtainedRewards.size();
        ArrayList<String> items = new ArrayList<String>(size);

        // need an empty (null) selection so theme doesnt switch when u just enter the activity
        items.add("Select Here");

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

                if (position == 0) {
                    // do nothing
                } else if (myRewards.getObtainedRewards().get(position - 1)) {
                    String themeName = myRewards.getThemes().get(position - 1).getThemeName();
                    myRewards.setSelectedTheme(themeName);

                    // update server info on selected theme
                    String myRewardsJsonString = myRewards.convertToJsonString();
                    userInfo.setCustomJson(myRewardsJsonString);

                    Call<UserInfo> editUser = proxy.editUser(userInfo.getId(), userInfo);
                    ProxyBuilder.callProxy(editUser, returnedUser -> response(returnedUser));

                } else {
                    Toast.makeText(getActivity(),
                            "You have not purchased this theme yet!",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }

            // Do nothing if nothing is selected
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


        // setup preview spinner
        Spinner previewSpinner = (Spinner) view.findViewById(R.id.previewSpinner);
        previewSpinner.setAdapter(adapter);

        // on click listener for spinner
        previewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                if (position != 0) {
                    // sets preview theme in myRewards
                    String previewTheme = myRewards.getThemes().get(position - 1).getThemeName();
                    myRewards.setPreviewTheme(previewTheme);
                }
            }

            // Do nothing if nothing is selected
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

    }

    private void response(UserInfo returnedUser) {
        Log.w(TAG, "    User: " + returnedUser);
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token);
        sharedData.setToken(token);
    }
}
