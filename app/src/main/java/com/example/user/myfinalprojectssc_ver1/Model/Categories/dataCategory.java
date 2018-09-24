
package com.example.user.myfinalprojectssc_ver1.Model.Categories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class dataCategory {

    @SerializedName("categoryId")
    @Expose
    private Long categoryId;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("retailerId")
    @Expose
    private Integer retailerId;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(Integer retailerId) {
        this.retailerId = retailerId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}
