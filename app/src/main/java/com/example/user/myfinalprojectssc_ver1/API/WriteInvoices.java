package com.example.user.myfinalprojectssc_ver1.API;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.user.myfinalprojectssc_ver1.Activities.MainActivity;
import com.example.user.myfinalprojectssc_ver1.Activities.PurchaseActivity;
import com.example.user.myfinalprojectssc_ver1.Activities.TapActivityForBooking;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.Booking;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.CardData;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.DesfireDep;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.PocketData;
import com.example.user.myfinalprojectssc_ver1.Model.DeleteInvoices;
import com.example.user.myfinalprojectssc_ver1.Model.ReturnInvoices.ReturnInvoices;
import com.example.user.myfinalprojectssc_ver1.NFCReader.NFCReader;
import com.example.user.myfinalprojectssc_ver1.Others.Constants;
import com.example.user.myfinalprojectssc_ver1.R;
import com.example.user.myfinalprojectssc_ver1.Retrofit2.APIUtils;
import com.example.user.myfinalprojectssc_ver1.Retrofit2.SOService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ho Dong Trieu on 09/11/2018
 */
public class WriteInvoices extends AsyncTask<WriteInvoices.WriteInvoicesParam,Void,WriteInvoices.WriteInvoicesParam> {

    public static class WriteInvoicesParam {
        final TapActivityForBooking activity;
        final NFCReader nfcReader;
        DesfireDep desfireDep;
        int count;

        public WriteInvoicesParam(TapActivityForBooking activity, NFCReader nfcReader, DesfireDep desfireDep,int count) {
            this.activity = activity;
            this.nfcReader = nfcReader;
            this.desfireDep = desfireDep;
            this.count = count;
        }
    }

    String CardNumber;

    @Override
    protected WriteInvoicesParam doInBackground(WriteInvoicesParam... writeInvoicesParams) {
        WriteInvoicesParam param = writeInvoicesParams[0];
        NFCReader reader = param.nfcReader;
        //reader.powerOn();
        param.desfireDep = new DesfireDep(reader.card, NFCReader.smartcardReader);

        CardData cardData = new CardData();
        PocketData pocketData = new PocketData();
        if (reader.card.isConnected()) {
            try {
                param.desfireDep.cardDataInit(cardData);
                if (cardData.isActive() && cardData.isBookingEnable()) {
                    param.desfireDep.pocketDataInit(pocketData);
                    CardNumber = pocketData.getPocketID();
                    param.activity.transactionModel.setCard_number(CardNumber);
                    param.desfireDep.selectApp("0400DF");
                    param.desfireDep.authenDivInp("2B", "01", Constants.DIVINP);
                    List<Booking> bookings = param.desfireDep.orderListInit();
                    if(bookings.get(0).getOrderStatus().equals("00")) {
                        if (pocketData.getPocketBalance() >= Long.valueOf(param.activity.TotalPayment)) {
                            param.desfireDep.selectApp("0200DF");
                            param.desfireDep.authenDivInp("14", "04", Constants.DIVINP);
                            if (param.desfireDep.payment(param.activity.TotalPayment)) {
                                param.desfireDep.commitTransaction();
                                param.desfireDep.selectApp("0400DF");
                                param.desfireDep.authenDivInp("2B", "01", Constants.DIVINP);
                                bookings.get(0).setOrderStatus("01");
                                bookings.get(0).setTransacCounter("00000001");
                                bookings.get(0).setOrderNumber(param.activity.InvoicesId);
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssZ");
                                String currentDateandTime = sdf.format(new Date()).substring(0, 14);
                                Log.d(Constants.TAG, currentDateandTime);
                                bookings.get(0).setOrderDatetime(currentDateandTime);
                                bookings.get(0).setDeliveryDeviceID("00000000");
                                bookings.get(0).setBookingDeviceID("11111111");
                                param.desfireDep.updateOrderLog(bookings.get(0), 0);
                                if(param.desfireDep.commitTransaction()) {
                                    Log.d(Constants.TAG,"transection success");
                                    param.count = 0; //success
                                    return writeInvoicesParams[0];
                                }
                            param.count = 4;
                            return writeInvoicesParams[0];
                            }
                        } else param.count = 2; //not enough money
                    } else param.count = 1;
                } else param.count = 3; //not active card
            } catch (IOException e) {
                e.printStackTrace();
            }
            return writeInvoicesParams[0];
        }
        param.count = 4;
        return writeInvoicesParams[0];
    }

    @Override
    protected void onPostExecute(WriteInvoicesParam writeInvoicesParam) {
        switch (writeInvoicesParam.count){
            case 0:
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());
                writeInvoicesParam.activity.transactionModel.setDate_time(currentDateandTime);
                NotificationShow(writeInvoicesParam,"Đặt hàng thành công");
                writeInvoicesParam.activity.SendRequestSale(CardNumber);
                break;
            case 1:
                NotificationShow(writeInvoicesParam,"Bạn đã hết quyền đặt hàng");
                break;
            case 2:
                NotificationShow(writeInvoicesParam,"Không đủ tiền trong ví");
                break;
            case 3:
                NotificationShow(writeInvoicesParam,"Thẻ không hợp lệ");
                break;
            case 4:
                NotificationShow(writeInvoicesParam,"Fail");
        }
    }

    private void NotificationShow(WriteInvoicesParam writeInvoicesParam,String msg){
        writeInvoicesParam.activity.countDownTimer.cancel();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(writeInvoicesParam.activity, R.style.AlertDialogTheme);
        if(writeInvoicesParam.count == 0){
            builder1.setMessage("Mã đơn hàng đã đặt: " + writeInvoicesParam.activity.InvoicesCode);
        }
        builder1.setTitle(msg);
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Ok",
                (dialog, id) -> {
                    dialog.cancel();
                    Intent intent = new Intent(writeInvoicesParam.activity,PurchaseActivity.class);
                    writeInvoicesParam.activity.startActivity(intent);
                    writeInvoicesParam.activity.finish();
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
        if(writeInvoicesParam.count != 0){
            DeleteInvoice(writeInvoicesParam.activity.InvoicesId,writeInvoicesParam.activity);
        }
    }

    private void DeleteInvoice(String idInvoices,TapActivityForBooking activityForBooking){
        DeleteInvoices deleteInvoices = new DeleteInvoices();
        deleteInvoices.setId(idInvoices);
        deleteInvoices.setVoidPayment(true);
        SOService mService;
        APIUtils apiUtils = new APIUtils("https://public.kiotapi.com/");
        mService = apiUtils.getSOService();
        mService.DeleteInvoice("Bearer " + MainActivity.accessToken,deleteInvoices).enqueue(new Callback<ReturnInvoices>() {
            @Override
            public void onResponse(Call<ReturnInvoices> call, final Response<ReturnInvoices> response) {
                if (response.message().equals("OK")) {
                    //do something....
                    Toast.makeText(activityForBooking,"Đơn hàng đã được hủy",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ReturnInvoices> call, Throwable t) {

            }
        });
    }
}
