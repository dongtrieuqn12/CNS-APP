package com.example.user.myfinalprojectssc_ver1.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myfinalprojectssc_ver1.API.CreditToCard;
import com.example.user.myfinalprojectssc_ver1.API.NotificationGuideText;
import com.example.user.myfinalprojectssc_ver1.API.TopUpRequestToBE;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.CardData;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.DesfireDep;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.PocketData;
import com.example.user.myfinalprojectssc_ver1.ModelSQLite.RecordTopUp;
import com.example.user.myfinalprojectssc_ver1.ModelSQLite.TransactionModel;
import com.example.user.myfinalprojectssc_ver1.NFCReader.NFCReader;
import com.example.user.myfinalprojectssc_ver1.Others.Constants;
import com.example.user.myfinalprojectssc_ver1.R;
import com.example.user.myfinalprojectssc_ver1.SQLiteDB.DatabaseHelperRecord;
import com.example.user.myfinalprojectssc_ver1.SQLiteDB.DatabaseHelperTransaction;
import com.example.user.myfinalprojectssc_ver1.WorkerActivity.WorkerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TapActivity extends WorkerActivity {

    private ImageView wave,card_visa;
    TextView count_timeout;
    public TextView guide_text;
    public CountDownTimer countDownTimer;
    int count = 0;
    public static boolean check = false;
    public String PocketID;
    public Long PocketBalance;

    DesfireDep desfireDep;
    public String amount_4byte,amout;

    public DatabaseHelperRecord databaseHelperRecord;
    public RecordTopUp recordTopUp;
    public DatabaseHelperTransaction databaseHelperTransaction;
    public TransactionModel transactionModel;
    public boolean isEndProcess = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap);

        Toast.makeText(this,"Tap thẻ vào thiết bị",Toast.LENGTH_LONG).show();
        databaseHelperRecord = new DatabaseHelperRecord(this);
        recordTopUp = new RecordTopUp();
        recordTopUp.setStatustopup(0);
        databaseHelperTransaction = new DatabaseHelperTransaction(this);
        transactionModel = new TransactionModel();
        transactionModel.setType(Constants.TOPUP_TYPE);
        transactionModel.setStatus(Constants.IS_NOT_SYNC);

        SetInitView();
        SetAnimation(1500);
        SetAnimationForCard1();
        count_timeout.setVisibility(View.VISIBLE);
        CountTimeOut();
    }

    private void GetDataFromActivity(){
        PocketID = getIntent().getStringExtra("PocketID");
        Log.d(Constants.TAG,PocketID);
        PocketBalance = getIntent().getLongExtra("PocketBalance",0);
        amout = getIntent().getStringExtra("Amount");
        transactionModel.setAmount(amout);
        amount_4byte = convertToHexStringBigEndian(Long.toHexString(Long.valueOf(amout)));
    }

    private String convertToHexStringBigEndian(String value){
        String temp = "";
        int count = 8 - value.length();
        for(int i = count ; i > 0 ; i--){
            value = "0" + value;
        }
        if(value.length() % 2 != 0){
            value = "0" + value;
        }
        for(int i = value.length() ; i >= 2 ; i -= 2){
            temp += value.substring(i-2,i);
        }
        return temp;
    }

    private void SetInitView(){
        wave = findViewById(R.id.image);
        card_visa = findViewById(R.id.card_visa);
        count_timeout = findViewById(R.id.count_timeout);
        count_timeout.setVisibility(View.GONE);
        guide_text = findViewById(R.id.guide_text);
        GetDataFromActivity();
    }

    private void SetAnimation(int time){
        Animation sizingAnimation = AnimationUtils.loadAnimation(TapActivity.this, R.anim.anim_nfc_tap);
        sizingAnimation.setDuration(time);
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(time);
        fadeOut.setRepeatCount(Animation.INFINITE);
        fadeOut.setRepeatMode(Animation.RESTART);
        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(sizingAnimation);
        animation.addAnimation(fadeOut);
        wave.startAnimation(animation);
    }

    private void SetAnimationForCard1(){
        Animation sizingAnimation = AnimationUtils.loadAnimation(TapActivity.this, R.anim.anim_for_card_out);
        AnimationSet animationSet = new AnimationSet(true);
        Animation fadeOut = new AlphaAnimation(0, 1);
        fadeOut.setDuration(1000);
        animationSet.addAnimation(sizingAnimation);
        animationSet.addAnimation(fadeOut);
        card_visa.startAnimation(animationSet);
    }

    private void CountTimeOut(){
        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                count_timeout.setText(millisUntilFinished / 1000 + "");
                //here you can have your logic to set text to edittext
            }
            public void onFinish() {
                if(!check){
                    Intent intent = new Intent(TapActivity.this,TopUpActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("PocketID",PocketID);
                    bundle.putLong("PocketBalance",PocketBalance);
                    bundle.putString("Activity","TapActivity");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    TapActivity.this.finish();
                }else {
                    //do nothing
                    check = false;
                }
            }

        };
        countDownTimer.start();
    }

    @Override
    public void onBackPressed() {
        count++;
        if (count == 1){
            Toast.makeText(TapActivity.this,"Nhấn một lần nữa để thoát",Toast.LENGTH_SHORT).show();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    count = 0;
                }
            },3000);
        }else {
            count = 0;
            Bundle bundle = new Bundle();
            bundle.putString("PocketID",PocketID);
            bundle.putLong("PocketBalance",PocketBalance);
            bundle.putString("Activity","TapActivity");
            Intent intent = new Intent(this,InputKeyboard.class);
            intent.putExtras(bundle);
            startActivity(intent);
            this.finish();
            countDownTimer.cancel();
        }
    }

    @Override
    protected void functionProcess(NFCReader nfcReader) {
        runOnUiThread(() -> {
            AnimationFadeOutForText();
            NotificationGuideText notificationGuideText = new NotificationGuideText(guide_text,nfcReader,TapActivity.this);
            notificationGuideText.execute();
        });
        CreditToCard creditToCard = new CreditToCard();
        creditToCard.execute(new CreditToCard.CreditCardParam(this,desfireDep,nfcReader,5));
    }

    private String CreateRequestTopUptoPocket(String mti, String cardNumber, String processCode, String amount) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mti",mti);
        jsonObject.put("cardNumber",cardNumber);
        jsonObject.put("processCode",processCode);
        jsonObject.put("amount",amount);
        jsonObject.put("traceNumber","222222");
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssZ");
        String currentDateandTime = sdf.format(new Date());
        jsonObject.put("time",currentDateandTime.substring(6,12));
        jsonObject.put("date",currentDateandTime.substring(2,6));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String terminalId = preferences.getString("terminalId", "30100000");
        String merchantId = preferences.getString("merchantId","111111111111111");
        jsonObject.put("terminalId",terminalId);
        jsonObject.put("merchantId",merchantId);
        return jsonObject.toString();
    }

    public void AnimationFadeOutForText(){
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(1000);
        fadeOut.setRepeatCount(Animation.INFINITE);
        fadeOut.setRepeatMode(Animation.RESTART);
        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(fadeOut);
        guide_text.startAnimation(animation);
    }
}
