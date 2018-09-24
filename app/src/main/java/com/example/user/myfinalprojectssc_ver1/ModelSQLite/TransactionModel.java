package com.example.user.myfinalprojectssc_ver1.ModelSQLite;

/**
 * Created by Ho Dong Trieu on 09/14/2018
 */
public class TransactionModel {
    int id;
    String date_time;
    int type;
    String card_number;
    String amount;
    int status;
    String id_invoices;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId_invoices() {
        return id_invoices;
    }

    public void setId_invoices(String id_invoices) {
        this.id_invoices = id_invoices;
    }
}
