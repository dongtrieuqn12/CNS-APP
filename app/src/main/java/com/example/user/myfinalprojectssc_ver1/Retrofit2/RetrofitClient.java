package com.example.user.myfinalprojectssc_ver1.Retrofit2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DongTrieu on 07/2018.
 */

//khởi tạo và cấu hình retrofit
public class RetrofitClient {
    private static Retrofit retrofit = null;
    public static Retrofit getClient(String baseurl){
        OkHttpClient builder = new OkHttpClient.Builder()
                                        .readTimeout(25000, TimeUnit.MILLISECONDS)
                                        .writeTimeout(10000,TimeUnit.MILLISECONDS)
                                        .connectTimeout(10000,TimeUnit.MILLISECONDS)
                                        .retryOnConnectionFailure(true)
                                        .build();

        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .client(builder)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit;
    }
}
