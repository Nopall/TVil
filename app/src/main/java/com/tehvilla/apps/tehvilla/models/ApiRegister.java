package com.tehvilla.apps.tehvilla.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiRegister {
    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("response")
    @Expose
    private List<Register> response = null;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public List<Register> getResponse() {
        return response;
    }

    public void setResponse(List<Register> response) {
        this.response = response;
    }
}
