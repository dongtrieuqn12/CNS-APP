
package com.example.user.myfinalprojectssc_ver1.Model.ErrorCode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorCode {

    @SerializedName("responseStatus")
    @Expose
    private ResponseStatus responseStatus;

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

}
