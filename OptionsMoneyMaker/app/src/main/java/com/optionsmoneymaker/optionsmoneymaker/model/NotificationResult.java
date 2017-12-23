package com.optionsmoneymaker.optionsmoneymaker.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Sagar on 16-10-2016.
 */
public class NotificationResult {

    @Expose
    private Integer status = 0;
    @Expose
    private String data = "";

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
