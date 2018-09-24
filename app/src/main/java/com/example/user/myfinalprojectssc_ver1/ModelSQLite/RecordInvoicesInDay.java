package com.example.user.myfinalprojectssc_ver1.ModelSQLite;

/**
 * Created by Ho Dong Trieu on 09/10/2018
 */
public class RecordInvoicesInDay {
    private int id;
    private String uid;
    private String InvoicesCode;
    private String amount;
    private String datetime;
    private String request;
    private int status;

    public RecordInvoicesInDay(){

    }

    public RecordInvoicesInDay(int id, String uid, String invoicesCode, String amount, String datetime, String request,int status) {
        this.request = request;
        this.status = status;
        this.id = id;
        this.uid = uid;
        InvoicesCode = invoicesCode;
        this.amount = amount;
        this.datetime = datetime;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getInvoicesCode() {
        return InvoicesCode;
    }

    public void setInvoicesCode(String invoicesCode) {
        InvoicesCode = invoicesCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
