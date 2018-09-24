
package com.example.user.myfinalprojectssc_ver1.Model.UpdateInvoices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateInvoices {

    @SerializedName("usingCod")
    @Expose
    private Boolean usingCod;
    @SerializedName("deliveryDetail")
    @Expose
    private DeliveryDetail deliveryDetail;

    public Boolean getUsingCod() {
        return usingCod;
    }

    public void setUsingCod(Boolean usingCod) {
        this.usingCod = usingCod;
    }

    public DeliveryDetail getDeliveryDetail() {
        return deliveryDetail;
    }

    public void setDeliveryDetail(DeliveryDetail deliveryDetail) {
        this.deliveryDetail = deliveryDetail;
    }

}
