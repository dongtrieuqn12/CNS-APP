
package com.example.user.myfinalprojectssc_ver1.Model.Products;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("retailerId")
    @Expose
    private Integer retailerId;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("categoryId")
    @Expose
    private Integer categoryId;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("allowsSale")
    @Expose
    private Boolean allowsSale;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("hasVariants")
    @Expose
    private Boolean hasVariants;
    @SerializedName("basePrice")
    @Expose
    private Integer basePrice;
    @SerializedName("conversionValue")
    @Expose
    private Integer conversionValue;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("inventories")
    @Expose
    private List<Inventory> inventories = null;
    @SerializedName("images")
    @Expose
    private List<String> images = null;
//    @SerializedName("inventories")
//    @Expose
//    private Inventory inventories;

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(Integer retailerId) {
        this.retailerId = retailerId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Boolean getAllowsSale() {
        return allowsSale;
    }

    public void setAllowsSale(Boolean allowsSale) {
        this.allowsSale = allowsSale;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getHasVariants() {
        return hasVariants;
    }

    public void setHasVariants(Boolean hasVariants) {
        this.hasVariants = hasVariants;
    }

    public Integer getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Integer basePrice) {
        this.basePrice = basePrice;
    }

    public Integer getConversionValue() {
        return conversionValue;
    }

    public void setConversionValue(Integer conversionValue) {
        this.conversionValue = conversionValue;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

//    public Inventory getInventories(){
//        return inventories;
//    }
//
//    public void setInventories(Inventory inventories){
//        this.inventories = inventories;
//    }
}
