package com.example.user.myfinalprojectssc_ver1.MifareDesfire;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;

import android.util.Log;

import com.example.user.myfinalprojectssc_ver1.Others.Constants;
import com.famoco.secommunication.ALPARProtocol;
import com.famoco.secommunication.SmartcardReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;

public class DesfireDep {
    //******************* Key Number *******************//
    private static final String MKcard_config = "0A";
    private static final String DKcard_config = "02";
    private static final String MKpocket_info = "11";
    private static final String DKpocket_info = "01";
    private static final String MKpocket_debit = "12";
    private static final String DKpocket_debit = "02";
    private static final String MKpocket_credit = "13";
    private static final String DKpocket_credit = "04";
    private static final String MKpocket_holder = "21";
    private static final String DKpocket_holder = "01";


    //******************* Application Integer Define **********************//
    public static final int DF_CARD = 0;
    public static final int DF_PURSE = 1;
    public static final int DF_ID = 2;
    //******************* Application ID *************************//
    private static final String DF_card = "0100DF";
    private static final String DF_purse = "0200DF";
    private static final String DF_id = "0300DF";
    //************ Native command response ************//
    static final String OPERATION_OK = "00";
    static final String APPLICATION_NOT_FOUND = "A0";
    //******** APDU Command Define *********//

    static final String appList = "6A";

    //*************** SAM ****************//
    private SmartcardReader smartcardReader;

    //*************** TAG ****************//
    public IsoDep isoDep;
    public DesfireDep(IsoDep isoDep, SmartcardReader smartcardReader) {
        this.smartcardReader = smartcardReader;
        //this.tagFromIntent = tagFromIntent;
        this.isoDep = isoDep;
    }

    public void initSmartcard() {
        smartcardReader = SmartcardReader.getInstance();
        // debugging enable
        //SmartcardReader.setDebuggingEnabled(true);
        // open smartcard reader
        smartcardReader.openReader(115200);

        String firmwareVersion = smartcardReader.getFirmwareVersion();
        Log.d("alo", "firmwareVersion = " + firmwareVersion);

        // auto negotiate for optimal speed
        smartcardReader.setAutoNegotiate(true);

        // power on smartcard reader
        byte[] atr = smartcardReader.powerOn();

        byte frequency = ALPARProtocol.PARAM_CLOCK_FREQUENCY_3_68MHz;
        smartcardReader.setClockCard(frequency);

        String atrHex = ByteUtils.byteArray2HexString(atr);
//        Log.d("alo", "ATR: " + atrHex);
    }
//
//    public void deinitSmartcard() {
//        // power off smartcard reader
//        smartcardReader.powerOff();
//
//        // close smartcard reader
//        smartcardReader.closeReader();
//    }

    public String send2SAM(String s) {

        //send APDU
        Log.d("alo", "=> SAM " + s);
        long start = System.currentTimeMillis();
        byte[] response = smartcardReader.sendApdu(ByteUtils.hexString2ByteArray(s));
        s = ByteUtils.byteArray2HexString(response);
        long end = System.currentTimeMillis();
        Log.d("alo", "<= SAM " + ByteUtils.byteArray2HexString(response));
        if (s.endsWith("90AF")) {
            Log.d("alo", "Communication continue");
        }
        return s;
    }

    public String getCipherCmd (String P1P2Le, String cardAPDU) {
        //initSmartcard();
        String samAPDU = "80ED" + P1P2Le + cardAPDU + "00";
        String s = send2SAM(samAPDU);
        if (s.endsWith("9000")) {
            Log.d("alo", "encipher succeed");
            s = s.substring(0, s.length() - 4);
            Log.d("alo", "encipher CMD: " + s);
            return s;
        } else {
            Log.d("alo", "encipher failed");
            return null;
        }
    }

    public String getDeCipherCmd (String cardAPDU) {
        String samAPDU = "805C000009" + cardAPDU;
        String s = send2SAM(samAPDU);
        return s;
    }



    public boolean connect() {
        try {
            isoDep.connect();
        } catch (IOException e) {
            Log.e("alo", "IOException while reading Desfire", e);
        }
        if (isoDep.isConnected()) {
            return true;
        } else {
            return false;
        }
    }


    public void getAppInfo() throws IOException {
        send2Card("6A");
    }

