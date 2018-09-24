
package com.example.user.myfinalprojectssc_ver1.Model.UpdateInvoices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryDetail {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("partnerDelivery")
    @Expose
    private PartnerDelivery partnerDelivery;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public PartnerDelivery getPartnerDelivery() {
        return partnerDelivery;
    }

    public void setPartnerDelivery(PartnerDelivery partnerDelivery) {
        this.partnerDelivery = partnerDelivery;
    }

}
