package com.example.user.myfinalprojectssc_ver1.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.myfinalprojectssc_ver1.API.CheckAmountInputAndActionTAP;
import com.example.user.myfinalprojectssc_ver1.Others.Constants;
import com.example.user.myfinalprojectssc_ver1.Others.KeyboardView;
import com.example.user.myfinalprojectssc_ver1.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import dmax.dialog.SpotsDialog;

public class InputKeyboard extends AppCompatActivity {

    KeyboardView keyboardView;
    String PocketID;
    Long PocketBalance;
    public SpotsDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_keyboard);

        InitView();
        GetDataFromActivity();
    }

    private void GetDataFromActivity(){
        PocketID = getIntent().getStringExtra("PocketID");
        PocketBalance = getIntent().getLongExtra("PocketBalance",0);
    }

    private void InitView(){
        keyboardView = findViewById(R.id.keyboard);
        progressDialog = new SpotsDialog(this,R.style.Custom2);
        progressDialog.setMessage("Hệ thống đang xử lý");
        findViewById(R.id.btn_done).setOnClickListener((v)->GetAmountInput());
        findViewById(R.id.back_inputKeyboard).setOnClickListener((v) -> back());
    }

    public void back() {
        Intent intent = new Intent(this,TopUpActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("PocketID",PocketID);
        bundle.putLong("PocketBalance",PocketBalance);
        bundle.putString("Activity","InputKeyboard");
        intent.putExtras(bundle);
        startActivity(intent);
        this.finish();
    }

    private void GetAmountInput() {
        //Log.d("HDT",keyboardView.getInputText());
        if(keyboardView.getInputText().equals("")){
            Toast.makeText(this,"Mời bạn nhập số tiền cần nạp",Toast.LENGTH_SHORT).show();
        }else {
            if(Long.valueOf(keyboardView.getInputText()) > 0) {
                //GoToTapActivity();
                Log.d(Constants.TAG,"im go to check Top Up");
                progressDialog.show();
                CheckAmountInputAndActionTAP checkAmountInputAndActionTAP = new CheckAmountInputAndActionTAP();
                try {
                    checkAmountInputAndActionTAP.execute(new CheckAmountInputAndActionTAP.TopUpRequest(this,MainActivity.TokenOfTopUp,CreateRequestTopUptoPocket(PocketID,keyboardView.getInputText()),""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(this,"Mời bạn nhập số tiền cần nạp",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void GoToTapActivity(){
        Intent intent = new Intent(this, TapActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("PocketID", PocketID);
        bundle.putLong("PocketBalance", PocketBalance);
        bundle.putString("Amount", keyboardView.getInputText());
        intent.putExtras(bundle);
        startActivity(intent);
        this.finish();
    }

    private String CreateRequestTopUptoPocket(String cardNumber, String amount) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mti", Constants.MTI_TOPUP_SALE);
        jsonObject.put("cardNumber",cardNumber);
        jsonObject.put("processCode",Constants.ProCode_TOPUP_POCACC);
        jsonObject.put("amount",amount);
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
