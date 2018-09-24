package com.example.user.myfinalprojectssc_ver1.API;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ho Dong Trieu on 09/04/2018
 */
public class getAccessToken extends AsyncTask<String,Void,String> {
    StringBuilder stringBuilder;
    HttpURLConnection connection = null;
    String targetURL;
    String urlParameters;
    public getAccessToken(String urlParameters){
        this.urlParameters = urlParameters;
    }
    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL("https://id.kiotviet.vn/connect/token");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush ();
            wr.close ();

            //get response
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine())!= null){
                stringBuilder.append(line + "\n");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return String.valueOf(stringBuilder);
    }
}
