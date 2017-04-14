package com.tehvilla.apps.tehvilla.models;

import android.app.admin.SecurityLog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NIT NB1 on 16/01/2017.
 */

public class ApiSlider {
    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("response")
    @Expose
    private List<Slider> response = null;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public List<Slider> getResponse() {
        return response;
    }

    public void setResponse(List<Slider> response) {
        this.response = response;
    }

}