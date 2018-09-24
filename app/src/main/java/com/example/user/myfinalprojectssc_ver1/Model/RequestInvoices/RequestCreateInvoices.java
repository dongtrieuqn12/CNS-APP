
package com.example.user.myfinalprojectssc_ver1.Model.RequestInvoices;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestCreateInvoices {

    @SerializedName("branchId")
    @Expose
    private String branchId;
    @SerializedName("branchName")
    @Expose
    private String branchName;
    @SerializedName("totalPayment")
    @Expose
    private String totalPayment;
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("soldById")
    @Expose
    private String soldById;
    @SerializedName("customerId")
    @Expose
    private String customerId;
    @SerializedName("usingCod")
    @Expose
    private boolean usingCod;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("invoiceDetails")
    @Expose
    private List<InvoiceDetail> invoiceDetails = null;
    @SerializedName("deliveryDetail")
    @Expose
    private DeliveryDetail deliveryDetail;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isUsingCod() {
        return usingCod;
    }

    public void setUsingCod(boolean usingCod) {
        this.usingCod = usingCod;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSoldById() {
        return soldById;
    }

    public void setSoldById(String soldById) {
        this.soldById = soldById;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<InvoiceDetail> getInvoiceDetails() {
        return invoiceDetails;
    }

    public void setInvoiceDetails(List<InvoiceDetail> invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
    }

    public DeliveryDetail getDeliveryDetail() {
        return deliveryDetail;
    }

    public void setDeliveryDetail(DeliveryDetail deliveryDetail) {
        this.deliveryDetail = deliveryDetail;
    }

}
