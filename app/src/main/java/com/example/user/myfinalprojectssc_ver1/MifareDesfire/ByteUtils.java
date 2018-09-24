package com.example.user.myfinalprojectssc_ver1.MifareDesfire;

import android.util.Log;

public class ByteUtils {
    public static byte[] hexString2ByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];
        for (int i = 0; i < len; i+=2 ) {
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String byteArray2HexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] int2bytearray(long i, int index) {
        byte[] result = new byte[index];

        for (int k = 0; k < index; k++) {
            result[k] = (byte) (i >> 8*k);
        }
        return result;
    }

    public static long numString2long(String numString) {
        Log.d("alo", "numString: " + numString);
        int len = numString.length();
        long value = 0;
        for (int i = 0; i < len; i++) {
            value += Character.digit(numString.charAt(i),10)*Math.pow(10,len - 1 - i);
        }
        Log.d("alo", "value changed: " + Long.toHexString(value));
        return value;
    }


    //test
    public static byte[] toByteArray(String hexString) {

        int hexStringLength = hexString.length();
        byte[] byteArray = null;
        int count = 0;
        char c;
        int i;

        // Count number of hex characters
        for (i = 0; i < hexStringLength; i++) {

            c = hexString.charAt(i);
            if (c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a'
                    && c <= 'f') {
                count++;
            }
        }

        byteArray = new byte[(count + 1) / 2];
        boolean first = true;
        int len = 0;
        int value;
        for (i = 0; i < hexStringLength; i++) {

            c = hexString.charAt(i);
            if (c >= '0' && c <= '9') {
                value = c - '0';
            } else if (c >= 'A' && c <= 'F') {
                value = c - 'A' + 10;
            } else if (c >= 'a' && c <= 'f') {
                value = c - 'a' + 10;
            } else {
                value = -1;
            }

            if (value >= 0) {

                if (first) {

                    byteArray[len] = (byte) (value << 4);

                } else {

                    byteArray[len] |= value;
                    len++;
                }

                first = !first;
            }
        }

        return byteArray;
    }

}
