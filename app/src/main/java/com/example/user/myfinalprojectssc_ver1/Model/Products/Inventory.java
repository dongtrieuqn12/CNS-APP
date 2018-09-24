
package com.example.user.myfinalprojectssc_ver1.Model.Products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Inventory {

    @SerializedName("productId")
    @Expose
    private Integer productId;
    @SerializedName("productCode")
    @Expose
    private String productCode;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("branchId")
    @Expose
    private Integer branchId;
    @SerializedName("branchName")
    @Expose
    private String branchName;
    @SerializedName("cost")
    @Expose
    private Integer cost;
    @SerializedName("onHand")
    @Expose
    private Integer onHand;
    @SerializedName("reserved")
    @Expose
    private Integer reserved;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getOnHand() {
        return onHand;
    }

    public void setOnHand(Integer onHand) {
        this.onHand = onHand;
    }

    public Integer getReserved() {
        return reserved;
    }

    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }

}
