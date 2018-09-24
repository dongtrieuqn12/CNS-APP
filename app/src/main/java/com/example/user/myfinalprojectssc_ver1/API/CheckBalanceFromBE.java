package com.example.user.myfinalprojectssc_ver1.API;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myfinalprojectssc_ver1.Activities.MainActivity;
import com.example.user.myfinalprojectssc_ver1.Activities.TopUpActivity;
import com.example.user.myfinalprojectssc_ver1.Others.Constants;

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
 * Created by Ho Dong Trieu on 09/04/2018
 */
public class CheckBalanceFromBE extends AsyncTask<CheckBalanceFromBE.CheckBalanceParams,Void,CheckBalanceFromBE.CheckBalanceParams> {

    public static class CheckBalanceParams {
        final TopUpActivity activity;
        int done;
        final String accesstoken;
        final String param;
        String result;
        TextView textView;

        public CheckBalanceParams(TopUpActivity activity, int done,String accesstoken,String param,String result,TextView textView) {
            this.activity = activity;
            this.done = done;
            this.accesstoken = accesstoken;
            this.param = param;
            this.result = result;
            this.textView = textView;
        }
    }

    StringBuilder stringBuilder = new StringBuilder();

    @Override
    protected CheckBalanceParams doInBackground(CheckBalanceParams... checkBalanceParams) {

        CheckBalanceParams Param = checkBalanceParams[0];

        try {
            URL url = new URL(Constants.URL_MCP);
            Log.d(Constants.TAG,"start: " + System.currentTimeMillis());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Authorization",Param.accesstoken);
            httpURLConnection.setRequestProperty("Content-Type","application/json");

            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.writeBytes(Param.param);
            dataOutputStream.flush();
            dataOutputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine())!= null){
                stringBuilder.append(line).append("\n");
            }
            Param.result = stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return checkBalanceParams[0];
    }

    @Override
    protected void onPostExecute(CheckBalanceParams checkBalanceParams) {
        JSONObject jsonObject = null;
        try {
            Log.d(Constants.TAG,"end: " + System.currentTimeMillis());
            jsonObject = new JSONObject(checkBalanceParams.result);
            Log.d(Constants.TAG,checkBalanceParams.result);
            String responseCode = jsonObject.getString("responseCode");
            switch (responseCode){
                case "00":
                    Long s = Long.valueOf(jsonObject.getString("amount"));
                    String s1 = "";
                    int numLength = s.toString().length();
                    for (int i=0; i<numLength; i++) {
                        if ((numLength-i)%3 == 0 && i != 0) {
                            s1 += ".";
                        }
                        s1 += s.toString().charAt(i);
                    }
                    checkBalanceParams.textView.setText(s1+"");
                    break;
                case "96":
                    Toast.makeText(checkBalanceParams.activity,"Thẻ không hợp lệ",Toast.LENGTH_SHORT).show();
                    checkBalanceParams.activity.ShowDefault();
                    break;
                case "05":
                    Toast.makeText(checkBalanceParams.activity,"Thẻ không hợp lệ",Toast.LENGTH_SHORT).show();
                    checkBalanceParams.activity.ShowDefault();
                    break;
                default:
                    Toast.makeText(checkBalanceParams.activity,"Unknown",Toast.LENGTH_SHORT).show();
                    checkBalanceParams.activity.ShowDefault();
                    break;
            }
//            if(checkBalanceParams.done == 1){
//                checkBalanceParams.activity.done1 = true;
//            }else checkBalanceParams.activity.done2 = true;
//            Log.d(Constants.TAG,checkBalanceParams.activity.done1+"");
            checkBalanceParams.activity.progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
