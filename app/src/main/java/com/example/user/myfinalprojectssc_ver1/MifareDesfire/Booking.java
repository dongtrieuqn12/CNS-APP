package com.example.user.myfinalprojectssc_ver1.MifareDesfire;

import android.util.Log;

import com.example.user.myfinalprojectssc_ver1.Others.Constants;

/**
 * Created by Ho Dong Trieu on 08/22/2018
 */
public class Booking {
    private String orderStatus;
    private String orderNumber;
    private String bookingDeviceID;
    private String deliveryDeviceID;
    private String transacCounter;
    private String orderDatetime;

//    public Booking(){
//
//    }

    public Booking(String bookingLog) {
        Log.d(Constants.TAG,bookingLog);
        this.orderStatus = bookingLog.substring(0,2);
        this.orderNumber = bookingLog.substring(2,10);
        this.bookingDeviceID = bookingLog.substring(10,18);
        this.deliveryDeviceID = bookingLog.substring(18,26);
        this.transacCounter = bookingLog.substring(26,34);
        this.orderDatetime = bookingLog.substring(34,48);
    }

    public String getOrderStatus() {
        return this.orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderNumber() {
        return this.orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getBookingDeviceID() {
        return this.bookingDeviceID;
    }

    public void setBookingDeviceID(String bookingDeviceID) {
        this.bookingDeviceID = bookingDeviceID;
    }

    public String getDeliveryDeviceID() {
        return this.deliveryDeviceID;
    }

    public void setDeliveryDeviceID(String deliveryDeviceID) {
        this.deliveryDeviceID = deliveryDeviceID;
    }

    public String getTransacCounter() {
        return this.transacCounter;
    }

    public void setTransacCounter(String transacCounter) {
        this.transacCounter = transacCounter;
    }

    public String getOrderDatetime() {
        return this.orderDatetime;
    }

    public void setOrderDatetime(String orderDatetime) {
        this.orderDatetime = orderDatetime;
    }

}