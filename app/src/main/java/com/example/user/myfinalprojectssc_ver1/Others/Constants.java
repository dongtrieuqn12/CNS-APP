package com.example.user.myfinalprojectssc_ver1.Others;

/**
 * Created by Ho Dong Trieu on 08/28/2018
 */
public interface Constants {
    String TAG = "HDT";

    public static final String DIVINP = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";

    public static final String MTI_BALANCE = "0100";
    public static final String MTI_SETTLEMENT = "0500";
    public static final String MTI_TOPUP_SALE = "0200";
    public static final String ProCode_BALANCE_PAYACC = "310000";
    public static final String ProCode_BALANCE_POCACC = "315100";
    public static final String ProCode_TOPUP_POCACC = "575100";
    public static final String ProCode_SALTE = "005100";

//    public static final String URL_MCP = "http://921bb4a4.ngrok.io/api/authorize";
    public static final String URL_MCP = "http://54.87.241.159:8080/api/authorize";

//    public static final String URL_MCP_ACCESS_TOKEN = "http://921bb4a4.ngrok.io/login";
    public static final String URL_MCP_ACCESS_TOKEN = "http://54.87.241.159:8080/login";


    //Card APP
    public static final String APP_CREDIT = "0200DF";

    //TYPE TRANSACTION
    public static final int SALE_TYPE = 1;
    public static final int TOPUP_TYPE = 2;

    //status transaction
    public static final int IS_SYNC = 1;
    public static final int IS_NOT_SYNC = 0;
}
