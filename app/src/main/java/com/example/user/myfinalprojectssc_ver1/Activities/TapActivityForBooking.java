package com.example.user.myfinalprojectssc_ver1.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myfinalprojectssc_ver1.API.SaleTransection;
import com.example.user.myfinalprojectssc_ver1.API.WriteInvoices;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.DesfireDep;
import com.example.user.myfinalprojectssc_ver1.ModelSQLite.TransactionModel;
import com.example.user.myfinalprojectssc_ver1.NFCReader.NFCReader;
import com.example.user.myfinalprojectssc_ver1.Others.Constants;
import com.example.user.myfinalprojectssc_ver1.R;
import com.example.user.myfinalprojectssc_ver1.SQLiteDB.DatabaseHelperTransaction;
import com.example.user.myfinalprojectssc_ver1.WorkerActivity.WorkerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ho Dong Trieu on 09/11/2018
 */
public class TapActivityForBooking extends WorkerActivity {

    private ImageView wave,card_visa;
    TextView count_timeout;
    public CountDownTimer countDownTimer;
    int count = 0;
    public static boolean check = false;
    public String PocketID;
    public Long PocketBalance;

    public String TotalPayment;
    public String InvoicesId;
    public String InvoicesCode;

    DesfireDep desfireDep;
    boolean tap = false;

    public DatabaseHelperTransaction databaseHelperTransaction;
    public TransactionModel transactionModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap);

        databaseHelperTransaction = new DatabaseHelperTransaction(this);
        transactionModel = new TransactionModel();
        transactionModel.setType(Constants.SALE_TYPE);
        transactionModel.setStatus(Constants.IS_NOT_SYNC);

        Toast.makeText(this,"Tap thẻ vào thiết bị",Toast.LENGTH_LONG).show();
        SetInitView();

        SetAnimation(1500);
        SetAnimationForCard1();
        count_timeout.setVisibility(View.VISIBLE);
        CountTimeOut();
    }

    private void SetInitView(){
        wave = findViewById(R.id.image);
        card_visa = findViewById(R.id.card_visa);
        count_timeout = findViewById(R.id.count_timeout);
        count_timeout.setVisibility(View.GONE);
        GetDataFromActivity();
    }

    private void GetDataFromActivity() {
        TotalPayment = getIntent().getStringExtra("TotalPayment");
        InvoicesId = getIntent().getStringExtra("InvoicesId");
        InvoicesCode = getIntent().getStringExtra("InvoicesCode");
        transactionModel.setId_invoices(InvoicesId);
        transactionModel.setAmount(TotalPayment);
        Log.d(Constants.TAG,TotalPayment);
        Log.d(Constants.TAG,InvoicesId);
    }

    private void SetAnimation(int time){
        Animation sizingAnimation = AnimationUtils.loadAnimation(TapActivityForBooking.this, R.anim.anim_nfc_tap);
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
        Animation sizingAnimation = AnimationUtils.loadAnimation(TapActivityForBooking.this, R.anim.anim_for_card_out);
        AnimationSet animationSet = new AnimationSet(true);
        Animation fadeOut = new AlphaAnimation(0, 1);
        fadeOut.setDuration(1000);
        animationSet.addAnimation(sizingAnimation);
        animationSet.addAnimation(fadeOut);
        card_visa.startAnimation(animationSet);
    }

    private void CountTimeOut(){
        countDownTimer = new CountDownTimer(30000, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                count_timeout.setText(millisUntilFinished / 1000 + "");
                //here you can have your logic to set text to edittext
            }
            public void onFinish() {
                if(!check){
                    Intent intent = new Intent(TapActivityForBooking.this,PurchaseActivity.class);
                    startActivity(intent);
                    TapActivityForBooking.this.finish();
                }else {
                    //do nothing
                    check = false;
                }
            }

        };
        countDownTimer.start();
        tap = true;
    }

    @Override
    public void onBackPressed() {
        count++;
        if (count == 1){
            Toast.makeText(TapActivityForBooking.this,"Nhấn một lần nữa để thoát",Toast.LENGTH_SHORT).show();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    count = 0;
                }
            },3000);
        }else {
            count = 0;
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            this.finish();
            countDownTimer.cancel();
        }
    }

    @Override
    protected void functionProcess(NFCReader nfcReader) {
        if(tap) {
            WriteInvoices writeInvoices = new WriteInvoices();
            writeInvoices.execute(new WriteInvoices.WriteInvoicesParam(this, nfcReader, desfireDep, 5));
        }
    }

    public void SendRequestSale(String CardNumber){
        try {
            SaleTransection saleTransection = new SaleTransection(CreateRequestSale(CardNumber,TotalPayment),MainActivity.TokenOfTopUp,this);
            saleTransection.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String CreateRequestSale(String cardNumber,String amount) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mti",Constants.MTI_TOPUP_SALE);
        jsonObject.put("cardNumber",cardNumber);
        jsonObject.put("processCode",Constants.ProCode_SALTE);
        jsonObject.put("amount",amount);
        Random rand = new Random();
        int n = rand.nextInt(899) + 100;
        int n1 = rand.nextInt(899) + 100;
        String traceNumber = String.valueOf(n) + String .valueOf(n1);
        jsonObject.put("traceNumber",traceNumber);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssZ");
        String currentDateandTime = sdf.format(new Date());
        jsonObject.put("time",currentDateandTime.substring(6,12));
        jsonObject.put("date",currentDateandTime.substring(2,6));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String terminalId = preferences.getString("terminalId", getString(R.string.terminalId));
        String merchantId = preferences.getString("merchantId",getString(R.string.merchantId));
        jsonObject.put("terminalId",terminalId);
        jsonObject.put("merchantId",merchantId);
        return jsonObject.toString();
    }


}