    public boolean selectApp(String AID) throws IOException {
        String s = send2Card("5A" + AID);
        if (s.endsWith("A0")) {
            Log.d("alo", "Application not found");
            return false;
        } else if (s.endsWith("00")) {
            Log.d("alo", "Application selected");
            return true;
        } else {
            Log.d("alo", "Select Application failed");
            return false;
        }
    }

    public boolean authenNative(String keyNoSAM, String keyNocard) throws IOException {
        boolean authenResult = false;
//        initSmartcard();
        String s = send2Card("AA" + keyNocard);
        if (s.startsWith("AF")) {
            s = s.substring(2);
            Log.d("alo", "E(Kx,RndB): " + s);
        }
        s = send2SAM("800A000012" + keyNoSAM + "00" + s +  "00");
        s = s.substring(0, s.length() - 4);
        s = send2Card("AF" + s);
        switch (s) {
            case "AE":
                Log.d("alo", "RndB was wrong");
                break;
            case "7F":
                Log.d("alo", "Length error");
                break;
            default:
                s = s.substring(2);
                s = send2SAM("800A000010" + s);
                switch (s) {
                    case "9000":
                        Log.d("alo", "Authenticate success");
                        authenResult = true;
                        break;
                    case "6700":
                        Log.d("alo", "Authenticate problem");
                        break;
                    default:
                        break;
                }
                break;
        }
//        deinitSmartcard();
        return authenResult;
    }

    public boolean authenDivInp(String keyNoSAM, String keyNocard, String divInp) throws IOException {
        boolean authenResult = false;
//        initSmartcard();
        String s = send2Card("AA" + keyNocard);
        if (s.startsWith("AF")) {
            s = s.substring(2);
            Log.d("alo", "E(Kx,RndB): " + s);
        }
        s = send2SAM("800A010022" + keyNoSAM + "00" + s + divInp + "00");
        Log.d("alo", "response: " + s);
        s = s.substring(0, s.length() - 4);
        s = send2Card("AF" + s);
        switch (s) {
            case "AE":
                Log.d("alo", "RndB was wrong");
                break;
            case "7F":
                Log.d("alo", "Length error");
                break;
            default:
                s = s.substring(2);
                s = send2SAM("800A000010" + s);
                switch (s) {
                    case "9000":
                        Log.d("alo", "Authenticate success");
                        authenResult = true;
                        break;
                    case "6700":
                        Log.d("alo", "Wrong Length");
                        break;
                    default:
                        break;
                }
                break;
        }
        //deinitSmartcard();
        return authenResult;
    }

