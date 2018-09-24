package com.example.user.myfinalprojectssc_ver1.API;

import android.annotation.SuppressLint;
import android.nfc.tech.IsoDep;
import android.os.AsyncTask;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;

import com.example.user.myfinalprojectssc_ver1.Activities.TapActivity;
import com.example.user.myfinalprojectssc_ver1.NFCReader.NFCReader;

/**
 * Created by Ho Dong Trieu on 09/17/2018
 */
public class NotificationGuideText extends AsyncTask<Void, Void, String> {

    private NFCReader nfcReader;
    @SuppressLint("StaticFieldLeak")
    private TextView tv;
    TapActivity activity;

    public NotificationGuideText(TextView tv, NFCReader nfcReader,TapActivity activity){
        this.nfcReader = nfcReader;
        this.tv = tv;
        this.activity = activity;
    }

    @Override
    protected String doInBackground(Void... voids) {
        //AnimationFadeOutForText();
        while (true){
            if(!nfcReader.card.isConnected()){
                return "";
            }
        }
    }

    @Override
    protected void onPostExecute(String s) {
        tv.clearAnimation();
    }

    public void AnimationFadeOutForText(){
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(1000);
        fadeOut.setRepeatCount(Animation.INFINITE);
        fadeOut.setRepeatMode(Animation.RESTART);
        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(fadeOut);
        tv.startAnimation(animation);
    }
}
