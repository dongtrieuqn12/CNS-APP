package com.example.user.myfinalprojectssc_ver1.API;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.user.myfinalprojectssc_ver1.Activities.TapActivity;
import com.example.user.myfinalprojectssc_ver1.Activities.TopUpActivity;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.CardData;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.DesfireDep;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.PocketData;
import com.example.user.myfinalprojectssc_ver1.NFCReader.NFCReader;
import com.example.user.myfinalprojectssc_ver1.Others.Constants;
import com.example.user.myfinalprojectssc_ver1.R;

import java.io.IOException;

/**
 * Created by Ho Dong Trieu on 09/18/2018
 */
public class CreditToCard extends AsyncTask<CreditToCard.CreditCardParam,Void,CreditToCard.CreditCardParam> {

    public static class CreditCardParam {
        final TapActivity activity;
        DesfireDep desfireDep;
        final NFCReader nfcReader;
        int count;

        public CreditCardParam(TapActivity activity, DesfireDep desfireDep, NFCReader nfcReader, int count) {
            this.activity = activity;
            this.desfireDep = desfireDep;
            this.nfcReader = nfcReader;
            this.count = count;
        }
    }

    PocketData pocketData;

    @Override
    protected CreditCardParam doInBackground(CreditCardParam... creditCardParams) {
        CreditCardParam param = creditCardParams[0];
        NFCReader reader = param.nfcReader;
        //reader.powerOn();
        param.desfireDep = new DesfireDep(reader.card, NFCReader.smartcardReader);
        CardData cardData = new CardData();
        pocketData = new PocketData();
        try {
            param.desfireDep.cardDataInit(cardData);
            if(cardData.isActive() && cardData.isTopupEnable()) {
                Log.d("HDT4", "2");
                param.desfireDep.pocketDataInit(pocketData);
                if (param.activity.PocketID.equals(pocketData.getPocketID())) {
                    param.activity.transactionModel.setCard_number(pocketData.getPocketID());
                    param.desfireDep.selectApp(Constants.APP_CREDIT);
                    if (param.desfireDep.authenDivInp("14", "04", Constants.DIVINP)) {
                        param.desfireDep.writeCredit_full("02", param.activity.amount_4byte);
                        if (param.desfireDep.commitTransaction()) {
                            param.desfireDep.pocketDataInit(pocketData);
                            param.activity.transactionModel.setStatus(Constants.IS_SYNC);
                            param.activity.transactionModel.setId_invoices("00");
                            param.activity.databaseHelperTransaction.AddTransaction(param.activity.transactionModel);
                            TapActivity.check = true;
                            param.count = 0;
                        } else {
                            param.count = 3;
                        }
                    } else {
                        param.count = 2;
                    }
                } else {
                    param.count = 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return creditCardParams[0];
    }

    @Override
    protected void onPostExecute(CreditCardParam creditCardParam) {
        switch (creditCardParam.count){
            case 0:
                //success
                NotificationShow(creditCardParam,"Giao dịch thành công",pocketData);
                break;
            case 1:
                //fail
                NotificationShow(creditCardParam,"Lỗi xác định thẻ",pocketData);
                break;
            case 2:
            case 3:
                //fail
                NotificationShow(creditCardParam,"Đã có lỗi xãy ra",pocketData);
                break;
        }
    }

    private void NotificationShow(CreditCardParam param, String msg, PocketData pocketData){
        param.activity.countDownTimer.cancel();
        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(param.activity, R.style.AlertDialogTheme);
        builder1.setTitle(msg);
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Đồng ý",
                (dialog, id) -> {
                    dialog.cancel();
                    Intent intent = new Intent(param.activity,TopUpActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("PocketID",param.activity.PocketID);
                    if(param.count == 0) {
                        bundle.putLong("PocketBalance", pocketData.getPocketBalance());
                    } else {
                        bundle.putLong("PocketBalance", param.activity.PocketBalance);
                    }
                    bundle.putString("Activity","TapActivity");
                    intent.putExtras(bundle);
                    param.activity.startActivity(intent);
                    param.activity.finish();
                });

        android.support.v7.app.AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
