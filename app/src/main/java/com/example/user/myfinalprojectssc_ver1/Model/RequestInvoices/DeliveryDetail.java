
package com.example.user.myfinalprojectssc_ver1.Model.RequestInvoices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryDetail {

    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("locationId")
    @Expose
    private String locationId;
    @SerializedName("status")
    @Expose
    private Integer status;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
