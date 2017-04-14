package com.tehvilla.apps.tehvilla.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SahabatDeveloper on 1/23/2017.
 */

public class ApiDetailPengguna {
    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("response")
    @Expose
    private List<DetailPengguna> response = null;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public List<DetailPengguna> getResponse() {
        return response;
    }

    public void setResponse(List<DetailPengguna> response) {
        this.response = response;
    }
}
