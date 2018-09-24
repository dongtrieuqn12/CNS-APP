package com.example.user.myfinalprojectssc_ver1.Table;

import java.io.Serializable;

/**
 * Created by Ho Dong Trieu on 09/06/2018
 */
public class MenuModel implements Serializable {
    private String image,name,price;
    private long id;
    private int visible;

    public MenuModel() {

    }

    public MenuModel(String image, String name, String price, long id, int visible) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.id = id;
        this.visible = visible;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }
}
