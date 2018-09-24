package com.example.user.myfinalprojectssc_ver1.Table;

/**
 * Created by Ho Dong Trieu on 09/12/2018
 */
public class StoreModel {

    String idInvoices;
    String FoodName;
    String datetime;
    int checkbox;
    int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getIdInvoices() {
        return idInvoices;
    }

    public void setIdInvoices(String idInvoices) {
        this.idInvoices = idInvoices;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public int getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(int checkbox) {
        this.checkbox = checkbox;
    }
}
