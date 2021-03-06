package com.jade.walkinggroupbus.walkingschoolbus.app.rewardscentrefragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.app.MessageActivity;
import com.jade.walkinggroupbus.walkingschoolbus.model.Message;
import com.jade.walkinggroupbus.walkingschoolbus.model.MyRewards;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

// Code tutorial for android tabs obtained from: https://www.youtube.com/watch?v=bNpWGI_hGGg

public class LeaderboardFragment extends Fragment{
    private static final String TAG = "LeaderboardFragment";

    private WGServerProxy proxy;
    private SharedData sharedData;

    private List<UserInfo> allUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MyRewards myRewards = MyRewards.MyRewards();

        // set theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), myRewards.getSelectedThemeID());
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        View view = localInflater.inflate(R.layout.fragment_leaderboard_tab, container, false);

        // set background
        if (!myRewards.getSelectedTheme().equals("Default")
                && !myRewards.getSelectedTheme().equals("Dark")) {
            ImageView img = (ImageView) view.findViewById(R.id.imageView);
            img.setImageResource(myRewards.getSelectedImgID());
        } else {
            // if theme is default or dark, disable filter
            View filter = (View) view.findViewById(R.id.filter);
            filter.setVisibility(View.GONE);
        }

        sharedData = SharedData.getSharedData();
        String token = sharedData.getToken();

        // check if token is set properly
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken());
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }

        // get the data about all users and display their name and totalPointsEarned
        getAllUsersInformation();

        return view;
    }

    // get the data about all users and display their name and totalPointsEarned
    void getAllUsersInformation(){
        Call<List<UserInfo>> caller = proxy.getUsers();
        ProxyBuilder.callProxy(caller,returnedList->response(returnedList));
    }

    // set the LeaderBoard listView with returned data from the server
    private void response( List<UserInfo> returnedList) {
        allUser = returnedList;
        Collections.sort( allUser, new UserInfoComparatorByPoints());

        ArrayAdapter<UserInfo> adapterForUsersTotalPointsEarned = new UserPointsAdapter();

        ListView usersTotalPointsEarned = (ListView) getView()
                .findViewById(R.id.listView_UsersTotalPointsEarned);
        usersTotalPointsEarned.setAdapter( adapterForUsersTotalPointsEarned);

    }

    // set newly issued token to proxy and save it on sharedData singleton object
    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token);
        sharedData.setToken(token);
    }

    /*
    --------------------------------
    PRIVATE
    --------------------------------
    */
    // Adapter for List of Users
    private class UserPointsAdapter extends ArrayAdapter<UserInfo> {
        public UserPointsAdapter(){
            super(getActivity().getBaseContext(), // Context
                    R.layout.list_template_leaderboard, // Layout for iteam of list
                    allUser);   // List of items
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            // Make sure we have a view to work with
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater()
                        .inflate(R.layout.list_template_leaderboard, parent, false);
            }

            // Find the UserInfo to work with.
            UserInfo currentUser = allUser.get(position);

            // Fill the view
            // name:
            TextView nameText = (TextView) itemView.findViewById(R.id.item_name);
            nameText.setText(currentUser.getName());

            // totalPoints:
            TextView pointsText = (TextView) itemView.findViewById(R.id.item_totalPointsEarned);
            pointsText.setText(""+currentUser.getTotalPointsEarned());

            itemView.setBackgroundColor(Color.WHITE);
            return itemView;
        }
    }

    // custom Comparator for sorting Users by totalPointsEarned
    private class UserInfoComparatorByPoints implements Comparator<UserInfo> {
        @Override
        public int compare(UserInfo user1, UserInfo user2){
            // for reverse order (make the list descending order), times -1
            return -1*(user1.getTotalPointsEarned()-user2.getTotalPointsEarned());
        }
    }

}
