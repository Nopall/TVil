package com.tehvilla.apps.tehvilla.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AkhmadNaufal on 2/15/17.
 */

public class ApiPesananSaya {
    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("response")
    @Expose
    private List<PesananSaya> response = null;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public List<PesananSaya> getResponse() {
        return response;
    }

    public void setResponse(List<PesananSaya> response) {
        this.response = response;
    }
}