    public String readData (String fileID, String offSet, int lengthInt) throws IOException {
        String length = ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(lengthInt,3));
        String nativeReadCmd = "BD" + fileID + offSet + length;
        String s = send2Card(nativeReadCmd);
        if (s.startsWith("00")) {
            Log.d("alo", "Read action succeed");
            return s.substring(2);
        } else if (s.startsWith("AF")) {
            Log.d("alo", "Read action continue");
            return s.substring(2);
        } else {
            Log.d("alo", "Read action error");
            return null;
        }
    }

    public String readLog (String fileID, String offSet, int lengthInt) throws IOException {
        String length = ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(lengthInt, 3));
        String nativeReadLogCmd = "BB" + fileID + offSet + length;
        String cipherString = "";
        send2SAM("807C000008" + "BB" + fileID + offSet +length + "00");
        String s = send2Card(nativeReadLogCmd);
        for (int i = 0; i < 10; i++) {
            if (s.startsWith("AF")) {
                Log.d("alo", "Read action continue");
                cipherString += s.substring(2);
                s = send2Card("AF");
            } else if (s.startsWith("00")) {
                Log.d("alo", "Read cipher string succeed");
                cipherString += s.substring(2);
                break;
            } else {
                Log.d("alo", "Error");
                break;
            }
        }
        Log.d("alo", "Cipher string: " + cipherString);
        s = send2SAM("80DD0000" + ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(cipherString.length()/2 + 1, 1)) + cipherString + "0000");

        return s;
    }

    public String readLog_plain(String fileID, String offSet, int anInt, int lengthInt) throws IOException {
        int i = 0; // Counter for receiving log data
        String logData = "";
        String length = ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(lengthInt, 3));
        String nativeReadLogCmd = "BB" + fileID + offSet + length;
        // Init SAM
        String s = "807C000008" + nativeReadLogCmd + "00";
        s = send2SAM(s);
        if (!s.endsWith("9000")) {
            return null;
        }
        s = send2Card(nativeReadLogCmd);
        for (i = 0; i < 10; i++) {
            if (s.startsWith("00")) {
                logData += s.substring(2);
                break;
            } else if (s.startsWith("AF")) {
                logData += s.substring(2);
                s = send2Card("AF");
            } else {
                return null;
            }
        }
        return logData;
    }

    public String readValue_plain (String fileID) throws IOException {
        String s = send2Card("6C" + fileID);
        if (!s.startsWith("00")) {
            Log.d("alo","Read value failed");
            return null;
        }
        return s.substring(2,10);
    }

    public String getValueNative (String fileID) throws IOException {
        String s = "6C" + fileID;
        s = send2Card(s);
        if (s.startsWith("00")) {
            Log.d("alo", "Read value succeed");
            s = s.substring(2);
            return s;
        } else {
            Log.d("alo", "Read value failed");
            return s;
        }
    }

    public boolean write2Card (String fileID, String offSet, String data) throws IOException {
        String length = ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(data.length()/2, 3));
        String nativeWriteCmd = "3D" + fileID + offSet + length + data;
        String s = send2Card(nativeWriteCmd);
        if (s.startsWith("00")) {
            Log.d("alo", "Writing succeed");
            return true;
        } else {
            Log.d("alo", "Writing failed ");
            return false;
        }
    }

    public boolean writeBackUp (String fileID, String offSet, String data) throws IOException {
        String length = ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(data.length()/2, 3));
        String nativeWriteCmd = "3D" + fileID + offSet + length + data;
        String s = send2Card(nativeWriteCmd);
        if (s.startsWith("00")) {
            Log.d("alo", "Wait for commit");
            s = send2Card("C7");
            if (s.startsWith("00")) {
                Log.d("alo", "Writing BackUp succeed");
                return true;
            } else {
                Log.d("alo", "Commit BackUp failed");
                return false;
            }
        } else {
            Log.d("alo", "Writing BackUp failed");
            return false;
        }
    }

    public boolean writeLog (String fileID, String offSet, String data) throws IOException {
        String cardAPDU = "3B" + fileID + offSet + ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(data.length()/2, 3)) + data;
        String s = getCipherCmd("0008" +
                ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(cardAPDU.length()/2, 1)), cardAPDU);
        s = send2Card("3B" + fileID + offSet + ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(data.length()/2, 3)) + s);
