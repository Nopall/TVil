package com.tehvilla.apps.tehvilla.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AkhmadNaufal on 3/6/17.
 */

public class EditPengguna {
    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("response")
    @Expose
    private List<Pengguna> response = null;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public List<Pengguna> getResponse() {
        return response;
    }

    public void setResponse(List<Pengguna> response) {
        this.response = response;
    }
}
