package com.example.user.myfinalprojectssc_ver1.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.user.myfinalprojectssc_ver1.API.CheckBalanceFromBE;
import com.example.user.myfinalprojectssc_ver1.API.DismissDialogForCheckBalance;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.DesfireDep;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.PocketData;
import com.example.user.myfinalprojectssc_ver1.ModelSQLite.RecordTopUp;
import com.example.user.myfinalprojectssc_ver1.NFCReader.NFCReader;
import com.example.user.myfinalprojectssc_ver1.Others.Constants;
import com.example.user.myfinalprojectssc_ver1.R;
import com.example.user.myfinalprojectssc_ver1.SQLiteDB.DatabaseHelperRecord;
import com.example.user.myfinalprojectssc_ver1.WorkerActivity.WorkerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class TopUpActivity extends AppCompatActivity {

    //init view
    public TextView topup_tenchuthe,topup_sothe,topup_sothe_title
            ,topup_tieudePayAcc,topup_soduPayAcc
            ,topup_tieudePocAccOnl,topup_soduPocAccOnl
            ,topup_tieudePocAccOff,topup_soduPocAccOff;

    ImageView back_topup_activity;

    private DatabaseHelperRecord databaseHelperRecord;
    private RecordTopUp recordTopUp;


    //Card infor
    private String PocketID;
    private Long PocketBalance;

    public boolean done1 = false, done2 = false;

    private String ActivityTaken;

    //dialog
    public SpotsDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);
        databaseHelperRecord = new DatabaseHelperRecord(this);
        recordTopUp = new RecordTopUp();
        SetInitView();
        try {
            GetDataFromActivity();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void GetDataFromActivity() throws JSONException {
        ActivityTaken = getIntent().getStringExtra("Activity");
        PocketID = getIntent().getStringExtra("PocketID");
        Log.d(Constants.TAG,PocketID);
        PocketBalance = getIntent().getLongExtra("PocketBalance",0);
        Log.d(Constants.TAG + "3",PocketBalance + "");
        switch (ActivityTaken){
            case "MainActivity":
                ShowDefault();
                break;
            case "TapActivity":
                balance();
                break;
            case "InputKeyboard":
                //do nothing
                ShowDefault();
                break;
        }

    }

    private void SetInitView() {
        //init text View:
        topup_tenchuthe = findViewById(R.id.topup_tenchuthe);
        topup_sothe = findViewById(R.id.topup_sothe);
        topup_sothe_title = findViewById(R.id.topup_sothe_title);

        topup_tieudePocAccOff = findViewById(R.id.topup_tieudePocAccOff);
        topup_soduPocAccOff = findViewById(R.id.topup_soduPocAccOff);

        topup_tieudePayAcc = findViewById(R.id.topup_tieudePayAcc);
        topup_soduPayAcc = findViewById(R.id.topup_soduPayAcc);

//        topup_tieudePocAccOnl = findViewById(R.id.topup_tieudePocAccOnl);
//        topup_soduPocAccOnl = findViewById(R.id.topup_soduPocAccOnl);

        //init image
        findViewById(R.id.back_topup_activity).setOnClickListener((v) -> back());

        //init button
        findViewById(R.id.btn_do_topup).setOnClickListener((v) -> topup());
        findViewById(R.id.btn_checkbalance).setOnClickListener((v) -> {
            try {
                balance();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        //dialog
        progressDialog = new SpotsDialog(this, R.style.Custom1);
    }

    private void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
//        startActivity(new Intent(TopUpActivity.this,MainActivity.class));
//        TopUpActivity.this.finish();
        super.onBackPressed();
    }

    private void balance() throws JSONException {
        //show dialog waiting
        progressDialog.show();
        //set View
        topup_sothe.setVisibility(View.GONE);
        topup_sothe_title.setVisibility(View.GONE);

//        topup_tieudePocAccOnl.setVisibility(View.GONE);
//        topup_soduPocAccOnl.setVisibility(View.GONE);

        topup_tieudePayAcc.setVisibility(View.VISIBLE);
        topup_soduPayAcc.setVisibility(View.VISIBLE);
        String s = "";
        int numLength = PocketBalance.toString().length();
        for (int i=0; i<numLength; i++) {
            if ((numLength-i)%3 == 0 && i != 0) {
                s += ".";
            }
            s += PocketBalance.toString().charAt(i);
        }
        topup_soduPocAccOff.setText(s);

        //call API
        CheckBalanceFromBE checkBalanceFromBE = new CheckBalanceFromBE();
        checkBalanceFromBE.execute(new CheckBalanceFromBE.CheckBalanceParams(this,1,MainActivity.TokenOfTopUp,CreateRequestCheckBalance(Constants.MTI_BALANCE,PocketID,Constants.ProCode_BALANCE_PAYACC),"",topup_soduPayAcc));
    }

    private void topup() {
        Bundle bundle = new Bundle();
        bundle.putString("PocketID",PocketID);
        Log.d(Constants.TAG,PocketID);
        bundle.putLong("PocketBalance",PocketBalance);
        Intent intent = new Intent(TopUpActivity.this,InputKeyboard.class);
        intent.putExtras(bundle);
        startActivity(intent);
        this.finish();
    }

    public void ShowDefault() {
        String s = "";
        int numLength = PocketBalance.toString().length();
        for (int i=0; i<numLength; i++) {
            if ((numLength-i)%3 == 0 && i != 0) {
                s += ".";
            }
            s += PocketBalance.toString().charAt(i);
        }
        topup_soduPocAccOff.setText(s);
        String s1 = "";
        int numLength1 = PocketID.length();
        for (int i=0; i<numLength1; i++) {
            if ((numLength1-i)%4 == 0 && i != 0) {
                s1 += " ";
            }
            s1 += PocketID.charAt(i);
        }
        topup_sothe.setText(s1);

        topup_tieudePayAcc.setVisibility(View.GONE);
        topup_soduPayAcc.setVisibility(View.GONE);

//        topup_tieudePocAccOnl.setVisibility(View.GONE);
//        topup_soduPocAccOnl.setVisibility(View.GONE);
    }

    private String CreateRequestCheckBalance(String mti, String cardNumber, String processCode) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mti",mti);
        jsonObject.put("cardNumber",cardNumber);
        jsonObject.put("processCode",processCode);
        jsonObject.put("amount","000000000000");
        jsonObject.put("traceNumber","222222");
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssZ");
        String currentDateandTime = sdf.format(new Date());
        jsonObject.put("time",currentDateandTime.substring(6,12));
        jsonObject.put("date",currentDateandTime.substring(2,6));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String terminalId = preferences.getString("terminalId", getString(R.string.terminalId));
        String merchantId = preferences.getString("merchantId",getString(R.string.merchantId));
        jsonObject.put("terminalId",terminalId);
        jsonObject.put("merchantId",merchantId);
        Log.d(Constants.TAG,jsonObject.toString());
        return jsonObject.toString();
    }

}
