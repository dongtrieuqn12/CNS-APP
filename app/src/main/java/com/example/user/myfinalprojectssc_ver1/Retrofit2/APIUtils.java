package com.example.user.myfinalprojectssc_ver1.Retrofit2;

/**
 * Created by DongTrieu on 07/2018.
 */

public class APIUtils {

    public static String Base_Url;
    public APIUtils(String Base_Url){
        this.Base_Url = Base_Url;
    }

    //nhận và gởi dữ liệu đi
    public static SOService getSOService(){
        return RetrofitClient.getClient(Base_Url).create(SOService.class);
    }
}
