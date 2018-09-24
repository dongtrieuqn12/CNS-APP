package com.example.user.myfinalprojectssc_ver1.ModelSQLite;

/**
 * Created by Ho Dong Trieu on 09/19/2018
 */
public class KiotVietModel {
    private int id;
    private long id_kiot;
    private String code;
    private String fullName;
    private long categoryId;
    private int allowsSale;
    private String basePrice;
    private int isActive;
    private long productId;
    private String productCode;
    private String productName;
    private long branchId;
    private int onHand;
    private String images;

    public int getAllowsSale() {
        return allowsSale;
    }

    public void setAllowsSale(int allowsSale) {
        this.allowsSale = allowsSale;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getId_kiot() {
        return id_kiot;
    }

    public void setId_kiot(long id_kiot) {
        this.id_kiot = id_kiot;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }


    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
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

    public long getBranchId() {
        return branchId;
    }

    public void setBranchId(long branchId) {
        this.branchId = branchId;
    }

    public int getOnHand() {
        return onHand;
    }

    public void setOnHand(int onHand) {
        this.onHand = onHand;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
