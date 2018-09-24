
package com.example.user.myfinalprojectssc_ver1.Model.GetAllInvoices;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class getAllDataInvoices {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("purchaseDate")
    @Expose
    private String purchaseDate;
    @SerializedName("branchId")
    @Expose
    private Integer branchId;
    @SerializedName("branchName")
    @Expose
    private String branchName;
    @SerializedName("soldById")
    @Expose
    private Integer soldById;
    @SerializedName("soldByName")
    @Expose
    private String soldByName;
    @SerializedName("customerId")
    @Expose
    private Integer customerId;
    @SerializedName("customerCode")
    @Expose
    private String customerCode;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("totalPayment")
    @Expose
    private Integer totalPayment;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("statusValue")
    @Expose
    private String statusValue;
    @SerializedName("usingCod")
    @Expose
    private Boolean usingCod;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("invoiceDetails")
    @Expose
    private List<AllInvoiceDetail> invoiceDetails = null;
    @SerializedName("invoiceOrderSurcharges")
    @Expose
    private List<Object> invoiceOrderSurcharges = null;
    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Integer getSoldById() {
        return soldById;
    }

    public void setSoldById(Integer soldById) {
        this.soldById = soldById;
    }

    public String getSoldByName() {
        return soldByName;
    }

    public void setSoldByName(String soldByName) {
        this.soldByName = soldByName;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(Integer totalPayment) {
        this.totalPayment = totalPayment;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
    }

    public Boolean getUsingCod() {
        return usingCod;
    }

    public void setUsingCod(Boolean usingCod) {
        this.usingCod = usingCod;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public List<AllInvoiceDetail> getInvoiceDetails() {
        return invoiceDetails;
    }

    public void setInvoiceDetails(List<AllInvoiceDetail> invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
    }

    public List<Object> getInvoiceOrderSurcharges() {
        return invoiceOrderSurcharges;
    }

    public void setInvoiceOrderSurcharges(List<Object> invoiceOrderSurcharges) {
        this.invoiceOrderSurcharges = invoiceOrderSurcharges;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

}
