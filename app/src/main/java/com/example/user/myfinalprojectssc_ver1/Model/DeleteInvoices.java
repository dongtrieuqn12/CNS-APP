package com.example.user.myfinalprojectssc_ver1.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ho Dong Trieu on 09/11/2018
 */
public class DeleteInvoices {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("isVoidPayment")
    @Expose
    private boolean isVoidPayment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isVoidPayment() {
        return isVoidPayment;
    }

    public void setVoidPayment(boolean voidPayment) {
        isVoidPayment = voidPayment;
    }
}
