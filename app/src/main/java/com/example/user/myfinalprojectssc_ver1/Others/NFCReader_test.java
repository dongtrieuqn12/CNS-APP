package com.example.user.myfinalprojectssc_ver1.Others;

import android.app.Activity;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.util.Log;

import com.example.user.myfinalprojectssc_ver1.MifareDesfire.ByteUtils;
import com.famoco.secommunication.ALPARProtocol;
import com.famoco.secommunication.SmartcardReader;

import java.io.IOException;

/**
 * Created by Ho Dong Trieu on 09/24/2018
 */

public class NFCReader_test {
    public static final int TIMEOUT = 500;

//    public static NFCReader get(Tag tag, Activity activity, SmartcardReader smartcardReader) {
//        NFCReader nfcReader = null;
//        try {
//            nfcReader = new NFCReader(IsoDep.get(tag), activity,smartcardReader);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return nfcReader;
//    }

    public static NFCReader_test get(Tag tag, Activity activity) {
        NFCReader_test nfcReader = null;
        try {
            nfcReader = new NFCReader_test(IsoDep.get(tag), activity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nfcReader;
    }

    public final IsoDep card;
    private Activity activity;
    public SmartcardReader smartcardReader;

    public static int count = 0;

    //    private NFCReader(IsoDep sc, Activity activity,SmartcardReader smartcardReader) throws IOException {
//        this.card = sc;
//        sc.connect();
//        //card.connect();
//        card.setTimeout(TIMEOUT);
//        this.activity = activity;
//        this.smartcardReader = smartcardReader;
//        //test***************************
//        if(WorkerActivity.count == 0) {
//            powerOn();
//            WorkerActivity.count++;
//        }
//        //test***************************
//        Log.d("HDT2","i am here");
//        //smartcardReader = SmartcardReader.getInstance();
//    }
    private NFCReader_test(IsoDep sc, Activity activity) throws IOException {
        this.card = sc;
        sc.connect();
        //card.connect();
        card.setTimeout(TIMEOUT);
        this.activity = activity;
//        if(count == 0) {
//            powerOn();
//            count++;
//        }
    }

    public void powerOn() {
        /* should already be connected... */
        smartcardReader = SmartcardReader.getInstance();
        smartcardReader.openReader(115200);

        smartcardReader.setAutoNegotiate(true);
        Log.d("HDT2","i am in powerOn");
        //power on smartcard reader
        byte[] atr = smartcardReader.powerOn();
        String artReceive = ByteUtils.byteArray2HexString(atr);
        Log.d("HDT2","i am in powerOn " + artReceive);

        byte frequencySetting = ALPARProtocol.PARAM_CLOCK_FREQUENCY_3_68MHz;
        smartcardReader.setClockCard(frequencySetting);
        //String artReceive = ByteUtils.byteArray2HexString(atr);
        Log.d("HDT2","i am in powerOn");
    }

    public void powerOff() {
        //selectMF();
        //turn off reader
        smartcardReader.powerOff();
        smartcardReader.closeReader();
    }

    public void reset() throws IOException {
        //selectMF();
    }

    /* calculation based on https://code.google.com/p/ifdnfc/source/browse/src/atr.c */
    public byte[] getATR() {
        // get historical bytes for 14443-A
        byte[] historicalBytes = card.getHistoricalBytes();
        if (historicalBytes == null) {
            // get historical bytes for 14443-B
            historicalBytes = card.getHiLayerResponse();
        }
        if (historicalBytes == null) {
            historicalBytes = new byte[0];
        }

        /* copy historical bytes if available */
        byte[] atr = new byte[4+historicalBytes.length+1];
        atr[0] = (byte) 0x3b;
        atr[1] = (byte) (0x80+historicalBytes.length);
        atr[2] = (byte) 0x80;
        atr[3] = (byte) 0x01;
        System.arraycopy(historicalBytes, 0, atr, 4, historicalBytes.length);

        /* calculate TCK */
        byte tck = atr[1];
        for (int idx = 2; idx < atr.length; idx++) {
            tck ^= atr[idx];
        }
        atr[atr.length-1] = tck;

        return atr;
    }

    public void eject() throws IOException {
        card.close();
//        resetScreenTimeout();
    }

    public byte[] transmit(byte[] apdu) throws IOException {
        return card.transceive(apdu);
    }

//    private static final byte[] SELECT_MF = {(byte) 0x00, (byte) 0xa4, (byte) 0x00, (byte) 0x0C};
//    private void selectMF() throws IOException {
//        byte[] response = card.transceive(SELECT_MF);
//        if (response.length == 2 && response[0] == (byte) 0x90 && response[1] == (byte) 0x00) {
//            Log.d(Constants.TAG, "Resetting the card by selecting the MF results in " + Hex.getHexString(response));
//        }
//    }
}
