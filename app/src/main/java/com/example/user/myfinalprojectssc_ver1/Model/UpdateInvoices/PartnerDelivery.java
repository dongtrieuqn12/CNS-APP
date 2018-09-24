
package com.example.user.myfinalprojectssc_ver1.Model.UpdateInvoices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PartnerDelivery {

    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