//        s = send2Card(cardAPDU);
        s = getDeCipherCmd(s);
        if (s.endsWith("9100")) {
            // Commit SAM
            s = send2SAM("807C000001C700");
            if (s.endsWith("9000")) {
            } else {
            }
            // Card commit
            s = send2Card("C7");
            s = getDeCipherCmd(s);
            if (s.endsWith("9100")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean valueCredit (String fileID, String data_4byte) throws IOException {
        String s = getCipherCmd("000206","0C" + fileID + data_4byte);
        s = send2Card("0C" + fileID + s);
        s = getDeCipherCmd(s);
        if (s.endsWith("9000")) {
            Log.d("alo", "Credit succeed");
            return true;
        } else {
            Log.d("alo", "Credit failed");
            return false;
        }
    }

    public boolean valueDebit (String fileID, String data_4byte) throws IOException {
        String s = getCipherCmd("000206","DC" + fileID + data_4byte);
        s = send2Card("DC" + fileID + s);
        s = getDeCipherCmd(s);
        if (s.endsWith("9000")) {
            Log.d("alo", "Wait for commit");
            s = send2Card("C7");
            s = getDeCipherCmd(s);
            if (s.startsWith("9000")) {
                Log.d("alo", "Credit succeed");
                return true;
            } else {
                Log.d("alo", "Commit failed");
                return false;
            }
        } else {
            Log.d("alo", "Credit failed");
            return false;
        }
    }

    public String tagInfo() throws IOException {
        String tagInfo = "";
        String s = send2Card("60");
        for (int i = 0; i <10; i++){
            if (s.startsWith("AF")) {
                s = s.substring(2);
                tagInfo += s;
                s = send2Card("AF");
            } else if (s.startsWith("00")) {
                s = s.substring(2);
                tagInfo += s;
                break;
            }
        }
        Log.d("alo", "Tag Info: " + tagInfo);
        return tagInfo;
    }

    public String send2Card(String s) throws IOException {
        byte[] response;
        Log.d("alo", "-> card: " + s);
        response = isoDep.transceive(ByteUtils.hexString2ByteArray(s));
        s = ByteUtils.byteArray2HexString(response);
        Log.d("alo", "<- card: " + s);
        return s;
    }

    //********************* SAM & CARD *************************//
    public String readLog_full (String fileID, String offSet, int lengthInt) throws IOException {
        int i = 0; // Counter for receiving log data
        String logData = "";
        String length = ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(lengthInt, 3));
        String nativeReadLogCmd = "BB" + fileID + offSet + length;
        // Init SAM
        String s = "807C000008" + nativeReadLogCmd + "00";
        s = send2SAM(s);
        if (!s.endsWith("9000")) {
            return null;
        }
        s = send2Card(nativeReadLogCmd);
        for (i = 0; i < 10; i++) {
            if (s.startsWith("00")) {
                Log.d("alo", "read all cipher data");
                logData += s.substring(2);
                break;
            } else if (s.startsWith("AF")) {
                Log.d("alo", "read cipher continue");
                logData += s.substring(2);
                s = send2Card("AF");
            } else {
                Log.d("alo", "read log failed");
                return null;
            }
        }
        if (i < 5) {
            s = send2SAM("80DD0000" + ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(logData.length()/2 + 1,1)) + logData + "0000");
            if (s.endsWith("9000")) {
                Log.d("alo", "Read log completed");
                logData = s.substring(0,s.length()-4);
                return logData;
            } else {
                Log.d("alo", "Decipher failed");
                logData = s.substring(s.length()-4);
                return logData;
            }
        }
        String cipher1 = logData.substring(0,480); // 1st frame to be decipher with the length of 240 bytes (F0)
        Log.d("alo","cipher1: " + cipher1);
        String cipher2 = logData.substring(480);
        Log.d("alo","cipher2: " + cipher2);
        s = send2SAM("80DDAF00" + "F0" + cipher1 + "00");
        if (s.endsWith("90AF")) {
            Log.d("alo", "Read encipher continue");
            logData = s.substring(0, s.length()-4);
            s = send2SAM("80DD0000" + ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(cipher2.length()/2 + 1,1)) + cipher2 + "0000");
            if (s.endsWith("9000")) {
                Log.d("alo","Read encipher complete");
                logData += s.substring(0,s.length()-4);
                return logData;
            } else {
                Log.d("alo", "Read encipher failed");
                logData += s.substring(0,s.length()-4);
                return logData;
            }
        } else {
            Log.d("alo", "Read encipher failed");
            return null;
        }
    }

    public boolean writeCredit_full (String fileID, String data_4byte) throws IOException {
        // Create a apdu message to get encipher
        String s = "80ED0002" + "06" + "0C" + fileID + data_4byte + "00";
        s = send2SAM(s);
        if (!s.endsWith("9000")) {
            Log.d("alo", "Credit failed");
            return false;
        }
        // Filter data only
        s = s.substring(0,s.length()-4);
        // Create a native command to send to card
        s = "0C" + fileID + s;
        s = send2Card(s);
        if (!s.startsWith("00")) {
            Log.d("alo", "Credit failed");
            return false;
        }
        // Create a apdu message to get decipher
        s = "805C0000" + ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(s.length()/2,1)) + s;
        s = send2SAM(s);
        if (!s.endsWith("9000")) {
            Log.d("alo", "Credit failed");
            return false;
        }
        Log.d("alo","Credit succeed");
        return true;
    }

    public boolean writeDebit_full (String fileID, String data_4byte) throws IOException {
        // Create a apdu message to get encipher
        String s = "80ED0002" + "06" + "DC" + fileID + data_4byte + "00";
        s = send2SAM(s);
        if (!s.endsWith("9000")) {
            Log.d("alo", "Credit failed");
            return false;
        }
        // Filter data only
        s = s.substring(0,s.length()-4);
        // Create a native command to send to card
        s = "DC" + fileID + s;
        s = send2Card(s);
        if (!s.startsWith("00")) {
            Log.d("alo", "Debit failed");
            return false;
        }
        // Create a apdu message to get decipher
        s = "805C0000" + ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(s.length()/2,1)) + s;
        s = send2SAM(s);
        if (!s.endsWith("9000")) {
            Log.d("alo", "Debit failed");
            return false;
        }
        Log.d("alo","Debit succeed");
        return true;
    }

    public boolean commitTransaction() throws IOException {
        // Send commit apdu to SAM
        String s = send2SAM("807C000001C700");
        if (!s.endsWith("9000")) {
            Log.d("alo","Commit failed");
            return false;
        }
        // Send commit command to card
        s = send2Card("C7");
        if (!s.startsWith("00")) {
            Log.d("alo","Commit failed");
            return false;
        }
        // decipher result
//        s = send2SAM("805C0000" + ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(s.length()/2,1)) + s);
//        if (!s.endsWith("9000")) {
//            Log.d("alo","Commit failed");
//            return false;
//        }
        Log.d("alo","Commit succeed");
        return true;
    }

    /***************** Application Function ******************/

    /*************** Card Info *********************/
    public String getCardID() throws IOException {
        //selectApp("0100DF"); // preserve for single calling
        String s = readData("01","000000",8);
        return s;
    }

    public String getCardExpiredDate() throws IOException {
        //selectApp("0100DF"); // preserve for single calling
        String s = readData("01","0D0000",4);
        return s;
    }
    /*************** End Card Info ****************/

    /****************** Card Config ****************/
    public String getCardConfig() throws IOException {
        //selectApp("0100DF"); // preserve for single calling
        String s = readData("02","000000",5);
        return s;
    }
    /***************** End Card Config ****************/

    /***************** Card Status *******************/
    public String getCardStatus() throws IOException {
        //selectApp("0100DF"); // preserve for single calling
        String s = readData("03","000000",1);
        return s;
    }
    /***************** End Card Config ****************/

    /***************** Pocket Info *******************/
    public String getPocketID() throws IOException {
        //selectApp("0200DF"); // preserve for single calling
        String s = readData("01","000000",8);
        return s;
    }

    public String getPocketStatus() throws IOException {
        //selectApp("0200DF"); // preserve for single calling
        String s = readData("01","110000",1);
        return s;
    }
    /***************** End Pocket Info *****************/

    /***************** Pocket Value *******************/
    public String getPocketValue_string() throws IOException {
        //selectApp("0200DF"); // preserve for single calling
        String s = readValue_plain("02");
        return s;
    }
    public long getPocketValue_long() throws IOException {
        //selectApp("0200DF"); // preserve for single calling
        long balance = 0;
        String s = readValue_plain("02");
        Log.d("hodongtrieu",s);
        byte[] buffer = new byte[4];
        buffer = ByteUtils.toByteArray(s);
//        for(int i = 1 ; i < 4 ; i++){
//            balance += (buffer[i]*0xFF) << 8*(4 - i);
//        }

        for (int i = 0; i < 4; i++)
        {
            balance += (buffer[i] & 0xff) << (8 * i);
        }
//        for (int i = 0 ; i < 8; i++){
//            balance += Long.valueOf(s.substring(i,i+1)) * pow(16,7-i);
//        }
        Log.d("hodongtrieu",balance+"");
        return balance;
    }
    /***************** End Pocket Value ****************/

    /***************** Pocket Log *********************/
    public String getPocketLog() throws IOException {
        String s = readLog_full("03","000000",0);
        return s;
    }
    /***************** End Pocket Log *****************/

    /********************* Combination *******************/
    public boolean cardDataInit (CardData cardData) throws IOException {
        // Initialize SAM
//        initSmartcard();

        /************** Card Data ***************/
        selectApp("0100DF");
        /************** Card Info ***************/
        cardData.setCardID(getCardID());
        cardData.setCardExDate(getCardExpiredDate());
        /************** Card Config *************/
        String s = getCardConfig();
        Log.d("hodongtrieu",s);
        if (s.charAt(1) == '1') {
            cardData.setActive(true);
        } else {
            cardData.setActive(false);
        }
        if (s.charAt(3) == '1') {
            cardData.setPurseEnable(true);
        } else {
            cardData.setPurseEnable(false);
        }
        if (s.charAt(5) == '1') {
            cardData.setTopupEnable(true);
        } else {
            cardData.setTopupEnable(false);
        }
        if (s.charAt(7) == '1') {
            cardData.setAutoLoadEnable(true);
        } else {
            cardData.setAutoLoadEnable(false);
        }
        if (s.charAt(9) == '1') {
            cardData.setBookingEnable(true);
        } else {
            cardData.setBookingEnable(false);
        }
        /****************** Card Status ****************/
        cardData.setCardStatus(getCardStatus());
        // Close SAM
//        deinitSmartcard();
        return true;
    }

    public boolean pocketDataInit (PocketData pocketData) throws IOException {
        /***************** Pocket Data ****************/
        selectApp("0200DF");
        /***************** Pocket Info ****************/
        pocketData.setPocketID(getPocketID());
        pocketData.setPocketStatus(getPocketStatus());
        /***************** Pocket Value ****************/
        pocketData.setPocketBalance(getPocketValue_long());
        return true;
    }

    /********************* Booking ***********************/
    public List<Booking> orderListInit() throws IOException {
        String log = readLog_plain("01","000000",0,0);
        List<Booking> list = new ArrayList<>();
        Log.v(Constants.TAG,log);
        for (int i = 8; i >= 0; i--) {
            list.add(new Booking(log.substring(i*48, i*48 + 48)));
        }
        return list;
    }

    public void writeOrderLog(Booking booking, int index) throws IOException {
        String orderLog = booking.getOrderStatus() + booking.getOrderNumber() + booking.getBookingDeviceID()
                + booking.getDeliveryDeviceID() + booking.getTransacCounter() + booking.getOrderDatetime();
        Log.d("hodongtrieuTest1",orderLog);
        writeLog
                ("01",ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(index/2,3)),orderLog);
        //commitTransaction();
    }

    public boolean topUp(String value) throws IOException {
        writeCredit_full("02",ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(ByteUtils.numString2long(value),4)));
        return true;
    }

    public boolean payment(String value) throws IOException {
        writeDebit_full("02",ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(Long.valueOf(value),4)));
        return true;
    }


    public void updateOrderLog(Booking booking, int index) throws IOException {
        Log.d("HDT4","update");
        String orderLog = booking.getOrderStatus() + booking.getOrderNumber() + booking.getBookingDeviceID()
                + booking.getDeliveryDeviceID() + booking.getTransacCounter() + booking.getOrderDatetime();
        updateLog_plain("01",ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(index,3)),"000000",orderLog);
        //commitTransaction();
    }



    public void updateLog_plain (String fileID, String logNo, String offSet, String data) throws IOException {
        String cardAPDU = "DB" + fileID + logNo + offSet + ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(data.length()/2, 3)) + data;
