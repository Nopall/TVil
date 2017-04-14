package com.tehvilla.apps.tehvilla.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SahabatDeveloper on 2/7/2017.
 */

public class ApiCheckoutTf {
    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("response")
    @Expose
    private List<CheckoutTf> response = null;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public List<CheckoutTf> getResponse() {
        return response;
    }

    public void setResponse(List<CheckoutTf> response) {
        this.response = response;
    }
}
