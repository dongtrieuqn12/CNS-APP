
package com.example.user.myfinalprojectssc_ver1.Model.RequestInvoices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceDetail {

    @SerializedName("productId")
    @Expose
    private Long productId;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("discount")
    @Expose
    private Integer discount;
    @SerializedName("discountRatio")
    @Expose
    private Integer discountRatio;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getDiscountRatio() {
        return discountRatio;
    }

    public void setDiscountRatio(Integer discountRatio) {
        this.discountRatio = discountRatio;
    }

}
