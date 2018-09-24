package com.example.user.myfinalprojectssc_ver1.Table;

/**
 * Created by Ho Dong Trieu on 09/10/2018
 */
public class InvoicesModel {
    private String name,price;
    private int selected;

    public InvoicesModel() {

    }

    public InvoicesModel(String name, String price, int selected) {
        this.name = name;
        this.price = price;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