//        String s = getCipherCmd("0008" +
//                ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(cardAPDU.length()/2, 1)), cardAPDU);
//        s = send2Card("3B" + fileID + offSet + ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(data.length()/2, 3)) + s);
//        s = getDeCipherCmd(s);
        String s = send2Card(cardAPDU);
//        if (s.startsWith("00")) {
//            LogDebug("CNS", "Wait for commit");
//             //Commit SAM
//            s = send2SAM("807C000001C700");
//            if (s.endsWith("9000")) {
//                LogDebug("CNS", "SAM committed");
//            } else {
//                LogDebug("CNS", "SAM committing failed");
//            }
////            // Card commit
////            s = send2Card("C7");
//////            s = getDeCipherCmd(s);
////            if (s.startsWith("00")) {
////                LogDebug("CNS", "Log writing succeed");
////                return true;
////            } else {
////                LogDebug("CNS", "Commit failed");
////                return false;
////            }
//            return true;
//        } else {
//            LogDebug("CNS", "Log writing failed");
//            return false;
//        }
    }

    public boolean writeLog_plain (String fileID, String offSet, String data) throws IOException {
        String cardAPDU = "3B" + fileID + offSet + ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(data.length()/2, 3)) + data;
//        String s = getCipherCmd("0008" +
//                ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(cardAPDU.length()/2, 1)), cardAPDU);
//        String s = send2Card("3B" + fileID + offSet + ByteUtils.byteArray2HexString(ByteUtils.int2bytearray(data.length()/2, 3)) + cardAPDU);
        String s = send2Card(cardAPDU);
        //s = getDeCipherCmd(s);
        if (s.startsWith("00")) {
            LogDebug("CNS", "Wait for commit");
            // Commit SAM
            s = send2SAM("807C000001C700");
            if (s.endsWith("9000")) {
                LogDebug("CNS", "SAM committed");
            } else {
                LogDebug("CNS", "SAM committing failed");
            }
            // Card commit
            s = send2Card("C7");
            //s = getDeCipherCmd(s);
            if (s.startsWith("00")) {
                LogDebug("CNS", "Log writing succeed");
                return true;
            } else {
                LogDebug("CNS", "Commit failed");
                return false;
            }
        } else {
            LogDebug("CNS", "Log writing failed");
            return false;
        }
    }

    private void LogDebug(String name , String log){
        Log.d(name,log);
    }
}
