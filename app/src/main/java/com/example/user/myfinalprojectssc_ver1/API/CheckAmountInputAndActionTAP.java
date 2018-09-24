package com.example.user.myfinalprojectssc_ver1.API;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.user.myfinalprojectssc_ver1.Activities.InputKeyboard;
import com.example.user.myfinalprojectssc_ver1.Activities.TopUpActivity;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.PocketData;
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
 * Created by Ho Dong Trieu on 09/18/2018
 */
public class CheckAmountInputAndActionTAP extends AsyncTask<CheckAmountInputAndActionTAP.TopUpRequest,Void,CheckAmountInputAndActionTAP.TopUpRequest> {

    public static class TopUpRequest {
        final InputKeyboard activity;
        final String accesstoken;
        final String param;
        String result;

        public TopUpRequest(InputKeyboard activity, String accesstoken, String param, String result) {
            this.activity = activity;
            this.accesstoken = accesstoken;
            this.param = param;
            this.result = result;
        }

    }

    @Override
    protected TopUpRequest doInBackground(TopUpRequest... topUpRequests) {
        TopUpRequest param = topUpRequests[0];
        try {
            URL url = new URL(Constants.URL_MCP);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Authorization",param.accesstoken);
            httpURLConnection.setRequestProperty("Content-Type","application/json");

            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.writeBytes(param.param);
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
            return topUpRequests[0];
        }
        return topUpRequests[0];
    }

    @Override
    protected void onPostExecute(TopUpRequest topUpRequest) {
        topUpRequest.activity.progressDialog.dismiss();
        Log.d(Constants.TAG,topUpRequest.result);
        if(topUpRequest.result.equals("")){
            NotificationShow(topUpRequest, "Có lỗi xảy ra từ hệ thống, mời bạn thực hiện giao dịch lại sau");
        } else {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(topUpRequest.result);
                Log.d(Constants.TAG, topUpRequest.result);
                String responseCode = jsonObject.getString("responseCode");
                switch (responseCode) {
                    case "00":
                        topUpRequest.activity.GoToTapActivity();
                        break;
                    default:
                        NotificationShow(topUpRequest, "Tài khoản không đủ tiền");
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void NotificationShow(TopUpRequest param, String msg){
        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(param.activity, R.style.AlertDialogTheme);
        builder1.setTitle(msg);
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Đồng ý",
                (dialog, id) -> {
                    dialog.cancel();
                    param.activity.back();
                });

        android.support.v7.app.AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
