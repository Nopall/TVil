package com.tehvilla.apps.tehvilla.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SahabatDeveloper on 1/24/2017.
 */

public class ApiVerifikasi {
    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("response")
    @Expose
    private List<Verifikasi> response = null;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public List<Verifikasi> getResponse() {
        return response;
    }

    public void setResponse(List<Verifikasi> response) {
        this.response = response;
    }
}
