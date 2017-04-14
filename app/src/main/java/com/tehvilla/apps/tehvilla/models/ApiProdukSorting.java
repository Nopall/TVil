package com.tehvilla.apps.tehvilla.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SahabatDeveloper on 1/19/2017.
 */

public class ApiProdukSorting {
    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("response")
    @Expose
    private List<ProdukSorting> response = null;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public List<ProdukSorting> getResponse() {
        return response;
    }

    public void setResponse(List<ProdukSorting> response) {
        this.response = response;
    }
}
