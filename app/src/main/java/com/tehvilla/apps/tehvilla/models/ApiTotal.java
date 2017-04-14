package com.tehvilla.apps.tehvilla.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AkhmadNaufal on 3/4/17.
 */
public class ApiTotal {
    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("response")
    @Expose
    private List<TotalProduk> response = new ArrayList<>();

    //result
    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public List<TotalProduk> getResponse() {
        return response;
    }

    public void setResponse(List<TotalProduk> response) {
        this.response = response;
    }
}
