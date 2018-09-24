package com.example.user.myfinalprojectssc_ver1.API;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.user.myfinalprojectssc_ver1.Activities.MainActivity;
import com.example.user.myfinalprojectssc_ver1.Activities.PurchaseActivity;
import com.example.user.myfinalprojectssc_ver1.Activities.SettlementActivity;
import com.example.user.myfinalprojectssc_ver1.Others.Constants;
import com.example.user.myfinalprojectssc_ver1.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ho Dong Trieu on 09/16/2018
 */
public class SettlementTransactionAPI extends AsyncTask<SettlementTransactionAPI.SettlementParam,Void,SettlementTransactionAPI.SettlementParam> {

    public static class SettlementParam {

        final SettlementActivity activity;
        final String request;
        String result;

        public SettlementParam(SettlementActivity activity, String request,String result) {
            this.activity = activity;
            this.request = request;
            this.result = result;
        }
    }

    @Override
    protected SettlementParam doInBackground(SettlementParam... settlementParams) {

        SettlementParam param = settlementParams[0];
        try {
            URL url = new URL(Constants.URL_MCP);
            Log.d(Constants.TAG,"start: " + System.currentTimeMillis());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Authorization", MainActivity.TokenOfTopUp);
            httpURLConnection.setRequestProperty("Content-Type","application/json");

            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.writeBytes(param.request);
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
            param.result = stringBuilder.toString();

        } catch (IOException e) {
            param.result = "";
        }

        return settlementParams[0];
    }

    @Override
    protected void onPostExecute(SettlementParam settlementParam) {
        Log.d(Constants.TAG,settlementParam.result);
//        ProcessAfterRequest(settlementParam);
        JSONObject jsonObject = null;
        try {
            if(!settlementParam.result.equals("")) {
                jsonObject = new JSONObject(settlementParam.result);
                String responseCode = jsonObject.getString("responseCode");
                switch (responseCode) {
                    case "00":
                        //case success
                        ProcessAfterRequest(settlementParam);
                        break;
                    case "07":
                        //not matching
                        NotificationShow(settlementParam,"Giao dịch kết toán không khớp");
                        break;
                    default:
                        //case fail
                        NotificationShow(settlementParam,"Giao dịch kết toán thất bại");
                        break;
                }
            } else {
                //fail.
                NotificationShow(settlementParam,"Giao dịch kết toán thất bại");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void ProcessAfterRequest(SettlementParam param){
        for(int i = 0 ; i < param.activity.transactionModelList.size() ; i ++){
            param.activity.databaseHelperTransaction.DelectRecordTransaction(param.activity.transactionModelList.get(i));
        }
        param.activity.total_settlment_amount.setText("0");
        param.activity.settlement_total_transaction.setText("Tổng số 0 giao dịch");
        param.activity.transactionModelList.clear();
        param.activity.settlementAdapter.notifyDataSetChanged();
        NotificationShow(param,"Giao dịch kết toán thành công");
    }

    private void NotificationShow(SettlementParam param, String msg){
        param.activity.progressDialog.dismiss();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(param.activity, R.style.AlertDialogTheme);
        builder1.setTitle(msg);
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Đồng ý",
                (dialog, id) -> {
                    dialog.cancel();
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
