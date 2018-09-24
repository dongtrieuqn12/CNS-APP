
package com.example.user.myfinalprojectssc_ver1.Model.GetAllInvoices;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllInvoices {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("pageSize")
    @Expose
    private Integer pageSize;
    @SerializedName("data")
    @Expose
    private List<getAllDataInvoices> data = null;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<getAllDataInvoices> getData() {
        return data;
    }

    public void setData(List<getAllDataInvoices> data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
