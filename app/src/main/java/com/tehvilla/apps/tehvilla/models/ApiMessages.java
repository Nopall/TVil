package com.tehvilla.apps.tehvilla.models;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

        import java.util.ArrayList;
        import java.util.List;

/**
 * Created by NIT NB1 on 16/01/2017.
 */

public class ApiMessages {
    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("response")
    @Expose
    private List<Produk> response = new ArrayList<>();

    //result
    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    //Produk
    public List<Produk> getResponse() {
        return response;
    }

    public void setResponse(List<Produk> response) {
        this.response = response;
    }
}
