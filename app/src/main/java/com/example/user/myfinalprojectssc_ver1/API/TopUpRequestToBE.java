package com.example.user.myfinalprojectssc_ver1.API;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myfinalprojectssc_ver1.Activities.PurchaseActivity;
import com.example.user.myfinalprojectssc_ver1.Activities.TapActivity;
import com.example.user.myfinalprojectssc_ver1.Activities.TopUpActivity;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.CardData;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.DesfireDep;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.PocketData;
import com.example.user.myfinalprojectssc_ver1.NFCReader.NFCReader;
import com.example.user.myfinalprojectssc_ver1.Others.Constants;
import com.example.user.myfinalprojectssc_ver1.R;
import com.famoco.secommunication.SmartcardReader;

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

/**
 * Created by Ho Dong Trieu on 09/04/2018
 */
public class TopUpRequestToBE extends AsyncTask<TopUpRequestToBE.TopUpParam,Void,TopUpRequestToBE.TopUpParam> {

    public static class TopUpParam {
        final TapActivity activity;
        final String accesstoken;
        final String param;
        String result;
        DesfireDep desfireDep;
        final NFCReader nfcReader;
        int count;

        public TopUpParam(TapActivity activity, String accesstoken, String param, String result, DesfireDep desfireDep, NFCReader nfcReader,int count) {
            this.activity = activity;
            this.accesstoken = accesstoken;
            this.param = param;
            this.result = result;
            this.desfireDep = desfireDep;
            this.nfcReader = nfcReader;
            this.count = count;
        }
    }

    PocketData pocketData;

    @Override
    protected TopUpParam doInBackground(TopUpParam... topUpParams) {
        TopUpParam upParam = topUpParams[0];
        NFCReader reader = upParam.nfcReader;
        //reader.powerOn();
        upParam.desfireDep = new DesfireDep(reader.card, NFCReader.smartcardReader);
        CardData cardData = new CardData();
        pocketData = new PocketData();
        Log.d("HDT4","1");

        try {
            upParam.desfireDep.cardDataInit(cardData);
            if(cardData.isActive() && cardData.isTopupEnable()){
                Log.d("HDT4","2");
                upParam.desfireDep.pocketDataInit(pocketData);
                if(upParam.activity.PocketID.equals(pocketData.getPocketID())){
                    upParam.activity.transactionModel.setCard_number(pocketData.getPocketID());
                    Log.d("HDT4","3");
                    upParam.desfireDep.selectApp(Constants.APP_CREDIT);
                    if(upParam.desfireDep.authenDivInp("14","04",Constants.DIVINP)){
                        Log.d("HDT4","4");
                        try {
                            URL url = new URL(Constants.URL_MCP);
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setConnectTimeout(10000);
                            httpURLConnection.setRequestMethod("POST");
                            httpURLConnection.setRequestProperty("Authorization",upParam.accesstoken);
                            httpURLConnection.setRequestProperty("Content-Type","application/json");

                            httpURLConnection.setUseCaches(false);
                            httpURLConnection.setDoOutput(true);

                            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                            dataOutputStream.writeBytes(upParam.param);
                            dataOutputStream.flush();
                            dataOutputStream.close();

                            InputStream inputStream = httpURLConnection.getInputStream();
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                            String line;
                            StringBuilder stringBuilder = new StringBuilder();
                            while ((line = bufferedReader.readLine())!= null){
                                stringBuilder.append(line).append("\n");
                            }
                            upParam.result = stringBuilder.toString();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return topUpParams[0];
                    } else {
                        Log.d("HDT4","4'");
                        upParam.count = 3;
                    }
                }else {
                    Log.d("HDT4","3'");
                    upParam.count = 2;
                }
            }else {
                Log.d("HDT4","2'");
                upParam.count = 1;
            }
            Log.d("HDT4","5");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return topUpParams[0];
    }

    @Override
    protected void onPostExecute(TopUpParam topUpParam) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(topUpParam.result);
            Log.d(Constants.TAG,topUpParam.result);
            String responseCode = jsonObject.getString("responseCode");
            //record****************************
            topUpParam.activity.recordTopUp.setCardid(jsonObject.getString("cardNumber"));
            topUpParam.activity.transactionModel.setCard_number(jsonObject.getString("cardNumber"));
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());
            topUpParam.activity.recordTopUp.setDatetimetopup(currentDateandTime);
            topUpParam.activity.recordTopUp.setAmountbefore(topUpParam.activity.PocketBalance.toString());
            topUpParam.activity.transactionModel.setDate_time(currentDateandTime);
            //**********************************
            switch (responseCode){
                case "00":
                    topUpParam.count = 4;
                    break;
                case "96":
                    topUpParam.count = 5;
                    break;
                case "05":
                    topUpParam.count = 5;
                    break;
                default:
                    topUpParam.count = 5;
                    break;
            }
            DoAfterCheckAmout(topUpParam);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void DoAfterCheckAmout(TopUpParam topUpParam){
        switch (topUpParam.count){
            case 1:
            case 2:
            case 5:
                //Toast.makeText(topUpParam.activity,"Thẻ không hợp lệ",Toast.LENGTH_SHORT).show();
                topUpParam.count = 0;
                topUpParam.activity.guide_text.clearAnimation();
                NotificationShow(topUpParam,"Thẻ không hợp lệ hoặc không đủ tiền trong tài khoản",pocketData);
                break;
            case 3:
                //Toast.makeText(topUpParam.activity,"Đã xảy ra lỗi",Toast.LENGTH_SHORT).show();
                topUpParam.count = 0;
                topUpParam.activity.guide_text.clearAnimation();
                NotificationShow(topUpParam,"Có lỗi xảy ra, mời bạn thực hiện lại giao dịch",pocketData);
                break;
            case 4:
                //success case
                topUpParam.activity.guide_text.clearAnimation();
                Log.d(Constants.TAG,topUpParam.result);
                try {
                    if(topUpParam.desfireDep.writeCredit_full("02",topUpParam.activity.amount_4byte)){
                        if(topUpParam.desfireDep.commitTransaction()){
                            //PocketData pocketData = new PocketData();
                            topUpParam.desfireDep.pocketDataInit(pocketData);
                            topUpParam.activity.recordTopUp.setAmountafter(pocketData.getPocketBalance() + "");
                            topUpParam.activity.recordTopUp.setStatustopup(1);
                            topUpParam.activity.transactionModel.setStatus(Constants.IS_SYNC);
                            topUpParam.activity.databaseHelperRecord.addRecordTopup(topUpParam.activity.recordTopUp);
                            topUpParam.activity.transactionModel.setId_invoices("00");
                            topUpParam.activity.databaseHelperTransaction.AddTransaction(topUpParam.activity.transactionModel);
                            TapActivity.check = true;
                            NotificationShow(topUpParam,"Giao dịch thành công",pocketData);
                        }
                    }
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private void NotificationShow(TopUpParam param, String msg,PocketData pocketData){
        param.activity.countDownTimer.cancel();
        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(param.activity,R.style.AlertDialogTheme);
        builder1.setTitle(msg);
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Đồng ý",
                (dialog, id) -> {
                    dialog.cancel();
                    Intent intent = new Intent(param.activity,TopUpActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("PocketID",pocketData.getPocketID());
                    bundle.putLong("PocketBalance",pocketData.getPocketBalance());
                    bundle.putString("Activity","TapActivity");
                    intent.putExtras(bundle);
                    param.activity.startActivity(intent);
                    param.activity.finish();
                });

        android.support.v7.app.AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
