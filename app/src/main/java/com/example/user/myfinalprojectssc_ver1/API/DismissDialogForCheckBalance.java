package com.example.user.myfinalprojectssc_ver1.API;

import android.os.AsyncTask;

import com.example.user.myfinalprojectssc_ver1.Activities.TopUpActivity;

/**
 * Created by Ho Dong Trieu on 09/04/2018
 */
public class DismissDialogForCheckBalance extends AsyncTask<TopUpActivity,Void,TopUpActivity> {

    @Override
    protected TopUpActivity doInBackground(TopUpActivity... topUpActivities) {
        TopUpActivity activity = topUpActivities[0];
        boolean done = false;
        while (!done){
            if(activity.done1 && activity.done2){
                activity.progressDialog.dismiss();
                done = true;
            }
        }
        activity.done1 = false;
        activity.done2 = false;
        return topUpActivities[0];
    }
}
