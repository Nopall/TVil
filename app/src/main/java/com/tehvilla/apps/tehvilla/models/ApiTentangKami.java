package com.tehvilla.apps.tehvilla.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ApiTentangKami{

        @SerializedName("result")
        @Expose
        private Boolean result;
        @SerializedName("response")
        @Expose
        private List<TentangKami> response = null;

        public Boolean getResult() {
            return result;
        }

        public void setResult(Boolean result) {
            this.result = result;
        }

        public List<TentangKami> getResponse() {
            return response;
        }

        public void setResponse(List<TentangKami> response) {
            this.response = response;
        }
}
