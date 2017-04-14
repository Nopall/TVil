package com.tehvilla.apps.tehvilla.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SahabatDeveloper on 1/19/2017.
 */

public class Bantuan {
    @SerializedName("no_telepon")
    @Expose
    private String noTelepon;
    @SerializedName("no_sms")
    @Expose
    private String noSms;

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public String getNoSms() {
        return noSms;
    }

    public void setNoSms(String noSms) {
        this.noSms = noSms;
    }
}
