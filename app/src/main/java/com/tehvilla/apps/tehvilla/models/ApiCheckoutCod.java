package com.tehvilla.apps.tehvilla.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SahabatDeveloper on 1/25/2017.
 */

public class ApiCheckoutCod {
    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("response")
    @Expose
    private List<CheckoutCod> response = null;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public List<CheckoutCod> getResponse() {
        return response;
    }

    public void setResponse(List<CheckoutCod> response) {
        this.response = response;
    }
}
